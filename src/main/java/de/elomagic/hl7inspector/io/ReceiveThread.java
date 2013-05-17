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

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.ImportOptionBean;
import de.elomagic.hl7inspector.gui.ImportOptionBean.StreamFormat;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.hl7.model.Segment;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import de.elomagic.hl7inspector.utils.StringVector;

/**
 *
 * @author rambow
 */
public class ReceiveThread extends Thread implements IOCharListener {
    private ImportOptionBean options = new ImportOptionBean();
    private int port = 2100;
    private boolean reuse = true;
    private boolean authentication = false;
    private boolean encryption = false;
    private Socket socket;
    private boolean terminate = false;
    private List<IOThreadListener> listener = new ArrayList<>();

    /** Creates a new instance of ReceiveThread */
    public ReceiveThread() {
        options.setSource("IP Socket");
        options.setImportMode(StreamFormat.FRAMED);
    }

    public Frame getFrame() {
        return options.getFrame();
    }

    public void setFrame(Frame f) {
        options.setFrame(f);
    }

    public ImportOptionBean getOptions() {
        return options;
    }

    public void setOptions(ImportOptionBean o) {
        options = o;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int p) {
        port = p;
    }

    public boolean isReuseSocket() {
        return reuse;
    }

    public void setReUseSocket(boolean value) {
        reuse = value;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public boolean isEncryption() {
        return encryption;
    }

    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }

    public void terminateRequest() {
        if(!terminate) {
            fireStatusEvent("Shutting down receive server...");
        }

        terminate = true;
        try {
            if(socket != null) {
                if(!socket.isClosed()) {
                    socket.close();
                }
            } else {
                new Socket("localhost", port).close();
            }
        } catch(Exception ex) {
            Logger.getLogger(getClass()).warn(ex.getMessage(), ex);
            fireStatusEvent((ex.getMessage() != null) ? ex.getMessage() : ex.toString());
        }
    }

    @Override
    public void run() {
        fireThreadStartedEvent();
        try {
            if(isAuthentication() || isEncryption()) {
                fireStatusEvent("Security enabled. (Encryption=" + Boolean.toString(isEncryption()) + ", Authentication=" + Boolean.toString(isAuthentication()) + ")");
            }
            fireStatusEvent("Listening on port " + port);
            ServerSocket server = new ServerSocket(port);
            try {
                while(!terminate) {
                    try {
                        socket = server.accept();
                        try {
//                        socket.setSoTimeout(5000);
                            if(!terminate) {
                                fireStatusEvent("Connecting from " + socket.getInetAddress().getHostName() + "(" + socket.getInetAddress().getHostAddress() + ").");
                            }

                            Reader reader = new InputStreamReader(socket.getInputStream(), options.getEncoding());

                            MessageParserStreamReader messageReader = new MessageParserStreamReader(reader, StreamFormat.FRAMED, options.getFrame());
                            try {
                                messageReader.addListener(this);

                                while(!terminate && reuse) {
                                    fireStatusEvent("Waiting for data...");
                                    Message message = messageReader.readMessage();
                                    handleMessage(message);
                                }
                            } finally {
                                messageReader.removeListener(this);
                            }
                        } finally {
                            socket.close();
                            socket = null;
                        }
                    } catch(EndOfStreamException ex) {
                        fireStatusEvent("End of stream detected. Still listening on port " + port);
                    }
                }
            } finally {
                server.close();
            }
        } catch(Exception ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            fireStatusEvent(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }
        fireStatusEvent("Receive server stopped.");
        fireThreadStoppedEvent();
    }

    public void addListener(IOThreadListener value) {
        listener.add(value);
    }

    public void removeListener(IOThreadListener value) {
        listener.remove(value);
    }

    private void handleMessage(Message message) {
        boolean ignore = false;
        // Now filtering
        if(!options.getPhrase().isEmpty()) {
            String m = options.isCaseSensitive() ? message.toString() : message.toString().toUpperCase();
            String phrase = options.isCaseSensitive() ? options.getPhrase() : options.getPhrase().toUpperCase();

            if(!options.isUseRegExpr()) {
                boolean found = m.contains(phrase);
                ignore = ((!found && !options.isNegReg()) || (found && options.isNegReg()));
            }
        }

        Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getModel();
        model.locked();
        try {
            model.addMessage(message);

            // Check buffer overflow
            while(model.getChildCount(model) > options.getBufferSize()) {
                if(options.isReadBottom()) {
                    model.removeChild(model, 0);
                } else {
                    model.removeChild(model, model.getChildCount(model) - 1);
                }
            }
        } finally {
            model.unlock();
        }

        fireStatusEvent("Sending acknowledge...");

        try {
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            try {
                Segment msh = message.getSegment("MSH");

                out.write(options.getFrame().getStartFrame());

                StringVector seg = new StringVector();
                seg.add("MSH");
                seg.add("^~\\&");
                seg.add(getField(msh, 5)); // Field 3
                seg.add(getField(msh, 6)); // Field 4
                seg.add(getField(msh, 3)); // Field 5
                seg.add(getField(msh, 4)); // Field 6
                seg.add(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); // Field 7
                seg.add(""); // Field 8
                seg.add("ACK");//^^" + msh.get(9).toString()); // Field 9
                seg.add(getField(msh, 10)); // Field 10
                seg.add(getField(msh, 11)); // Field 11
                seg.add(getField(msh, 12)); // Field 12
                seg.add(""); // Field 13
                seg.add(""); // Field 14
                seg.add(""); // Field 15
                seg.add(""); // Field 16
                seg.add(""); // Field 17
                seg.add(getField(msh, 18)); // Field 18
                out.write(seg.toString('|'));
                out.write("\r");

                seg = new StringVector();
                seg.add("MSA");
                seg.add("AA");
                seg.add(getField(msh, 10));

                out.write(seg.toString('|'));
                out.write("\r");

                out.write(options.getFrame().getStopFrame());
            } finally {
                out.flush();
            }
        } catch(Exception ex) {
            fireStatusEvent(ex.getMessage());
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
        }
    }

    private String getField(Segment seg, int index) {
        String result = "";

        if(seg.size() >= index) {
            result = seg.get(index).toString();
        }
        return result;
    }

    // protected methods
    protected void fireThreadStartedEvent() {
        for(IOThreadListener l : listener) {
            l.threadStarted(this);
        }
    }

    protected void fireThreadStoppedEvent() {
        for(IOThreadListener l : listener) {
            l.threadStopped(this);
        }
    }

    protected void fireCharReceivedEvent(char c) {
        for(IOThreadListener l : listener) {
            l.charReceived(this, c);
        }
    }

    protected void fireCharSendEvent(char c) {
        for(IOThreadListener l : listener) {
            l.charSend(this, c);
        }
    }

    protected void fireStatusEvent(String text) {
        for(IOThreadListener l : listener) {
            l.status(this, text);
        }
    }

    // Interface IOCharListener
    @Override
    public void charSend(Object source, char c) {
        fireCharSendEvent(c);
    }

    @Override
    public void charReceived(Object source, char c) {
        fireCharReceivedEvent(c);
    }
}
