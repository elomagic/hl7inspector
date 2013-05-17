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
package de.elomagic.hl7inspector.gui.profiles.input;

import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author rambow
 */
public class PreviewCellRenderer extends JLabel implements TableCellRenderer {
    /**
     * Creates a new instance of ImportTableCellRenderer.
     */
    public PreviewCellRenderer() {
    }

    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable jTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(obj.toString());
        setOpaque(true);
        setBackground((column == 0) ? SystemColor.control : SystemColor.window);
        setForeground((column == 0) ? SystemColor.controlText : SystemColor.windowText);

        return this;
    }
}
