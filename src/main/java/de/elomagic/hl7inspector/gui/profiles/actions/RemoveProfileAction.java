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
package de.elomagic.hl7inspector.gui.profiles.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;

import javafx.scene.control.ButtonType;

import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.VectorListModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.ProfileFile;

/**
 *
 * @author Carsten Rambow
 */
public class RemoveProfileAction extends AbstractAction {

    private final JList list;

    /**
     * Creates a new instance of FileOpenAction.
     *
     * @param list
     */
    public RemoveProfileAction(final JList list) {
        super("Remove", ResourceLoader.loadImageIcon("edit_remove.png"));

        this.list = list;

        putValue(SHORT_DESCRIPTION, "Remove selected profile");
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if(list.getSelectedValue() != null) {
            if(Notification.confirmOkCancel("Are you sure?").get() == ButtonType.OK) {
                ((VectorListModel)list.getModel()).remove((ProfileFile)list.getSelectedValue());
            }
        } else {
            Notification.error("No profile selected!");
        }
    }
}
