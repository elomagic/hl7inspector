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

import de.elomagic.hl7inspector.profile.SegmentItem;
import de.elomagic.hl7inspector.profile.SegmentList;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class SegmentModel extends ProfileModel {

    /** Creates a new instance of SegmentModel */
    public SegmentModel() {
        super();
    }

    /** Creates a new instance of SegmentModel */
    public SegmentModel(SegmentList segmentList) {
        super();

        setModel(segmentList);
    }

    public void setModel(SegmentList segmentList) {
        clear();

        Iterator<String> it = segmentList.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            SegmentItem seg = segmentList.getSegment(key);
            table.add(seg);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SegmentItem seg = (SegmentItem) table.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return seg.getId();
            case 1:
                return seg.getDescription();
            case 2:
                return seg.getChapter();
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
            SegmentItem seg = (SegmentItem) table.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    seg.setId(aValue.toString());
                    break;
                case 1:
                    seg.setDescription(aValue.toString());
                    break;
                case 2:
                    seg.setChapter(aValue.toString());
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
        return 3;
    }

    public SegmentItem getSegment(int rowIndex) {
        SegmentItem seg = new SegmentItem(
                getValueAt(rowIndex, 0).toString(),
                getValueAt(rowIndex, 1).toString(),
                getValueAt(rowIndex, 2).toString());

        return seg;
    }

    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Id";
            case 1:
                return "Description";
            case 2:
                return "Chapter";
            default:
                return "";
        }
    }

    @Override
    public Class getDefaultRowClass() {
        return SegmentItem.class;
    }

}
