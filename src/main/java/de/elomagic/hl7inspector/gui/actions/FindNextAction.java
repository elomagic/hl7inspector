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
import de.elomagic.hl7inspector.gui.FindBar;
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
public class FindNextAction extends BasicAction {
    /**
     * Creates a new instance of FindNextAction.
     */
    public FindNextAction() {
        super();

        putValue(NAME, "Find next");
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("go-next.png"));
        putValue(SHORT_DESCRIPTION, "Find next occurrences of the phrase ");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        try {
            FindBar f = FindBar.getInstance();
            String phrase = f.getEscapedPhrase();
            boolean cs = f.isCaseSensitive();

            List<Hl7Object> selectedObjects = Desktop.getInstance().getSelectedObjects();

            Desktop.getInstance().findNextPhrase(phrase,
                                                 selectedObjects.isEmpty() ? null : selectedObjects.get(0),
                                                 cs);
        } catch(Exception ex) {
            SimpleDialog.error(ex.getMessage());
        }
    }
}
