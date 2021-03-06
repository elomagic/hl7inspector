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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.gui.ImportOptionBean;
import de.elomagic.hl7inspector.hl7.model.Message;

/**
 *
 * @author rambow
 */
public class MessageImportThread extends Thread implements IOCharListener {
    private InputStream fin;
    private ImportOptionBean options;
    private MessageParserStreamReader fileparser;
    private List<MessageImportListener> listener = new ArrayList<>();

    /** Creates a new instance of MessageImportThread */
    public MessageImportThread(InputStream in, ImportOptionBean readOptions) {
        fin = in;
        options = readOptions;

        setPriority(MIN_PRIORITY);
    }

    public void addListener(MessageImportListener value) {
        listener.add(value);
    }

    public void removeListener(MessageImportListener value) {
        listener.remove(value);
    }
    public boolean terminate = false;

    @Override
    public void run() {
        try {
            InputStreamReader reader = new InputStreamReader(fin, options.getEncoding());
            fileparser = new MessageParserStreamReader(reader, options.getImportMode(), options.getFrame());
            fileparser.addListener(this);

            Message message = null;

            do {
                message = fileparser.readMessage();

                if(message != null) {
                    try {
                        message.setSource(options.getSource());
                    } catch(Exception e) {
                        message.setSource("Unknown data stream");
                    }

                    fireMessageReadEvent(message);
                }
            } while((!terminate) && (message != null));
        } catch(Exception ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            //SimpleDialog.error(e, "Reading stream error");
        }
        fireImportDoneEvent();
    }

    protected void fireImportDoneEvent() {
        for(MessageImportListener l : listener) {
            l.importDone(new MessageImportEvent(this, null, fileparser.getBytesRead()));
        }
    }

    protected void fireMessageReadEvent(Message message) {
        for(MessageImportListener l : listener) {
            l.messageRead(new MessageImportEvent(this, message, fileparser.getBytesRead()));
        }
    }

    protected void fireCharRead(char c) {
        for(MessageImportListener l : listener) {
            l.charRead(c);
        }
    }

    // Interface IOCharListener
    @Override
    public void charReceived(Object source, char c) {
        fireCharRead(c);
    }

    @Override
    public void charSend(Object source, char c) {
    }
}
