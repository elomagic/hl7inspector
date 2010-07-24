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

import de.elomagic.hl7inspector.utils.StringVector;
import javax.swing.JLabel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author rambow
 */
public class MapFieldModel extends AbstractTableModel {

    /** Creates a new instance of ImportFileModel */
    public MapFieldModel(ImportFileModel fileModel) {
        for (int i = 0; i < fileModel.getColumnCount(); i++) {
            mapList.add("-");
        }
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param	rowIndex	the row whose value is to be queried
     * @param	columnIndex 	the column whose value is to be queried
     * @return	the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Line:";
            default:
                return mapList.get(columnIndex - 1);
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
    @Override
    public int getColumnCount() {
        return mapList.size();
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
    @Override
    public int getRowCount() {
        return 1;
    }

//  private ImportFileModel fm;
    private StringVector mapList = new StringVector("");
    /**
     *  Returns false.  This is the default implementation for all cells.
     * 
     *  @param  rowIndex  the row being queried
     *  @param  columnIndex the column being queried
     *  @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex > 0) && (rowIndex == 0);
    }

    /**
     *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     * 
     * 
     * @param columnIndex  the column being queried
     * @return the Object.class
     */
    @Override
    public Class getColumnClass(int columnIndex) {
        return (columnIndex == 0) ? JLabel.class : String.class;
    }

    /**
     *  This empty implementation is provided so users don't have to implement
     *  this method if their data model is not editable.
     * 
     *  @param  aValue   value to assign to cell
     *  @param  rowIndex   row of cell
     *  @param  columnIndex  column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        for (int i=0; i<getColumnCount(); i++) {
//            if ((i != columnIndex) && (getValueAt(rowIndex, i).equals(aValue)) && !("-".equals(aValue)))
//                setValueAt("-", rowIndex, i);
//        }        

        if ((columnIndex > 0) && (rowIndex == 0)) {
            mapList.set(columnIndex - 1, aValue.toString());

            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

}
