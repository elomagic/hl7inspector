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
package de.elomagic.hl7inspector.gui.profiles.model;

import de.elomagic.hl7inspector.profile.DataTypeItem;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class DataTypeModel<E extends DataTypeItem> extends ProfileModel<DataTypeItem> {

    private static final long serialVersionUID = -9155940816412596164L;

    /** Creates a new instance of DataTypeModel */
    public DataTypeModel() {
        super();
    }

    /** Creates a new instance of DataTypeModel */
    public DataTypeModel(List<E> list) {
        super();

        setModel(list);
    }

    public final void setModel(List<E> list) {
        clear();
        table.addAll(list);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DataTypeItem de = getRow(rowIndex);

        switch (columnIndex) {
            case 0:
                return de.getParentDataType();
            case 1:
                return new Integer(de.getIndex());
            case 2:
                return de.getDataType();
            case 3:
                return new Integer(de.getLength());
            case 4:
                return de.getDescription();
            case 5:
                return de.getOptionality();
            case 6:
                return de.getChapter();
            case 7:
                return de.getParentDataTypeName();
            case 8:
                return de.getTable();
            default:
                return "";
        }
    }

    /**
     *  This empty implementation is provided so users don't have to implement
     *  this method if their data model is not editable.
     * 
     * 
     * @param aValue   value to assign to cell
     * @param rowIndex   row of cell
     * @param columnIndex  column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            DataTypeItem de = getRow(rowIndex);

            switch (columnIndex) {
                case 0:
                    de.setParentDataType(aValue.toString());
                    break;
                case 1:
                    de.setIndex(aValue.toString());
                    break;
                case 2:
                    de.setDataType(aValue.toString());
                    break;
                case 3:
                    de.setLength(aValue.toString());
                    break;
                case 4:
                    de.setDescription(aValue.toString());
                    break;
                case 5:
                    de.setOptionality(aValue.toString());
                    break;
                case 6:
                    de.setChapter(aValue.toString());
                    break;
                case 7:
                    de.setParentDataTypeName(aValue.toString());
                    break;
                case 8:
                    de.setTable(aValue.toString());
                    break;
                default:
                    ;
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Deprecated
    public DataTypeItem getDataTypeItem(int rowIndex) {
        return getRow(rowIndex);
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Parent Data Type";
            case 1:
                return "Index";
            case 2:
                return "Data Type";
            case 3:
                return "Length";
            case 4:
                return "Description";
            case 5:
                return "Opt";
            case 6:
                return "Chapter";
            case 7:
                return "Parent Data Type Name";
            case 8:
                return "Table";
            default:
                return "";
        }
    }

    @Override
    public Class getDefaultRowClass() {
        return DataTypeItem.class;
    }

}
