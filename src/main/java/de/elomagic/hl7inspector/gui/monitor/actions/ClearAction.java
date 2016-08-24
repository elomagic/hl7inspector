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
package de.elomagic.hl7inspector.gui.monitor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import javafx.scene.control.ButtonType;

import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.images.ResourceLoader;

/**
 *
 * @author Carsten Rambow
 */
public class ClearAction extends AbstractAction {

    private final CharacterMonitor dlg;

    /**
     * Creates a new instance of FileNewAction.
     *
     * @param d
     */
    public ClearAction(final CharacterMonitor d) {
        super("", ResourceLoader.loadImageIcon("edit-clear.png"));

        dlg = d;

        putValue(SHORT_DESCRIPTION, "Clear trace log");
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if(Notification.confirmOkCancel("Clear trace log?").get() == ButtonType.OK) {
            dlg.clear();
        }
    }
}
