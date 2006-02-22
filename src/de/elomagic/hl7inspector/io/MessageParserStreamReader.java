/*
 * Copyright 2006 Carsten Rambow
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

import de.elomagic.hl7inspector.hl7.model.Delimiters;
import de.elomagic.hl7inspector.hl7.model.Message;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author rambow
 */
public class MessageParserStreamReader {
    
    /** Creates a new instance of FileParserStream */
    public MessageParserStreamReader(Reader reader, int format, Frame frame) {
        this.reader = reader;
        this.format = format;
        this.frame = frame;
    }
    
    public final static int AUTO_DETECT_FORMAT  = 0;
    public final static int FRAMED_FORMAT       = 1;
    public final static int LINE_FORMAT         = 2;
    
    public Message readMessage() throws IOException {
        return readNextMessage();
    }
    
    public boolean available() { return false; }
    
    public long getBytesRead() { return bytesReads; }
    
    public void addListener(IOCharListener value) { listener.add(value); }
    public void removeListener(IOCharListener value) { listener.remove(value); }
    
    private Reader reader;
    private int format;
    private Frame frame;
    private LineNumberReader  lineReader = null;
    private long bytesReads = 0;
    
    private String bufferedLine = "";
    
    private Vector<IOCharListener> listener = new Vector<IOCharListener>();
    
    private Message readNextMessage() throws IOException {
        Message result = null;
        
        // Format detection
        Character cc = null;
        if (format == AUTO_DETECT_FORMAT) {
            int c = reader.read();
            if (c == -1) {
                throw new IOException("Error: End of stream reached.");
            }            
            
            cc = new Character((char)c);
            bytesReads++;
            
            format = (cc.charValue() == frame.getStartFrame())?FRAMED_FORMAT:LINE_FORMAT;
            
            fireCharReceived("Using parser format: " + ((format==FRAMED_FORMAT)?"FRAMED_FORMAT":"LINE_FORMAT"));
        }
        
        switch (format) {
            case FRAMED_FORMAT: { // Framed messages
                boolean done = false;
                char c;
                
                // Wait for start frame
                do {
                    c = (cc != null)?cc.charValue():(char)reader.read();
                    
                    if (c == 0xffff) {
                        throw new IOException("Error: End of stream reached.");
                    }
                    
                    bytesReads++;
                    fireCharReceived(c);
                    
                } while (c != frame.getStartFrame());
                
                Vector<String> msgText = new Vector<String>();
                
                String msgCache = "";
                
                while (!done) {
                    String line = readSegment();
                    bytesReads = bytesReads + line.length() + 1;
                    
                    line = msgCache.concat(line);
                    
                    msgCache = "";
                    msgText.add(line);
                    
                    // Next chars, may be the end frame
                    char ec[] = new char[frame.getStopFrameLength()];
                    
                    int r = reader.read(ec);
                    if (r == -1) {
                        throw new IOException("Error: End of stream reached.");
                    }
                    
                    bytesReads = bytesReads + r;
                    fireCharReceived(new String(ec));
                    
                    // Is end frame received
                    done = Arrays.equals(ec, frame.getStopFrame());
                    
                    if (!done) {
                        msgCache = new String(ec);
                    }
                }
                
                StringBuffer m = new StringBuffer();
                for (int i=0; i<msgText.size(); i++) {
                    m = m.append(msgText.get(i).toString()).append((char)0xd);
                }
                
                result = new Message();
                result.parse(m.toString());
                
                break;
            }
            case LINE_FORMAT: { // Unframed messages
                if (lineReader == null) {
                    lineReader = new LineNumberReader(reader);
                }
                
                String line = (bufferedLine.length() != 0)?bufferedLine:lineReader.readLine();
                
                if (line != null) {
                    bytesReads = bytesReads + line.length() + 1;
                }
                
                if (cc != null) {
                    line = cc.charValue() + line;
                    cc = null;
                }
                bufferedLine = "";
                
                boolean done = false;
                
                while ((line != null) && (!done)) {
                    bytesReads = bytesReads + line.length()+1;
                    int x = line.indexOf("MSH");
                    if (x != -1) { // New message begins
                        Delimiters del = new Delimiters(line.substring(x+3));
                        
                        Vector<String> msgText = new Vector<String>();
                        
                        while ((line != null) && (!done)) {
                            if (!line.equals("")) {
                                String seg = (line.length() > x+3)?line.substring(x):"";
                                
                                if ((msgText.size() != 0) && (seg.startsWith("MSH"))) {
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
                        
                        StringBuffer m = new StringBuffer();
                        for (int i=0; i<msgText.size(); i++) {
                            m = m.append(msgText.get(i).toString()).append((char)0xd);
                        }
                        
                        result = new Message();
                        result.parse(m.toString());
                    } else {
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
    
    public String readSegment() throws IOException {
        StringBuffer sb = new StringBuffer();
        
        int c = reader.read();
        if (c == -1) {
            throw new IOException("Error: End of stream reached.");
        }
        
        fireCharReceived((char)c);
        
        while (c != 0x0d) {
            sb.append((char)c);
            
            c = reader.read();
            if (c == -1) {
                throw new IOException("Error: End of stream reached.");
            }            
            fireCharReceived((char)c);
        }
        
        return sb.toString();        
    }
    
    protected void fireCharReceived(String s) {
        for (int q=0; q<s.length();q++) {
            for (int i=0; i<listener.size();i++)
                listener.get(i).charReceived(this, s.charAt(q));
        }
    }
    
    protected void fireCharReceived(char c) {
        for (int i=0; i<listener.size();i++)
            listener.get(i).charReceived(this, c);
    }
}
