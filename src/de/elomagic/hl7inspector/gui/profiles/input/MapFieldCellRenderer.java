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

import javax.swing.JComboBox;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author rambow
 */
public class MapFieldCellRenderer extends JComboBox implements TableCellRenderer {
    
    /** Creates a new instance of ImportTableCellRenderer */
    public MapFieldCellRenderer() { }
    
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable jTable, Object obj, boolean isSelected, boolean hasFocus, int row, int column) {
        getModel().setSelectedItem(obj.toString());
        
        return this;
    }    
}
