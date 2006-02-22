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

import de.elomagic.hl7inspector.gui.ImportOptionBean;
import de.elomagic.hl7inspector.hl7.model.Message;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class MessageImportThread extends Thread implements IOCharListener {
    
    
    /** Creates a new instance of MessageImportThread */
    public MessageImportThread(InputStream in, ImportOptionBean readOptions) {
        fin = in;
        options = readOptions;
        
        setPriority(MIN_PRIORITY);
    }
    
    public void addListener(MessageImportListener value) { listener.add(value); }
    
    public void removeListener(MessageImportListener value) { listener.remove(value); }
    
    public boolean terminate = false;
    
    private InputStream   fin;
    private ImportOptionBean  options;
    private MessageParserStreamReader fileparser;
    
    private Vector<MessageImportListener> listener = new Vector<MessageImportListener>();
    
    public void run() {
        try {
            InputStreamReader reader = new InputStreamReader(fin, options.getEncoding());
            fileparser = new MessageParserStreamReader(reader, options.getImportMode(), options.getFrame());
            fileparser.addListener(this);
            
            Message message = null;
            
            do {
                message = fileparser.readMessage();
                
                if (message != null) {
                    try {
                        message.setSource(options.getSource());
                    } catch (Exception e) {
                        message.setSource("Unknown data stream");
                    }
                    
                    fireMessageReadEvent(message);
                }
            } while ((!terminate) && (message != null));
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            //SimpleDialog.error(e, "Reading stream error");
        }
        fireImportDoneEvent();
    }
    
    protected void fireImportDoneEvent() {
        for (int i=0; i<listener.size();i++)
            listener.get(i).importDone(new MessageImportEvent(this, null, fileparser.getBytesRead()));
    }
    
    protected void fireMessageReadEvent(Message message) {
        for (int i=0; i<listener.size();i++)
            listener.get(i).messageRead(new MessageImportEvent(this, message, fileparser.getBytesRead()));
    }
    
    protected void fireCharRead(char c) {
        for (int i=0; i<listener.size();i++)
            listener.get(i).charRead(c);
    }

    // Interface IOCharListener
    public void charReceived(Object source, char c) { fireCharRead(c); }
    public void charSend(Object source, char c) { }
}
