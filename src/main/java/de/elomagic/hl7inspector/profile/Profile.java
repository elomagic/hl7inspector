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

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.utils.StringVector;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import nanoxml.XMLElement;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author rambow
 */
@Root(name = "hl7inspector-profile", strict = false)
public class Profile {

    /** Creates a new instance of Profile */
    public Profile() {
    }

    public static Profile getDefault() {
        return defaultProfile;
    }

    public static void setDefault(Profile profile) {
        defaultProfile = profile;
    }

    private static Profile defaultProfile = new Profile();
    
    public static Profile loadFromStream(InputStream in) throws Exception {
        Profile p = new Profile();
        p.loadFromStreamImpl(in);

        return p;
    }

    private void loadFromStreamImpl(InputStream in) throws Exception {
        dataElementList.clear();

        InputStream fin = new BufferedInputStream(in);
        byte[] prefixBuffer = new byte[2];
        fin.mark(2);
        fin.read(prefixBuffer);
        fin.reset();

        String prefix = new String(prefixBuffer);

        if (prefix.equals("PK")) {
            ZipInputStream zin = new ZipInputStream(fin);
            zin.getNextEntry();
            fin = zin;
        }

        XMLElement xml = new XMLElement();

        InputStreamReader sin = new InputStreamReader(fin);
        try {
            BufferedReader bin = new BufferedReader(sin);
            try {
                xml.parseFromReader(bin);
            } finally {
                bin.close();
            }
        } finally {
            sin.close();
        }

        ElementTable rootElements = new ElementTable(xml.getChildren());

        // Read profile description
        if (rootElements.containsKey("description")) {
            description = rootElements.get("description").getContent();
        }

        if (rootElements.containsKey("name")) {
            name = rootElements.get("name").getContent();
        }

        if (rootElements.containsKey("schema-version")) {
            schemaVersion = rootElements.get("schema-version").getContent();
        } else {
            schemaVersion = Hl7Inspector.getVersion();
        }

        // Read data elements
        List dataElements = rootElements.get("data-elements").getChildren();
        for (int i = 0; i < dataElements.size(); i++) {
            XMLElement element = (XMLElement) dataElements.get(i);

            DataElement de = new DataElement(element);

            dataElementList.addDataElement(de);
        }

        // Read data types
        if (rootElements.get("data-types") != null) {
            List dataTypes = rootElements.get("data-types").getChildren();
            for (int i = 0; i < dataTypes.size(); i++) {
                XMLElement element = (XMLElement) dataTypes.get(i);

                DataTypeItem dt = new DataTypeItem(element);

                dataTypeList.addDataType(dt);
            }
        }

        // Read segments
        if (rootElements.get("segments") != null) {
            List segments = rootElements.get("segments").getChildren();
            for (int i = 0; i < segments.size(); i++) {
                XMLElement element = (XMLElement) segments.get(i);

                SegmentItem seg = new SegmentItem(element);

                segmentList.addSegment(seg);
            }
        }

        // Read table data
        if (rootElements.get("table-data") != null) {
            List tableData = rootElements.get("table-data").getChildren();
            for (int i = 0; i < tableData.size(); i++) {
                XMLElement element = (XMLElement) tableData.get(i);

                TableItem ti = new TableItem(element);

                tableItemList.addTableItem(ti);
            }
        }

        // Read validation mapping
        if (rootElements.get("validate") != null) {
            validate.read(rootElements.get("validate"));
        }
    }

