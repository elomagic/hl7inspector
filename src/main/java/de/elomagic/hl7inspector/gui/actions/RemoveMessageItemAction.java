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
 */
package de.elomagic.hl7inspector.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.KeyStroke;

import javafx.scene.control.ButtonType;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.images.ResourceLoader;

/**
 *
 * @author Carsten Rambow
 */
public class RemoveMessageItemAction extends BasicAction {

    /**
     * Creates a new instance of RemoveMessageItemAction.
     */
    public RemoveMessageItemAction() {
        super();

        putValue(NAME, "Clear selected node(s)");
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("edit-clear.png"));
        putValue(SHORT_DESCRIPTION, "Delete selected node(s)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        List<Hl7Object> selectedObjects = Desktop.getInstance().getSelectedObjects();

        if(selectedObjects.isEmpty()) {
            Notification.info("No node selected.");
        } else if(selectedObjects.size() > 1) {
            Notification.error("Only one selected node can clear.");
        } else if(Notification.confirmOkCancel("Clear selected node(s)?").get() == ButtonType.OK) {
            Desktop.getInstance().removeHL7Object(selectedObjects.get(0));
        }
    }
}
