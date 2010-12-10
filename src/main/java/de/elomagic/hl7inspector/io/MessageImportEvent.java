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

/**
 *
 * @author rambow
 */
public class MessageImportEvent {

    /** Creates a new instance of MessageImportEvent */
    public MessageImportEvent(MessageImportThread _source, Message _message, long _bytesRead) {
        source = _source;
        message = _message;
        bytesRead = _bytesRead;
    }

    private Message message;
    public Message getMessage() {
        return message;
    }

    private MessageImportThread source;
    public MessageImportThread getSource() {
        return source;
    }

    private long bytesRead;
    public long getBytesRead() {
        return bytesRead;
    }

}
