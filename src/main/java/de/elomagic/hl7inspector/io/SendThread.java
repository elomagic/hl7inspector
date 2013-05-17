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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.gui.ImportOptionBean.StreamFormat;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.hl7.model.Segment;

/**
 *
 * @author rambow
 */
public class SendThread extends Thread implements IOCharListener {
    private SendOptionsBean options = new SendOptionsBean();
    private List<Message> messages = new ArrayList<>();
    private Socket socket;
    private OutputStreamWriter writer;
    private InputStreamReader reader;
    private boolean terminate = false;
    private boolean done = false;
    private List<IOThreadListener> listener = new ArrayList<>();

    /** Creates a new instance of ReceiveThread */
    public SendThread() {
    }

    public SendOptionsBean getOptions() {
        return options;
    }

    public void setOptions(SendOptionsBean o) {
        options = o;
    }

    public void setMessages(List<Message> list) {
        messages = list;
    }

    public void terminateRequest() {
        /*if (!terminate) {
         fireStatusEvent("Cancel sending message...");
         }*/

        terminate = true;

        if(socket != null) {
            try {
                if(socket.isConnected()) {
                    socket.close();
                    //socket.setSoTimeout(1);
                }
            } catch(Exception e) {
                Logger.getLogger(getClass()).warn(e.getMessage(), e);
            }
        }
    }

    @Override
    public void run() {
        fireThreadStartedEvent();
        try {
            while(!terminate && !done) {
                try {
                    if(socket == null) {
                        if(options.isAuthentication() || options.isEncryption()) {
                            fireStatusEvent("Security enabled. (Encryption=" + Boolean.toString(options.isEncryption()) + ", Authentication=" + Boolean.toString(options.isAuthentication()) + ")");
                        }

                        fireStatusEvent("Connecting system " + options.getHost() + ":" + options.getPort() + "...");

                        SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();

                        socket = new Socket();
                        socket.connect(new InetSocketAddress(options.getHost(), options.getPort()));
                        fireStatusEvent("System " + options.getHost() + ":" + options.getPort() + " connected.");
                        writer = new OutputStreamWriter(socket.getOutputStream(), options.getEncoding());
                        reader = new InputStreamReader(socket.getInputStream(), options.getEncoding());

                        sendMessages();
                    }
                } catch(Exception ex) {
                    Logger.getLogger(getClass()).warn(ex.getMessage(), ex);
                    fireStatusEvent((ex.getMessage() != null) ? ex.getMessage() : ex.toString());
                    terminateRequest();
                }
            }

            if(socket != null) {
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
        } catch(Exception e) {
            Logger.getLogger(getClass()).error(e, e);
            fireStatusEvent((e.getMessage() != null) ? e.getMessage() : e.toString());
        }
        fireStatusEvent("Send messages server stopped.");
        fireThreadStoppedEvent();
    }

    private void sendMessages() throws IOException {
        while((messages.size() > 0) && (!terminate)) {
            Message message = messages.get(0);

            writeMessage(message);

            message = readAcknowledge();

            evaluateAcknowledge(message);

            messages.remove(0);
        }
        done = true;
        fireStatusEvent("All selected messaged send.");
    }

    private void writeMessage(Message message) throws IOException {
        fireStatusEvent("Send message...");
        StringBuilder sb = new StringBuilder();
        sb.append(options.getFrame().getStartFrame());
        sb.append(message.toString());
        sb.append(options.getFrame().getStopFrame());

        for(int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);

            fireCharSendEvent(c);
            writer.write(c);
        }
        writer.flush();
        fireStatusEvent("Message send.");
    }

    private Message readAcknowledge() throws IOException {
        fireStatusEvent("Waiting for acknowledge message...");

        MessageParserStreamReader messageReader = new MessageParserStreamReader(reader, StreamFormat.FRAMED, options.getFrame());
        messageReader.addListener(this);
        Message message = messageReader.readMessage();

        fireStatusEvent("Acknowledge message received.");

        return message;
    }

    private void evaluateAcknowledge(Message message) {
        Segment msa = message.getSegment("MSA");
        Segment err = message.getSegment("ERR");

        if(msa == null) {
            fireStatusEvent("Invalid acknowledge message structure. Segment MSA is missing.");
        } else {
            if(msa.size() < 1) {
                fireStatusEvent("Invalid segment MSA. Field MSA-1 is missing.");
            } else {
                String ac = msa.get(1).toString();

                String status;

                if(ac.equals("AA") || ac.equals("CA")) {
                    status = "Evaluate acknowledge: Application | Commit Accept";
                } else if(ac.equals("AE") || ac.equals("CE")) {
                    status = "Evaluate acknowledge: Application | Commit Error !!!";
                } else if(ac.equals("AR") || ac.equals("CR")) {
                    status = "Evaluate acknowledge: Application | Commit Reject !!!";
                } else {
                    status = "Evaluate acknowledge: Unkown or missing acknowledge code in field MSA-1 !!!";
                }

                fireStatusEvent(status);
            }
        }
    }

    public void addListener(IOThreadListener value) {
        listener.add(value);
    }

    public void removeListener(IOThreadListener value) {
        listener.remove(value);
    }

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
