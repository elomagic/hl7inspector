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

import de.elomagic.hl7inspector.profile.DataElement;
import de.elomagic.hl7inspector.profile.DataElementMap;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class DataElementModel extends ProfileModel {

    public DataElementModel() {
        super();
    }

    /** Creates a new instance of DataTypeModel */
    public DataElementModel(DataElementMap dataElementList) {
        super();

        setModel(dataElementList);
    }

    public void setModel(DataElementMap dataElementList) {
        clear();

        Iterator<String> it = dataElementList.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            DataElement de = dataElementList.get(key);
            table.add(de);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DataElement de = (DataElement) table.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return de.getItem();
            case 1:
                return de.getSegment();
            case 2:
                return new Integer(de.getSequence());
            case 3:
                return de.getDataType();
            case 4:
                return new Integer(de.getLen());
            case 5:
                return de.getRepeatable();
            case 6:
                return de.getTable();
            case 7:
                return de.getName();
            case 8:
                return de.getChapter();

//    private String  chapter         = "";
//    private String  qty             = "";
            default:
                return "";
        }
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    public DataElement getDataElement(int rowIndex) {
        DataElement de = new DataElement(
                getValueAt(rowIndex, 0).toString(),
                getValueAt(rowIndex, 3).toString(),
                getValueAt(rowIndex, 7).toString(),
                Integer.parseInt(getValueAt(rowIndex, 4).toString()),
                getValueAt(rowIndex, 6).toString());

        de.setSegment(getValueAt(rowIndex, 1).toString());
        de.setSequence(Integer.parseInt(getValueAt(rowIndex, 2).toString()));
        de.setRepeatable(getValueAt(rowIndex, 5).toString());
        de.setChapter(getValueAt(rowIndex, 8).toString());

        return de;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Id";
            case 1:
                return "Seg";
            case 2:
                return "Seq";
            case 3:
                return "Data Type";
            case 4:
                return "Length";
            case 5:
                return "Rpt";
            case 6:
                return "Table";
            case 7:
                return "Description";
            case 8:
                return "Chapter";
            default:
                return "";
        }
    }

    @Override
    public Class getDefaultRowClass() {
        return DataElement.class;
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
            DataElement de = (DataElement) table.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    de.setItem(aValue.toString());
                    break;
                case 1:
                    de.setSegment(aValue.toString());
                    break;
                case 2:
                    de.setSequence(Integer.parseInt(aValue.toString()));
                    break;
                case 3:
                    de.setDataType(aValue.toString());
                    break;
                case 4:
                    de.setLen(Integer.parseInt(aValue.toString()));
                    break;
                case 5:
                    de.setRepeatable(aValue.toString());
                    break;
                case 6:
                    de.setTable(aValue.toString());
                    break;
                case 7:
                    de.setName(aValue.toString());
                    break;
                case 8:
                    de.setChapter(aValue.toString());
                default:
                    ;
            }

            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
    }

}
