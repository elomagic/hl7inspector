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

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author rambow
 */
public abstract class VectorTableModel extends AbstractTableModel {
    
    /** Creates a new instance of ProfileTableModel */
    public VectorTableModel() { }
    
    public void clear() {
        table.clear();
        fireTableDataChanged();
    }
    
    public int addRow(Object object) {
        table.add(object);
        
        int l = table.size()-1;        
        
        if (lockCount == 0) {
            fireTableRowsInserted(l, l);
        }
        
        return l;
    }
    
    public void deleteRow(int rowIndex) throws ArrayIndexOutOfBoundsException {
        table.remove(rowIndex);
        
        //if (lockCount == 0)
            fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public void lock() { lockCount++; }
    
    public void unlock() {
        lockCount--;
        if (lockCount == 0) {
            fireTableRowsInserted(table.size()-1, table.size()-1);
        }
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
    public int getRowCount() { return table.size(); }
    
    protected Vector<Object> table = new Vector<Object>();
    
    private int lockCount = 0;
    
}
