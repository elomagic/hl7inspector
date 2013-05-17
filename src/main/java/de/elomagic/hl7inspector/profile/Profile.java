/*
 * Copyright 2010 Carsten Rambow
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
package de.elomagic.hl7inspector.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.utils.StringVector;

/**
 *
 * @author rambow
 */
@XmlRootElement(name = "hl7inspector-profile")
@XmlAccessorType(XmlAccessType.NONE)
public class Profile {
    private String description = "";
    private String name = "";
    private String schemaVersion = Hl7Inspector.getVersion();
    private List<DataElement> dataElementList = new ArrayList<>();
    private List<SegmentItem> segmentList = new ArrayList<>();
    private List<DataTypeItem> dataTypeList = new ArrayList<>();
    private List<TableItem> tableDataList = new ArrayList<>();
    private ValidateMapper validateMapper = new ValidateMapper();
    private Map<String, DataElement> dataElementMap = new HashMap<>();
    private Map<String, DataTypeItem> dataTypeMap = new HashMap<>();
    private Map<String, SegmentItem> segmentMap = new HashMap<>();
    private Map<String, TableItem> tableDataMap = new HashMap<>();

    /** Creates a new instance of Profile */
    public Profile() {
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "schema-version")
    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    @XmlElementWrapper(name = "data-elements")
    @XmlElement(name = "data-element")
    public List<DataElement> getDataElementList() {
        return dataElementList;
    }

    public void setDataElementList(List<DataElement> dataElementList) {
        this.dataElementList = dataElementList;
    }

    @XmlElementWrapper(name = "segments")
    @XmlElement(name = "segment")
    public List<SegmentItem> getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(List<SegmentItem> segmentList) {
        this.segmentList = segmentList;
    }

    @XmlElementWrapper(name = "data-types")
    @XmlElement(name = "data-type")
    public List<DataTypeItem> getDataTypeList() {
        return dataTypeList;
    }

    public void setDataTypeList(List<DataTypeItem> dataTypeList) {
        this.dataTypeList = dataTypeList;
    }

    @XmlElementWrapper(name = "table-data")
    @XmlElement(name = "table")
    public List<TableItem> getTableDataList() {
        return tableDataList;
    }

    public void setTableDataList(List<TableItem> tableDataList) {
        this.tableDataList = tableDataList;
    }

    @XmlElement(name = "validate")
    public ValidateMapper getValidateMapper() {
        return validateMapper;
    }

    public void setValidateMapper(ValidateMapper validateMapper) {
        this.validateMapper = validateMapper;
    }

    public void reindex() {
        dataElementMap.clear();
        dataTypeMap.clear();
        segmentMap.clear();
        tableDataMap.clear();
    }

    public DataElement getDataElement(String segmentType, int indexOfField) {
        if(dataElementMap.size() != dataElementList.size()) {
            dataElementMap.clear();

            for(DataElement de : dataElementList) {
                dataElementMap.put(de.getSegment() + "-" + de.getSequence(), de);
            }
        }

        return dataElementMap.get(segmentType + '-' + indexOfField);
    }

    public DataTypeItem getDataType(String dataType, int index) {
        if(dataTypeMap.size() != dataTypeList.size()) {
            dataTypeMap.clear();

            for(DataTypeItem t : dataTypeList) {
                dataTypeMap.put(t.getParentDataType() + '-' + t.getIndex(), t);
            }
        }

        return dataTypeMap.get(dataType + '-' + index);
    }

    public SegmentItem getSegment(String segmentType) {
        if(segmentMap.size() != segmentList.size()) {
            segmentMap.clear();

            for(SegmentItem item : segmentList) {
                segmentMap.put(item.getId(), item);
            }
        }

        return segmentMap.get(segmentType);
    }

    public TableItem getTableData(String tableId, String value) {
        if(tableDataMap.size() != tableDataList.size()) {
            tableDataMap.clear();

            for(TableItem item : tableDataList) {
                tableDataMap.put(item.getId() + '-' + item.getValue(), item);
            }
        }

        return tableDataMap.get(tableId + '-' + value);
    }

    public boolean containsParentDataType(String dataType) {
        for(DataTypeItem dt : getDataTypeList()) {
            if(dt.getParentDataType().equals(dataType)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsTableData(String tableId) {
        boolean result = false;

        for(TableItem ti : tableDataList) {
            if(ti.getId().equals(tableId)) {
                return true;
            }
        }

        return false;
    }

    public StringVector validate() {
        StringVector result = new StringVector();

        // Validate data types
        for(DataTypeItem dt : dataTypeList) {
            if((dt.getIndex() == 0) && !dt.getParentDataType().trim().isEmpty()) {
                result.add("Error in data type definition " + dt.getParentDataType() + ": Index invalid. Must be greater then 0.");
            }
            // todo Validation of order missing

            if(dt.getParentDataType().trim().isEmpty() && (dt.getIndex() != 0)) {
                result.add("Error in data type definition " + dt.getParentDataTypeName() + ": Parent data type not set.");
            }

            if(!dt.getTable().trim().isEmpty()) {
                if(!containsTableData(dt.getTable())) {
                    result.add("Error in data type definition " + dt.getParentDataType() + "." + dt.getIndex() + ": Table definition " + dt.getTable() + " not found.");
                }
            }

            if(!dt.getDataType().trim().isEmpty()) {
                if(!containsParentDataType(dt.getDataType())) {
                    result.add("Error in data type definition " + dt.getParentDataType() + "." + dt.getIndex() + ": Data type definition " + dt.getDataType() + " not found.");
                }
            }
        }

        // Validate data elements
        for(DataElement de : dataElementList) {
            if(de.getSegment().trim().isEmpty()) {
                result.add("Error in data element definition " + de.getName() + ": Segment not set.");
            } else {
                if(getSegment(de.getSegment().trim()) == null) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Segment definition " + de.getSegment() + " not found.");
                }
            }

            if(de.getSequence() == 0) {
                result.add("Error in data element definition " + de.getSegment() + ": Sequence invalid. Must be greater then 0.");
            }
            // todo Validation of order missing

            if(de.getItem().isEmpty()) {
                result.add("Error in data element definition " + de.getSegment() + ": Item id not set.");
            }

            if(!de.getDataType().trim().isEmpty()) {
                if(!containsParentDataType(de.getDataType())) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Data type definition " + de.getDataType() + " not found.");
                }
            } else {
                result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Data type not set.");
            }

            if(!de.getTable().trim().isEmpty()) {
                if(!containsTableData(de.getTable())) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Table definition " + de.getTable() + " not found.");
                }
            }

        }

        return result;
    }
}