    public void saveToStream(OutputStream out) throws Exception {
        XMLElement xml = new XMLElement();
        xml.setName("hl7inspector-profile");

        XMLElement xmlName = new XMLElement();
        xmlName.setName("name");
        xmlName.setContent(name);
        xml.addChild(xmlName);

        XMLElement xmlDesc = new XMLElement();
        xmlDesc.setName("description");
        xmlDesc.setContent(description);
        xml.addChild(xmlDesc);

        XMLElement xmlSchemaVersion = new XMLElement();
        xmlSchemaVersion.setName("schema-version");
        xmlSchemaVersion.setContent(Hl7Inspector.getVersion());
        xml.addChild(xmlSchemaVersion);

        // Data Elements
        XMLElement xmlDataElements = new XMLElement();
        xmlDataElements.setName("data-elements");
        Iterator<DataElement> it1 = dataElementList.values().iterator();
        while (it1.hasNext()) {
            DataElement de = it1.next();

            xmlDataElements.addChild(de.getXMLElement());
        }
        xml.addChild(xmlDataElements);

        // Data Types
        XMLElement xmlDataTypes = new XMLElement();
        xmlDataTypes.setName("data-types");
        Iterator<DataTypeItem> it2 = dataTypeList.values().iterator();
        while (it2.hasNext()) {
            DataTypeItem de = it2.next();

            xmlDataTypes.addChild(de.getXMLElement());
        }
        xml.addChild(xmlDataTypes);

        // Segments
        XMLElement xmlSegments = new XMLElement();
        xmlSegments.setName("segments");
        Iterator<SegmentItem> it3 = segmentList.values().iterator();
        while (it3.hasNext()) {
            SegmentItem se = it3.next();

            xmlSegments.addChild(se.getXMLElement());
        }
        xml.addChild(xmlSegments);

        // Table Data
        XMLElement xmlTableData = new XMLElement();
        xmlTableData.setName("table-data");
        Iterator<TableItem> it4 = tableItemList.values().iterator();
        while (it4.hasNext()) {
            TableItem ti = it4.next();

            xmlTableData.addChild(ti.getXMLElement());
        }
        xml.addChild(xmlTableData);

        // Table Data
        XMLElement xmlValidateData = validate.write();
        xml.addChild(xmlValidateData);

        BufferedOutputStream bout = new BufferedOutputStream(out);
        try {

            ZipOutputStream zout = new ZipOutputStream(bout);
            try {
                zout.putNextEntry(new ZipEntry("profile-data"));
                zout.setComment("Generated by HL7 Inspector Version " + Hl7Inspector.getVersion());

                OutputStreamWriter sout = new OutputStreamWriter(zout);
                try {
                    String s = xml.toString();

                    sout.write(s);
                    sout.flush();
                } finally {
                    sout.close();
                }
//
//            zout.putNextEntry(new ZipEntry("profile-data-new.xml"));
//            zout.setComment("Generated by HL7 Inspector Version " + Hl7Inspector.getVersion());
//
//            sout = new OutputStreamWriter(zout, "utf-8");
//            try {
//                BufferedWriter bout = new BufferedWriter(sout);
//                try {
//                    Serializer serializer = new Persister();
//                    serializer.write(this, bout);
//
//                    bout.flush();
//                } finally {
//                    bout.close();
//                }
//            } finally {
//                sout.close();
//            }
            } finally {
                zout.close();
            }
        } finally {
            bout.close();
        }
    }

    @Element(name = "description", required = false)
    private String description = "";
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Element(name = "name", required = false)
    private String name = "";
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Element(name = "schema-version", required = false)
    private String schemaVersion = "";
    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    private DataElementList dataElementList = new DataElementList();
    public DataElementList getDataElementList() {
        return dataElementList;
    }

    public void setDataElementList(DataElementList value) {
        dataElementList = value;
    }

    private DataTypeItemList dataTypeList = new DataTypeItemList();
    public DataTypeItemList getDataTypeList() {
        return dataTypeList;
    }

    public void setDataTypeList(DataTypeItemList value) {
        dataTypeList = value;
    }

//    @ElementMap(name = "segments",
//    key = "ID",
//    value = "segment",
//    valueType = de.elomagic.hl7inspector.profile.SegmentItem.class,
//    attribute = true,
//    required = false)
    private SegmentList segmentList = new SegmentList();
    public SegmentList getSegmentList() {
        return segmentList;
    }

    public void setSegmentList(SegmentList value) {
        segmentList = value;
    }

    private TableItemList tableItemList = new TableItemList();
    public TableItemList getTableItemList() {
        return tableItemList;
    }

    public void setTableItemList(TableItemList value) {
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

            if ((dt.getIndex() == 0) && (dt.getParentDataType().trim().length() != 0)) {
                result.add("Error in data type definition " + dt.getParentDataType() + ": Index invalid. Must be greater then 0.");
            }
            // todo Validation of order missing

            if ((dt.getParentDataType().trim().length() == 0) && (dt.getIndex() != 0)) {
                result.add("Error in data type definition " + dt.getParentDataTypeName() + ": Parent data type not set.");
            }

            if (dt.getTable().trim().length() != 0) {
                if (!getTableItemList().containsTable(dt.getTable())) {
                    result.add("Error in data type definition " + dt.getParentDataType() + "." + dt.getIndex() + ": Table definition " + dt.getTable() + " not found.");
                }
            }

            if (dt.getDataType().trim().length() != 0) {
                if (!getDataTypeList().containsDataType(dt.getDataType())) {
                    result.add("Error in data type definition " + dt.getParentDataType() + "." + dt.getIndex() + ": Data type definition " + dt.getDataType() + " not found.");
                }
            }
        }

        // Validate data elements
        Iterator<DataElement> dataElements = getDataElementList().values().iterator();
        while (dataElements.hasNext()) {
            DataElement de = dataElements.next();

            if (de.getSegment().trim().length() == 0) {
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

            if (de.getItem().length() == 0) {
                result.add("Error in data element definition " + de.getSegment() + ": Item id not set.");
            }

            if (de.getDataType().trim().length() != 0) {
                if (!getDataTypeList().containsDataType(de.getDataType())) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Data type definition " + de.getDataType() + " not found.");
                }
            } else {
                result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Data type not set.");
            }

            if (de.getTable().trim().length() != 0) {
                if (!getTableItemList().containsTable(de.getTable())) {
                    result.add("Error in data element definition " + de.getSegment() + "." + de.getSequence() + ": Table definition " + de.getTable() + " not found.");
                }
            }

        }

        return result;
    }

}
