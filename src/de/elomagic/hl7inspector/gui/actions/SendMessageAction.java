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

package de.elomagic.hl7inspector.gui.actions;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.KeyStroke;

/**
 *
 * @author rambow
 */
public class SendMessageAction extends AbstractAction {
    
    /** Creates a new instance of ExitAction */
    public SendMessageAction(String value) {
        super(value);
        
        init();
    }
    
    public SendMessageAction() {
        super("Send messages...");
        
        init();
    }
    
    public void init() {
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("send.png"));
        putValue(SHORT_DESCRIPTION, "Send selected messages");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK ));       
    }
    
    public void actionPerformed(ActionEvent e) {
        Desktop.getInstance().setTabVisible(Desktop.getInstance().getSendWindow());
    }
}
