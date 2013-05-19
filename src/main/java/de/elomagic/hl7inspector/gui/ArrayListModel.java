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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author rambow
 */
public abstract class ArrayListModel<E extends Object> extends AbstractTableModel {
    private static final long serialVersionUID = -649617212368934188L;
    private int lockCount = 0;
    protected List<E> table = new ArrayList<>();

    /**
     * Creates a new instance of ArrayListModel.
     */
    public ArrayListModel() {
    }

    public void clear() {
        table.clear();
        fireTableDataChanged();
    }

    public int addRow(E object) {
        table.add(object);

        int l = table.size() - 1;

        if(lockCount == 0) {
            fireTableRowsInserted(l, l);
        }

        return l;
    }

    public void deleteRow(int rowIndex) throws ArrayIndexOutOfBoundsException {
        table.remove(rowIndex);

        //if (lockCount == 0)
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public E getRow(int rowIndex) {
        return table.get(rowIndex);
    }

    public void lock() {
        lockCount++;
    }

    public void unlock() {
        lockCount--;
        if(lockCount == 0) {
            fireTableRowsInserted(table.size() - 1, table.size() - 1);
        }
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it should display. This method should be quick, as it is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return table.size();
    }
}
