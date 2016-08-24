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
import javax.swing.JTable;

import javafx.scene.control.ButtonType;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.profiles.model.ProfileModel;
import de.elomagic.hl7inspector.gui.profiles.model.SortedTableModel;
import de.elomagic.hl7inspector.images.ResourceLoader;

/**
 *
 * @author Carsten Rambow
 */
public class DeleteAllItemsAction extends AbstractAction {

    private final JTable table;

    /**
     * Creates a new instance of FileOpenAction.
     *
     * @param table
     */
    public DeleteAllItemsAction(final JTable table) {
        super("Delete All", ResourceLoader.loadImageIcon("edit_remove.png"));

        this.table = table;

        putValue(SHORT_DESCRIPTION, "Delete all items");
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        try {
            if(Notification.confirmOkCancel("Are you sure?").get() == ButtonType.OK) {
                ((ProfileModel)((SortedTableModel)table.getModel()).getTableModel()).clear();
            }
        } catch(Exception ee) {
            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
            Notification.error(ee, ee.getMessage());
        }

    }
}
