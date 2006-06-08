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
import de.elomagic.hl7inspector.gui.options.OptionsDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.AbstractAction;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author rambow
 */
public class OptionsAction extends AbstractAction {
    
    /** Creates a new instance of ExitAction */
    public OptionsAction() {
        super("Options...", ResourceLoader.loadImageIcon("preferences-desktop.png"));
        
        putValue(SHORT_DESCRIPTION, "Setup options");
    }
    
    public void actionPerformed(ActionEvent e) {
        Hashtable h = new Hashtable(StartupProperties.getInstance());
        
        OptionsDialog dlg = new OptionsDialog();
        if (dlg.ask()) {
            Enumeration en = StartupProperties.getInstance().keys();
            
            // Differ old an new setup
            while (en.hasMoreElements()) {
                String key = en.nextElement().toString();
                
                if (StartupProperties.getInstance().getProperty(key).equals(h.get(key))) {
                    h.remove(key);
                }
            }
            
            if (h.containsKey(StartupProperties.APP_LOOK_AND_FEEL)) {
                try {
                    UIManager.setLookAndFeel((LookAndFeel)StartupProperties.getInstance().getLookAndFeelClass().newInstance());
                    SwingUtilities.updateComponentTreeUI(Desktop.getInstance());
                } catch (Exception ex) {
                    SimpleDialog.error(ex);
                }
                
            }
            
            if (h.containsKey(StartupProperties.DESKTOP_IMAGE)) {
                Desktop.getInstance().getScrollPane().updateBackgroundImage();
            }
            
            if (h.containsKey(StartupProperties.TREE_FONT_NAME)) {
                Desktop.getInstance().getTree().repaint();
            }
            
            if ((h.containsKey(StartupProperties.TREE_NODE_LENGTH)) ||
                    (h.containsKey(StartupProperties.TREE_VIEW_MODE))) {                
                // TODO: Text of all tree nodes must be reset
            }
        }
    }
}
