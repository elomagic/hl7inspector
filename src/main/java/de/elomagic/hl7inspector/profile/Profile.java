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

import de.elomagic.hl7inspector.utils.StringVector;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rambow
 */
@XmlRootElement(name = "hl7inspector-profile")
@XmlAccessorType(XmlAccessType.NONE)
public class Profile {

    /** Creates a new instance of Profile */
    public Profile() {
    }

    private String description = "";

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String name = "";

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String schemaVersion = "";

    @XmlElement(name = "schema-version")
    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    private DataElementMap dataElementList = new DataElementMap();

    public DataElementMap getDataElementList() {
        return dataElementList;
    }

    public void setDataElementList(DataElementMap value) {
        dataElementList = value;
    }

    private DataTypeItemMap dataTypeList = new DataTypeItemMap();

    public DataTypeItemMap getDataTypeList() {
        return dataTypeList;
    }

    public void setDataTypeList(DataTypeItemMap value) {
        dataTypeList = value;
    }

//    @ElementMap(name = "segments",
//    key = "ID",
//    value = "segment",
//    valueType = de.elomagic.hl7inspector.profile.SegmentItem.class,
//    attribute = true,
//    required = false)
    private SegmentMap segmentList = new SegmentMap();

    public SegmentMap getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(SegmentMap value) {
        segmentList = value;
    }

    private TableItemMap tableItemList = new TableItemMap();

    public TableItemMap getTableItemList() {
        return tableItemList;
    }

    public void setTableItemList(TableItemMap value) {
        tableItemList = value;
    }

    private ValidateMapper validate = new ValidateMapper();

    public ValidateMapper getValidateMapper() {
        return validate;
    }

    public StringVector validate() {
        StringVector result = new StringVector();

        // Validate data types
        Iterator<DataTypeItem> it = getDataTypeList().values().iterator();
        while (it.hasNext()) {
            DataTypeItem dt = it.next();

            if ((dt.getIndex() == 0) && !dt.getParentDataType().trim().isEmpty()) {
                result.add("Error in data type definition " + dt.getParentDataType() + ": Index invalid. Must be greater then 0.");
            }
            // todo Validation of order missing

            if (dt.getParentDataType().trim().isEmpty() && (dt.getIndex() != 0)) {
                result.add("Error in data type definition " + dt.getParentDataTypeName() + ": Parent data type not set.");
            }

            if (!dt.getTable().trim().isEmpty()) {
                if (!getTableItemList().containsTable(dt.getTable())) {
                    result.add("Error in data type definition " + dt.getParentDataType() + "." + dt.getIndex() + ": Table definition " + dt.getTable() + " not found.");
                }
            }

            if (!dt.getDataType().trim().isEmpty()) {
                if (!getDataTypeList().containsDataType(dt.getDataType())) {
                    result.add("Error in data type definition " + dt.getParentDataType() + "." + dt.getIndex() + ": Data type definition " + dt.getDataType() + " not found.");
                }
            }
        }

        // Validate data elements
        Iterator<DataElement> dataElements = getDataElementList().values().iterator();
        while (dataElements.hasNext()) {
            DataElement de = dataElements.next();

            if (de.getSegment().trim().isEmpty()) {
                result.add("Error in data element definition " + de.getName() + ": Segment not set.");
            } else {
                if (getSegmentList().getSegment(de.getSegment().trim()) == null) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Segment definition " + de.getSegment() + " not found.");
                }
            }

            if (de.getSequence() == 0) {
                result.add("Error in data element definition " + de.getSegment() + ": Sequence invalid. Must be greater then 0.");
            }
            // todo Validation of order missing

            if (de.getItem().isEmpty()) {
                result.add("Error in data element definition " + de.getSegment() + ": Item id not set.");
            }

            if (!de.getDataType().trim().isEmpty()) {
                if (!getDataTypeList().containsDataType(de.getDataType())) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Data type definition " + de.getDataType() + " not found.");
                }
            } else {
                result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Data type not set.");
            }

            if (!de.getTable().trim().isEmpty()) {
                if (!getTableItemList().containsTable(de.getTable())) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Table definition " + de.getTable() + " not found.");
                }
            }

        }

        return result;
    }

}
