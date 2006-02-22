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

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author rambow
 */
public class ExitAction extends AbstractAction {
  
  /** Creates a new instance of ExitAction */
  public ExitAction() {
    super("Exit", ResourceLoader.loadImageIcon("exit.png"));
  
    putValue(SHORT_DESCRIPTION, "Exit Hl7 Inspector");
    putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
  }
   
  public void actionPerformed(ActionEvent e) {
    if (SimpleDialog.confirmYesNo("Really exit the hl7 inspector?") == 0) {
      
      StartupProperties prop = StartupProperties.getInstance();
      prop.setProperty(prop.DESKTOP_X, Integer.toString(Desktop.getInstance().getBounds().x));
      prop.setProperty(prop.DESKTOP_Y, Integer.toString(Desktop.getInstance().getBounds().y));
      prop.setProperty(prop.DESKTOP_W, Integer.toString(Desktop.getInstance().getBounds().width));
      prop.setProperty(prop.DESKTOP_H, Integer.toString(Desktop.getInstance().getBounds().height));      
      prop.save();

      System.exit(0);          
    }

  }    
}
