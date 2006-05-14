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

import de.elomagic.hl7inspector.gui.MessageWriterBean;
import de.elomagic.hl7inspector.hl7.model.Message;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class MessageWriterThread extends Thread {
    /** Creates a new instance of MessageImportThread */
    public MessageWriterThread(Vector<Message> messageList, MessageWriterBean mwb ) {
        setPriority(MIN_PRIORITY);
        
        messages = messageList;
        bean = mwb;
    }
    
    public void addListener(MessageWriterListener value) { listener.add(value); }
    
    public void removeListener(MessageWriterListener value) { listener.remove(value); }
    
    public boolean terminate = false;
        
    public void run() {
        try {
            int i = 0;
            
            File messageFile = null;
            if (!bean.isManyFiles()) {
                wout = new OutputStreamWriter(new FileOutputStream(bean.getSingleFileName(), false), bean.getEncoding());
            }
            
            while ((!terminate) && (i<messages.size())) {
                Message message = messages.get(i);
                try {                
                    if (bean.isManyFiles()) {
                        messageFile = createDataFile(i);
                        wout = new OutputStreamWriter(new FileOutputStream(messageFile), bean.getEncoding());
                    }                    
                    writeMessage(message);                    
                } finally {
                    wout.flush();                    
                }
                
                if (bean.isManyFiles()) {
                    wout.close();
                    
                    if (bean.isGenerateSempahore()) {
                        createSemaphoreFile(i);                    
                    }
                }

                fireMessageSavedEvent(bean.isManyFiles()?bean.getSingleFileName():messageFile, i);
                i++;                    
            }
            
            if (!bean.isManyFiles()) {
                wout.close();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            //SimpleDialog.error(e, "Reading stream error");
        }
        fireWriterDoneEvent(0);
    }
    
    protected void fireMessageSavedEvent(File file, int count) {
        for (int i=0; i<listener.size(); i++)
            listener.get(i).messageSaved(this, file, count);
    }
    
    protected void fireWriterDoneEvent(int count) {
        for (int i=0; i<listener.size(); i++)
            listener.get(i).writerDone(this, 0);
    }
    
    private Vector<MessageWriterListener> listener = new Vector<MessageWriterListener>();
    
    private MessageWriterBean           bean;
    private OutputStreamWriter          wout;
    private Vector<Message>             messages    = new Vector<Message>();
    
    private void writeMessage(Message message) throws IOException {
        String msgText = message.toString();
        
        if (!bean.isManyFiles()) {
            wout.write(bean.getFrame().getStartFrame());
        }
        
        wout.write(msgText);
        
        if (!bean.isManyFiles()) {
            wout.write(bean.getFrame().getStopFrame());
        }
    }
    
    private File createDataFile(int indexName) throws IOException {
        return createIndexFile(indexName, bean.getDataFileExtension());
    }
    
    private File createSemaphoreFile(int indexName) throws IOException {
        return createIndexFile(indexName, bean.getSemaphoreExtension());
    }    
    
    private File createIndexFile(int indexName, String extension) throws IOException {
        File file = new File(bean.getDestinationFolder().getAbsolutePath().concat("\\" + getFileWithoutExtension(indexName)).concat(".").concat(extension));
        
        if (!file.exists()) {
            file.createNewFile();
        }
        
        return file;        
    }
    
    private String getFileWithoutExtension(int indexName) {
        String result = Integer.toString(indexName);
        
        while (result.length() < 5)
            result = "0".concat(result);
        
        result = bean.getDataFilePrefix().concat(result);
        
        return result;
    }
}
