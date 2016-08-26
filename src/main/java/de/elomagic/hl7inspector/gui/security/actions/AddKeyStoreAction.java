/*
 * Copyright 2016 Carsten Rambow
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
package de.elomagic.hl7inspector.gui.security.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JList;

import de.elomagic.hl7inspector.file.filters.KeyStoreFileFilter;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.VectorListModel;
import de.elomagic.hl7inspector.images.ResourceLoader;

/**
 *
 * @author Carsten Rambow
 */
public class AddKeyStoreAction extends AbstractAction {

    private final JList list;

    /**
     * Creates a new instance of FileOpenAction.
     *
     * @param list
     */
    public AddKeyStoreAction(final JList list) {
        super("Add", ResourceLoader.loadImageIcon("edit_add.png"));

        this.list = list;

        putValue(SHORT_DESCRIPTION, "Add keystore");
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        //StartupProperties prop = StartupProperties.getInstance();
        File path = new File(System.getProperty("user.dir"));

        JFileChooser fc = new JFileChooser(path);
        fc.addChoosableFileFilter(new KeyStoreFileFilter());

        fc.setDialogTitle("Choose keystore");
        if(fc.showOpenDialog(Desktop.getInstance().getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            fc.setVisible(false);

            File file = fc.getSelectedFile();
            try {
                VectorListModel model = ((VectorListModel)list.getModel());
                if(model.indexOf(file) == -1) {
                    model.add(file);
                }
            } catch(Exception ee) {
                Notification.error(ee);
            }
        }
    }
}
