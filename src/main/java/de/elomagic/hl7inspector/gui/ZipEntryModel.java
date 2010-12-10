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

import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author rambow
 */
public class ZipEntryModel extends VectorTableModel {

    /** Creates a new instance of ZipEntryModel */
    public ZipEntryModel(ZipFile file) {
        super();

        Enumeration enu = file.entries();
        while (enu.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) (enu.nextElement());
            if (entry.getSize() != 0) {
                addRow(entry);
            }
        }
    }

    public ZipEntry getEntry(int rowIndex) {
        return (ZipEntry) (table.get(rowIndex));
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     * 
     * 
     * @param rowIndex	the row whose value is to be queried
     * @param columnIndex 	the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ZipEntry entry = getEntry(rowIndex);

        Object o;

        switch (columnIndex) {
            case 0: {
                int i = (entry.getName().lastIndexOf("/") != -1) ? entry.getName().lastIndexOf("/") : entry.getName().lastIndexOf("\\");
                o = (i == -1) ? entry.getName() : entry.getName().substring(i + 1);
                break;
            }
            case 1:
                o = new Long(entry.getSize());
                break;
            case 2:
                o = DateFormat.getDateTimeInstance().format(new Date(entry.getTime()));
                break;
            case 3: {
                int i = (entry.getName().lastIndexOf("/") != -1) ? entry.getName().lastIndexOf("/") : entry.getName().lastIndexOf("\\");
                o = (i == -1) ? "" : entry.getName().substring(0, i);
                break;
            }
            default:
                o = "";
        }

        return o;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     * 
     * 
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return 4;
    }

    /**
     *  Returns a default name for the column using spreadsheet conventions:
     *  A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     *  returns an empty string.
     * 
     * 
     * @param column  the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column) {
        String o;

        switch (column) {
            case 0:
                o = "Name";
                break;
            case 1:
                o = "Size (bytes)";
                break;
            case 2:
                o = "Date/Time";
                break;
            case 3:
                o = "Folder";
                break;
            default:
                o = "";
        }

        return o;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class c;

        switch (columnIndex) {
            case 0:
                c = String.class;
                break;
            case 1:
                c = Long.class;
                break;
            case 2:
                c = String.class;
                break;
            case 3:
                c = String.class;
                break;
            default:
                c = null;
        }

        return c;
    }

}
