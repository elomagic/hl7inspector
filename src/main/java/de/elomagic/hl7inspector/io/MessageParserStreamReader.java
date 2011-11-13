/*
 * Copyright 2010 Carsten Rambow
 * 
 * Licensed under the GNU Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.gnu.org/licenses/gpl.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.elomagic.hl7inspector.io;

import de.elomagic.hl7inspector.gui.ImportOptionBean.StreamFormat;
import de.elomagic.hl7inspector.hl7.model.Delimiters;
import de.elomagic.hl7inspector.hl7.model.Message;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author rambow
 */
public class MessageParserStreamReader {

    /** Creates a new instance of FileParserStream */
    public MessageParserStreamReader(Reader reader, StreamFormat format, Frame frame) {
        this.reader = reader;
        this.format = format;
        this.frame = frame;
    }

    public Message readMessage() throws IOException {
        return readNextMessage();
    }

    private long bytesReads = 0;

    public long getBytesRead() {
        return bytesReads;
    }

    private List<IOCharListener> listener = new ArrayList<IOCharListener>();

    public void addListener(IOCharListener value) {
        listener.add(value);
    }

    public void removeListener(IOCharListener value) {
        listener.remove(value);
    }

    private Reader reader;
    private StreamFormat format;
    private Frame frame;
    private LineNumberReader lineReader = null;
    private String bufferedLine = "";

    private Message readNextMessage() throws IOException {
        Message result = null;

        // Format detection
        Character cc = null;
        if (format == StreamFormat.AUTO_DETECT) {
            int c = reader.read();
            if (c == -1) {
                throw new EndOfStreamException();
            }

            cc = new Character((char) c);
            bytesReads++;

            format = cc.charValue() == frame.getStartFrame() ? StreamFormat.FRAMED : StreamFormat.TEXT_LINE;

            fireCharReceived("Using parser format: " + ((format == StreamFormat.FRAMED) ? "FRAMED_FORMAT" : "LINE_FORMAT"));
        }

        switch (format) {
            case FRAMED: { // Framed messages
                boolean done = false;
                char c;

                // Wait for start frame
                do {
                    c = cc != null ? cc.charValue() : (char) reader.read();

                    if (c == 0xffff) {
                        throw new EndOfStreamException();
                    }

                    bytesReads++;
                    fireCharReceived(c);

                } while (c != frame.getStartFrame());

                StringBuilder sb = new StringBuilder();

                while (!done) {
                    int m = reader.read();

                    if (c == -1) {
                        throw new EndOfStreamException();
                    }

                    bytesReads++;
                    sb.append((char) m);
                    fireCharReceived((char) m);

                    // Check on stop frame
                    if (sb.length() >= frame.getStopFrameLength()) {
                        char ec[] = new char[frame.getStopFrameLength()];
                        sb.getChars(sb.length() - frame.getStopFrameLength(), sb.length(), ec, 0);

                        done = Arrays.equals(ec, frame.getStopFrame());

                        if (done) {
                            // Cut stop frame
                            sb.delete(sb.length() - frame.getStopFrameLength(), sb.length());
                        }
                    }
                }

                if (sb.charAt(sb.length() - 1) != 0x0d) {
                    fireCharReceived("Warning: Last segment have no segment termination char 0x0d !");
                    sb.append((char) 0xd);
                }

                result = new Message();
                String msg = sb.toString();

                result.parse(msg);

                break;
            }
            case TEXT_LINE: { // Unframed messages
                if (lineReader == null) {
                    lineReader = new LineNumberReader(reader);
                }

                String line = bufferedLine.length() != 0 ? bufferedLine : lineReader.readLine();
                if (cc != null) {
                    line = cc.charValue() + line;
                    cc = null;
                }
                bufferedLine = "";

                boolean done = false;

                while ((line != null) && !done) {
                    int x = line.indexOf("MSH");
                    if (x != -1) { // New message begins
                        Delimiters del = new Delimiters(line.substring(x + 3));

                        List<String> msgText = new ArrayList<String>();

                        while ((line != null) && !done) {
                            bytesReads += line.length() + 1;
                            if (!line.isEmpty()) {
                                String seg = (line.length() > x + 3) ? line.substring(x) : "";

                                if (!msgText.isEmpty() && seg.startsWith("MSH")) {
                                    bufferedLine = line;
                                    done = true;
                                } else {
                                    if (seg.indexOf(del.fieldDelimiter) == 3) {
                                        msgText.add(line.substring(x));
                                    }
                                }
                            }
                            if (!done) {
                                line = lineReader.readLine();
                            }

                        }

                        StringBuilder m = new StringBuilder();
                        for (String s : msgText) {
                            m = m.append(s).append((char) 0xd);
                        }

                        result = new Message();
                        result.parse(m.toString(), del);
                    } else {
                        bytesReads += line.length() + 1;
                        line = lineReader.readLine();
                    }
                }
                break;
            }
            default: {
                throw new IOException("Unknown format detection setup. Check your configuration.");
            }
        }

        return result;
    }

    protected void fireCharReceived(String s) {
        for (char c : s.toCharArray()) {
            for (IOCharListener l : listener) {
                l.charReceived(this, c);
            }
        }
    }

    protected void fireCharReceived(char c) {
        for (IOCharListener l : listener) {
            l.charReceived(this, c);
        }
    }

}
