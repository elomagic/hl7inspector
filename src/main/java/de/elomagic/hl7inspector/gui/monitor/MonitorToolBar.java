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
package de.elomagic.hl7inspector.gui.monitor;

import de.elomagic.hl7inspector.gui.monitor.actions.ClearAction;
import de.elomagic.hl7inspector.gui.monitor.actions.FileSaveAction;

import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 *
 * @author rambow
 */
public class MonitorToolBar extends JToolBar {
    /**
     * Creates a new instance of ToolBar.
     */
    public MonitorToolBar(CharacterMonitor dlg) {
        super();

        setOrientation(JToolBar.HORIZONTAL);
        setFloatable(false);
        setRollover(true);

        add(new JButton(new ClearAction(dlg)));
        add(new JButton(new FileSaveAction(dlg)));
    }
}
