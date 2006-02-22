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

import de.elomagic.hl7inspector.file.filters.ProfileFileFilter;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.VectorListModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JList;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class AddProfileAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public AddProfileAction(JList _list) {
        super("Add", ResourceLoader.loadImageIcon("edit_add.png"));
        
        list = _list;
        
        putValue(SHORT_DESCRIPTION, "Add profile");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    
    public void actionPerformed(ActionEvent e) {
        //StartupProperties prop = StartupProperties.getInstance();
        File path = new File(System.getProperty("user.dir"));
        
        JFileChooser fc = new JFileChooser(path);
        fc.addChoosableFileFilter(new ProfileFileFilter());
        
        fc.setDialogTitle("Choose hl7 profile");
        if (fc.showOpenDialog(Desktop.getInstance()) == JFileChooser.APPROVE_OPTION) {
            fc.setVisible(false);
            
            ProfileFile file = new ProfileFile(fc.getSelectedFile());
            
            try {
                FileInputStream fin = new FileInputStream(file);
                try {
                    Profile p = new Profile();
                    p.loadFromStream(fin);
                    file.setDescription(p.getDescription());
                    
		    VectorListModel model = ((VectorListModel)list.getModel());
		    if (model.indexOf(file) == -1) {
			model.add(file);
                    }
                } finally {
                    fin.close();
                }
            } catch (Exception ee) {
                Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                SimpleDialog.error("Invalid file format!");
            }
        }
    }
    
    private JList list;
}
