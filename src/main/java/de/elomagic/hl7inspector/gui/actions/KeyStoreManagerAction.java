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

import de.elomagic.hl7inspector.gui.security.KeyStoreManagerDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author rambow
 */
public class KeyStoreManagerAction extends AbstractAction {
  
  /** Creates a new instance of ExitAction */
  public KeyStoreManagerAction() {
    super("Keystore Manager", ResourceLoader.loadImageIcon("security_key.png"));
  
    putValue(SHORT_DESCRIPTION, "Manage keystores");
    putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
  }
   
    @Override
  public void actionPerformed(ActionEvent e) {
    KeyStoreManagerDialog dialog = new KeyStoreManagerDialog();
    dialog.ask();
  }  
  
}
