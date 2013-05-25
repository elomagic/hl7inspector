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
import de.elomagic.hl7inspector.gui.DesktopIntf;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.images.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 *
 * @author rambow
 */
public class AddMessageItemAction extends AbstractAction {
    /**
     * Creates a new instance of AddMessageItemAction.
     */
    public AddMessageItemAction(Class c) {
        super("Append empty " + getObjectDescription(c));

        init(c);
    }

    public AddMessageItemAction() {
        super("Append empty item");

        init(c);
    }

    private void init(Class cl) {
        c = cl;

        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("edit_add.png"));
        //putValue(SHORT_DESCRIPTION, "Append empty " + getObjectDescription(c) + ".");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
    }

    private static String getObjectDescription(Class c) {
        String s = c.getName();
        s = s.substring(s.lastIndexOf(".") + 1);
        return s.toLowerCase();
    }
    private Class c;

    @Override
    public void actionPerformed(final ActionEvent event) {
        DesktopIntf d = Desktop.getInstance();

        List<Hl7Object> selectedObjects = d.getSelectedObjects();
        d.appendHl7Object(selectedObjects.get(0));
    }
}