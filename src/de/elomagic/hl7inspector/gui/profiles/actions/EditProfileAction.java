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

package de.elomagic.hl7inspector.gui.profiles.actions;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.profiles.ProfileDefinitionDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.ProfileFile;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JList;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class EditProfileAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public EditProfileAction(JList _list) {
        super("Edit", ResourceLoader.loadImageIcon("edit.png"));
        
        list = _list;
        
        putValue(SHORT_DESCRIPTION, "Edit selected profile");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            if (list.getSelectedValue() != null) {
                ProfileFile file = (ProfileFile)list.getSelectedValue();
                
                ProfileDefinitionDialog dialog = new ProfileDefinitionDialog(file);
                if (dialog.ask()) {
                    try {
                        FileOutputStream fout = new FileOutputStream(dialog.getProfileFile());
                        try {
                            dialog.getProfile().saveToStream(fout);
                            dialog.getProfileFile().setDescription(dialog.getProfile().getName());
                        } finally {
                            fout.close();
                        }                        
                        
                        StartupProperties.getInstance().setProperty(StartupProperties.DEFAULT_PROFILE, (file).toString());
                        
                        if (file.equals(new ProfileFile(StartupProperties.getInstance().getProperty(StartupProperties.DEFAULT_PROFILE, "")))) {
                            Desktop.getInstance().setProfileFile(file);
                        }
                    } catch (Exception ee) {
                        Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                        SimpleDialog.error(ee);
                    }                    
                }
            } else {
                SimpleDialog.error("No profile selected!");
            }
        } catch (Exception ee) {
            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
            SimpleDialog.error(ee);
        }
    }
    
    private JList list;
}
