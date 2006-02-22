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

//import de.elomagic.hl7inspector.autoupdate.UpdateChecker;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.UpdateCheckDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author rambow
 */
public class CheckUpdateAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public CheckUpdateAction() {
        super("Check update...", ResourceLoader.loadImageIcon("system-software-update.png"));//icon);
        
        putValue(SHORT_DESCRIPTION, "Check for updates of hl7 inspector");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            UpdateCheckDialog.check(false);
        } catch (Exception ee) {
            SimpleDialog.error("Update check failed!\n\n" + ee.getMessage());            
        }
    }
}
