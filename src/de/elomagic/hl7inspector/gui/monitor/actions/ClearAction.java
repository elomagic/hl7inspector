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

package de.elomagic.hl7inspector.gui.monitor.actions;

import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author rambow
 */
public class ClearAction extends AbstractAction {
    
    /** Creates a new instance of FileNewAction */
    public ClearAction(CharacterMonitor d) {
        super("", ResourceLoader.loadImageIcon("edit-clear.png"));//icon);
        
        dlg = d;
        
        putValue(SHORT_DESCRIPTION, "Clear trace log");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    
    public void actionPerformed(ActionEvent e) {
        if (SimpleDialog.confirmYesNo("Clear trace log?") == 0) {
            dlg.clear();
        }
    }
    
    private CharacterMonitor dlg;    
}
