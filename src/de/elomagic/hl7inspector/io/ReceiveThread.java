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

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.ImportOptionBean;
import de.elomagic.hl7inspector.gui.ImportOptionBean.StreamFormat;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.hl7.model.Segment;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author rambow
 */
public class ReceiveThread extends Thread implements IOCharListener {
    
    /** Creates a new instance of ReceiveThread */
    public ReceiveThread() {
        options.setSource("IP Socket");
        options.setImportMode(StreamFormat.FRAMED);
    }
    
    public Frame getFrame() { return options.getFrame(); }
    public void setFrame(Frame f) { options.setFrame(f); }
    
    public ImportOptionBean getOptions() { return options; }
    public void setOptions(ImportOptionBean o) { options = o; }
    
    public int getPort() { return port; }
    public void setPort(int p) { port = p; }
    
    public boolean isReuseSocket() { return reuse; }
    public void setReUseSocket(boolean value) { reuse = value; }
    
    public boolean isAuthentication() { return authentication; }
    public void setAuthentication(boolean authentication) { this.authentication = authentication; }
    
    public boolean isEncryption() { return encryption; }
    public void setEncryption(boolean encryption) { this.encryption = encryption; }
    
    public void terminateRequest() {
        if (!terminate) {
            fireStatusEvent("Shutting down receive server...");
        }
        
        terminate = true;
        try {
            if (socket != null) {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } else {
                new Socket("localhost", port).close();
            }
        } catch(Exception e) {
            Logger.getLogger(getClass()).warn(e, e);
            fireStatusEvent((e.getMessage()!=null)?e.getMessage():e.toString());
        }
    }
    
    public void run() {
        fireThreadStartedEvent();
        try {
            if ((isAuthentication()) || isEncryption()) {
                fireStatusEvent("Security enabled. (Encryption=" + Boolean.toString(isEncryption()) + ", Authentication=" + Boolean.toString(isAuthentication()) + ")");
            }
            fireStatusEvent("Listen on port " + port);
            ServerSocket server = new ServerSocket(port);
            try {
                while (!terminate) {
                    socket = server.accept();
                    try {
//                        socket.setSoTimeout(5000);
                        if (!terminate) {
                            fireStatusEvent("Connecting from " + socket.getInetAddress().getHostName() + "(" + socket.getInetAddress().getHostAddress() + ").");
                        }
                        
                        writer  = new OutputStreamWriter(socket.getOutputStream());
                        reader  = new InputStreamReader(socket.getInputStream());
                        
                        MessageParserStreamReader messageReader = new MessageParserStreamReader(reader, StreamFormat.FRAMED, options.getFrame());
                        try {
                            messageReader.addListener(this);
                            
                            while ((!terminate) && (reuse)) {
                                fireStatusEvent("Waiting for data...");
                                Message message = messageReader.readMessage();
                                handleMessage(message);
                            }
                        } finally {
                            messageReader.removeListener(this);
                        }
                    } finally {
                        socket.close();
                    }
                }
            } finally {
                server.close();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e, e);
            fireStatusEvent((e.getMessage()!=null)?e.getMessage():e.toString());
        }
        fireStatusEvent("Receive server stopped.");
        fireThreadStoppedEvent();
    }
    
    public void addListener(IOThreadListener value) { listener.add(value); }
    public void removeListener(IOThreadListener value) { listener.remove(value); }
    
    private void handleMessage(Message message) {
        boolean ignore = false;
        // Now filtering
        if (options.getPhrase().length() != 0) {
            String m = (options.isCaseSensitive()?message.toString():message.toString().toUpperCase());
            String phrase = (options.isCaseSensitive()?options.getPhrase():options.getPhrase().toUpperCase());
            
            if (!options.isUseRegExpr()) {
                boolean found = (m.indexOf(phrase) != -1);
                ignore =((( !found && !options.isNegReg()) ||
                        found && options.isNegReg()));
            }
        }
        
        Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getModel();
        model.locked();
        try {
            model.addMessage(message);
            
            // Check buffer overflow
            while (model.getChildCount(model) > options.getBufferSize()) {
                if (options.isReadBottom())
                    model.removeChild(model, 0);
                else
                    model.removeChild(model, model.getChildCount(model)-1);
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
                out.write("MSH|^~\\&|" + msh.get(5).toString() + "|" + msh.get(6).toString() + "|" + msh.get(3).toString() + "|" + msh.get(4).toString() + "|" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +  "|||".concat("\r"));
                out.write("MSA|AA".concat("\r"));
                out.write(options.getFrame().getStopFrame());
            } finally {
                out.flush();
            }
        } catch (Exception e) {
            fireStatusEvent(e.getMessage());
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
    }
    
    // protected methods
    
    protected void fireThreadStartedEvent() {
        for (int i=0; i<listener.size();i++) {
            listener.get(i).threadStarted(this);
        }
    }
    
    protected void fireThreadStoppedEvent() {
        for (int i=0; i<listener.size();i++) {
            listener.get(i).threadStopped(this);
        }
    }
    
    protected void fireCharReceivedEvent(char c) {
        for (int i=0; i<listener.size();i++) {
            listener.get(i).charReceived(this, c);
        }
    }
    
    protected void fireCharSendEvent(char c) {
        for (int i=0; i<listener.size();i++) {
            listener.get(i).charSend(this, c);
        }
    }
    
    protected void fireStatusEvent(String text) {
        for (int i=0; i<listener.size();i++) {
            listener.get(i).status(this, text);
        }
    }
    
    // private
    
    private MessageImportThread importThread = null;
    
    private int                 port            = 2100;
    private boolean             reuse           = true;
    private ImportOptionBean    options         = new ImportOptionBean();
    private boolean             authentication  = false;
    private boolean             encryption      = false;
    
    private Socket              socket;
    private OutputStreamWriter  writer;
    private InputStreamReader   reader;
    
    private boolean      terminate   = false;
    
    private Vector<IOThreadListener> listener = new Vector<IOThreadListener>();
    
    // Interface IOCharListener
    public void charSend(Object source, char c) { fireCharSendEvent(c); }
    
    public void charReceived(Object source, char c) { fireCharReceivedEvent(c); }
}
