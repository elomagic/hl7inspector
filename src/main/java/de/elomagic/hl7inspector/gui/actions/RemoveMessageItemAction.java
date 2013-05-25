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
 */
package de.elomagic.hl7inspector.gui.actions;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.images.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.KeyStroke;

/**
 *
 * @author rambow
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
    public void actionPerformed(ActionEvent e) {
        List<Hl7Object> selectedObjects = Desktop.getInstance().getSelectedObjects();

        if(selectedObjects.isEmpty()) {
            SimpleDialog.info("No node selected.");
        } else if(selectedObjects.size() > 1) {
            SimpleDialog.error("Only one selected node can clear.");
        } else {
            if(SimpleDialog.confirmYesNo("Clear selected node(s)?") == SimpleDialog.YES_OPTION) {
                Desktop.getInstance().removeHL7Object(selectedObjects.get(0));
            }
        }
    }
}
