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
import de.elomagic.hl7inspector.gui.profiles.ProfileDefinitionDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.profile.ProfileIO;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JList;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class NewProfileAction extends AbstractAction {

    private static final long serialVersionUID = 4409576078805566452L;

    /** Creates a new instance of FileOpenAction */
    public NewProfileAction(JList _list) {
        super("New", ResourceLoader.loadImageIcon("document-new.png"));

        list = _list;

        putValue(SHORT_DESCRIPTION, "Create and edit new profile");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            File path = new File(System.getProperty("user.dir"));

            JFileChooser fc = new JFileChooser(path);
            fc.addChoosableFileFilter(new ProfileFileFilter());

            fc.setDialogTitle("Create new profile");
            try {
                if (fc.showSaveDialog(Desktop.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    ProfileFile file = new ProfileFile(fc.getSelectedFile().getPath());

                    if (file.exists()) {
                        if (SimpleDialog.confirmYesNo("File already exists. Overwrite?") == 0) {
                            file.delete();
                        }
                    }

                    FileOutputStream fout = new FileOutputStream(file);
                    try {
                        ProfileIO.save(fout, new Profile());
                    } finally {
                        fout.close();
                    }

                    VectorListModel<ProfileFile> model = ((VectorListModel<ProfileFile>) list.getModel());
                    if (model.indexOf(file) == -1) {
                        model.add(file);
                    }

                    ProfileDefinitionDialog dialog = new ProfileDefinitionDialog(file);
                    dialog.ask();
                }
            } finally {
                fc.setVisible(false);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            SimpleDialog.error(ex);
        }
    }

    private JList list;
}
