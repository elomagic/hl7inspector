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
package de.elomagic.hl7inspector.gui.security.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;

import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.VectorListModel;
import de.elomagic.hl7inspector.images.ResourceLoader;

/**
 *
 * @author rambow
 */
public class RemoveKeyStoreAction extends AbstractAction {
    private JList list;

    /** Creates a new instance of FileOpenAction */
    public RemoveKeyStoreAction(JList list) {
        super("Remove", ResourceLoader.loadImageIcon("edit_remove.png"));

        this.list = list;

        putValue(SHORT_DESCRIPTION, "Remove selected keystore");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(list.getSelectedValue() != null) {
            if(SimpleDialog.confirmYesNo("Are you sure?") == 0) {
                ((VectorListModel)list.getModel()).remove(list.getSelectedValue());
            }
        } else {
            SimpleDialog.error("No keystore selected!");
        }
    }
}
