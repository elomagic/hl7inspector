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
package de.elomagic.hl7inspector.gui;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

/**
 *
 * @author rambow
 */
public class ToolKit {

    /** Creates a new instance of ToolKit */
    public ToolKit() {
    }

    /**
     * Use {@link window.setLocationRelativeTo(Component)} instead
     * @param window
     * @param parentWindow
     * @return
     * @deprecated
     */
    @Deprecated
    public static Rectangle centerFrame(Window window, Window parentWindow) {
        parentWindow = Desktop.getInstance();

        Rectangle r = new Rectangle(window.getBounds().width, window.getBounds().height);

        r.x = ((parentWindow.getBounds().width - window.getBounds().width) / 2) + parentWindow.getBounds().x;
        r.y = ((parentWindow.getBounds().height - window.getBounds().height) / 2) + parentWindow.getBounds().y;

        if (r.x > Toolkit.getDefaultToolkit().getScreenSize().width) {
            r.x = Toolkit.getDefaultToolkit().getScreenSize().width - r.width;
        }

        if (r.y > Toolkit.getDefaultToolkit().getScreenSize().height) {
            r.y = Toolkit.getDefaultToolkit().getScreenSize().height - r.height;
        }

        return r;
    }

}
