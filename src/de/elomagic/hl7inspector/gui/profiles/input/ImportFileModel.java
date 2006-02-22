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

import de.elomagic.hl7inspector.gui.VectorTableModel;
import de.elomagic.hl7inspector.utils.StringVector;
import java.util.Vector;

/**
 *
 * @author rambow
 */
public class ImportFileModel extends VectorTableModel {
    
    /** Creates a new instance of ImportFileModel */
    public ImportFileModel(Vector<Object> sampleLines) { table = (sampleLines==null)?new Vector<Object>():sampleLines; }
    
    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (table.size() <= rowIndex) {
            return "";
        }
        
        StringVector stack = (StringVector)table.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return (getRowCount()<=rowIndex)?"":Integer.toString(rowIndex+1);
            default: return (columnIndex<=stack.size())?stack.get(columnIndex-1):"";
        }
    }
    
    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount() {
        int r = getRowCount();
        return (r==0)?1:((StringVector)table.get(1)).size()+1;
    }
    
    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() { return (table.size()<10)?10:table.size(); }    
}
