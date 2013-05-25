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
import de.elomagic.hl7inspector.images.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;

/**
 *
 * @author rambow
 */
public class ViewCompressedAction extends AbstractAction {
    /**
     * Creates a new instance of ViewCompressedAction.
     */
    public ViewCompressedAction(String value) {
        super(value, ResourceLoader.loadImageIcon("compressed.png"));

        putValue(SHORT_DESCRIPTION, "Suppress empty nodes in the tree.");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }

    public ViewCompressedAction() {
        super("View compressed", ResourceLoader.loadImageIcon("compressed.png"));

        putValue(SHORT_DESCRIPTION, "Suppress empty nodes in the tree.");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        boolean c = ((AbstractButton)event.getSource()).isSelected();
        Desktop.getInstance().setCompressedView(c);
    }
}
