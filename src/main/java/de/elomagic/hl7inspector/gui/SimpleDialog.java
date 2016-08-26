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
package de.elomagic.hl7inspector.gui;

import java.awt.KeyboardFocusManager;
import java.awt.Window;

import javax.swing.JOptionPane;

/**
 *
 * @author Carsten Rambow
 */
public final class SimpleDialog {

    public static int YES_OPTION = JOptionPane.YES_OPTION;
    public static int NO_OPTION = JOptionPane.NO_OPTION;

    /**
     * Creates a new instance of SimpleDialog.
     */
    private SimpleDialog() {
    }

    public static String ask(String text, String def) {
        return JOptionPane.showInputDialog(getParent(), text, def);
    }

    private static Window getParent() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
    }

}
