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

import de.elomagic.hl7inspector.hl7.model.Message;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Vector;
import org.apache.log4j.Logger;
/**
 *
 * @author rambow
 */
public class SendThread extends Thread implements IOCharListener {
    
    /** Creates a new instance of ReceiveThread */
    public SendThread() { }    
    
    public SendOptionsBean getOptions() { return options; }    
    public void setOptions(SendOptionsBean o) { options = o; }
    
    public void setMessages(Vector<Message> list) { messages = list; }
        
    public void terminateRequest() {
        /*if (!terminate) {
            fireStatusEvent("Cancel sending message...");
        }*/
        
        terminate = true;
        
        if (socket != null) {
            try {
                if (socket.isConnected()) {
                    socket.close();
                    //socket.setSoTimeout(1);
                }
            } catch (Exception e) {
                Logger.getLogger(getClass()).warn(e.getMessage(), e);
            }
        }
    }    
    
    public void run() {
        fireThreadStartedEvent();
        try {
            while ((!terminate) && (!done)) {
                try {
                    if (socket == null) {
                        fireStatusEvent("Connecting system " + options.getHost() + ":" + options.getPort() + "...");
                        
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(options.getHost(), options.getPort()));
                        fireStatusEvent("System " + options.getHost() + ":" + options.getPort() + " connected.");
                        writer    = new OutputStreamWriter(socket.getOutputStream(), options.getEncoding());
                        reader    = new InputStreamReader(socket.getInputStream(), options.getEncoding());
                        
                        sendMessages();
                    }
                } catch (Exception e) {
                    Logger.getLogger(getClass()).warn(e.getMessage(), e);
                    fireStatusEvent((e.getMessage()!=null)?e.getMessage():e.toString()); 
                    terminateRequest();                    
                }
            }
            
            if (socket != null) {
//                if (socket.isConnected()) {
                    fireStatusEvent("Disconnecting from system " + options.getHost() + ":" + options.getPort() + "...");
/*                    if (writer != null) {
                        writer.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }*/
                    socket.close();
                    fireStatusEvent("System " + options.getHost() + ":" + options.getPort() + " disconnected.");
//                }
            }                        
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e, e);
            fireStatusEvent((e.getMessage()!=null)?e.getMessage():e.toString());            
        }
        fireStatusEvent("Send messages server stopped.");        
        fireThreadStoppedEvent();        
    }
        
    private void sendMessages() throws IOException { 
        while ((messages.size() > 0) && (!terminate)) {
            Message message = messages.get(0);

            writeMessage(message);
            
            message = readAcknowledge();
            
            messages.remove(0);
        }
        done = true;
        fireStatusEvent("All selected messaged send."); 
    }
    
    private void writeMessage(Message message) throws IOException {
        fireStatusEvent("Send message...");
        StringBuffer sb = new StringBuffer();
        sb.append(options.getFrame().getStartFrame());
        sb.append(message.toString());
        sb.append(options.getFrame().getStopFrame());
        
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            
            fireCharSendEvent(c);
            writer.write(c);
        }        
        writer.flush();
        fireStatusEvent("Message send.");        
    }    
    
    private Message readAcknowledge() throws IOException {
        fireStatusEvent("Waiting for acknowledge message...");
        
        MessageParserStreamReader messageReader = new MessageParserStreamReader(reader, MessageParserStreamReader.FRAMED_FORMAT, options.getFrame());
        messageReader.addListener(this);
        Message message = messageReader.readMessage();
        
        fireStatusEvent("Acknowledge message received.");
        
        return message;
    }
    
    public void addListener(IOThreadListener value) { listener.add(value); }    
    public void removeListener(IOThreadListener value) { listener.remove(value); }
    
    private SendOptionsBean options     = new SendOptionsBean();
    private Vector<Message> messages    = new Vector<Message>();
           
    private Socket              socket;
    private OutputStreamWriter  writer;
    private InputStreamReader   reader;
    
    private boolean     terminate   = false;
    private boolean     done        = false;
        
    protected void fireThreadStartedEvent() {
        for (int i=0; i<listener.size();i++)
            listener.get(i).threadStarted(this);
    }
    
    protected void fireThreadStoppedEvent() {
        for (int i=0; i<listener.size();i++)
            listener.get(i).threadStopped(this);
    }
    
    protected void fireCharReceivedEvent(char c) {
        for (int i=0; i<listener.size();i++)
            listener.get(i).charReceived(this, c);
    }
    
    protected void fireCharSendEvent(char c) {
        for (int i=0; i<listener.size();i++)
            listener.get(i).charSend(this, c);
    }    
    
    protected void fireStatusEvent(String text) {
        for (int i=0; i<listener.size();i++)
            listener.get(i).status(this, text);
    }        
    
    private Vector<IOThreadListener> listener = new Vector<IOThreadListener>();

    // Interface IOCharListener
    public void charSend(Object source, char c) { fireCharSendEvent(c); }

    public void charReceived(Object source, char c) { fireCharReceivedEvent(c); }
}
