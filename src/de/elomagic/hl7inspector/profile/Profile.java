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

package de.elomagic.hl7inspector.profile;

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.utils.StringVector;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import nanoxml.XMLElement;

/**
 *
 * @author rambow
 */
public class Profile {
    
    /** Creates a new instance of Profile */
    public Profile() { }
    
    public final static Profile getDefault() { return profile; }    
    private final static Profile profile = new Profile();
        
    public void loadFromStream(InputStream in) throws Exception {
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
        Vector dataElements = rootElements.get("data-elements").getChildren();        
        for (int i=0;i<dataElements.size();i++) {
            XMLElement element = (XMLElement)dataElements.get(i);
            
            DataElement de = new DataElement(element);
            
            dataElementList.addDataElement(de);
        }                     
        
        // Read data types
        if (rootElements.get("data-types") != null) {
            Vector dataTypes = rootElements.get("data-types").getChildren();        
            for (int i=0;i<dataTypes.size();i++) {
                XMLElement element = (XMLElement)dataTypes.get(i);

                DataTypeItem dt = new DataTypeItem(element);

                dataTypeList.addDataType(dt);
            }                     
        }
        
        // Read segments
        if (rootElements.get("segments") != null) {
            Vector segments = rootElements.get("segments").getChildren();        
            for (int i=0;i<segments.size();i++) {
                XMLElement element = (XMLElement)segments.get(i);

                SegmentItem seg = new SegmentItem(element);

                segList.addSegment(seg);
            }
        }
        
        // Read table data
        if (rootElements.get("table-data") != null) {
            Vector tableData = rootElements.get("table-data").getChildren();        
            for (int i=0;i<tableData.size();i++) {
                XMLElement element = (XMLElement)tableData.get(i);

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
        Enumeration enu = dataElementList.elements();
        while (enu.hasMoreElements()) {
            DataElement de = (DataElement)enu.nextElement();
            
            xmlDataElements.addChild(de.getXMLElement());
        }        
        xml.addChild(xmlDataElements);
        
        // Data Types
        XMLElement xmlDataTypes = new XMLElement();
        xmlDataTypes.setName("data-types");       
        enu = dataTypeList.elements();
        while (enu.hasMoreElements()) {
            DataTypeItem de = (DataTypeItem)enu.nextElement();
            
            xmlDataTypes.addChild(de.getXMLElement());
        }        
        xml.addChild(xmlDataTypes);
        
        // Segments
        XMLElement xmlSegments = new XMLElement();
        xmlSegments.setName("segments");       
        enu = segList.elements();
        while (enu.hasMoreElements()) {
            SegmentItem se = (SegmentItem)enu.nextElement();
            
            xmlSegments.addChild(se.getXMLElement());
        }        
        xml.addChild(xmlSegments);

        // Table Data
        XMLElement xmlTableData = new XMLElement();
        xmlTableData.setName("table-data");
        enu = tableItemList.elements();
        while (enu.hasMoreElements()) {
            TableItem ti = (TableItem)enu.nextElement();
            
            xmlTableData.addChild(ti.getXMLElement());
        }        
        xml.addChild(xmlTableData);
        
        // Table Data
        XMLElement xmlValidateData = validate.write();
        xml.addChild(xmlValidateData);
                
        ZipOutputStream zout = new ZipOutputStream(out);
        try {
            zout.putNextEntry(new ZipEntry("profile-data"));
            zout.setComment("HL7 Inspector Version " + Hl7Inspector.getVersion() + " Profile");
            
            OutputStreamWriter sout = new OutputStreamWriter(zout);
            try {
                BufferedWriter bout = new BufferedWriter(sout);
                try {                                        
                    String s = xml.toString();

                    bout.write(s);
                    bout.flush();                
                } finally {
                    bout.close();
                }
            } finally {
                sout.close();
            }        
        } finally {
            zout.close();
        }      
    }
    
    public String getDescription() { return description; }    
    public void setDescription(String description) { this.description = description; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }    
    
    public String getSchemaVersion() { return schemaVersion; }
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }    
    
    public SegmentList          getSegmentList() { return segList; }
    public DataElementList      getDataElementList() { return dataElementList; }
    public DataTypeItemList     getDataTypeList() { return dataTypeList; }
    public TableItemList        getTableItemList() { return tableItemList; }
    public ValidateMapper       getValidateMapper() { return validate; }
    
    public void   setDataElementList(DataElementList value) { dataElementList = value; }
    public void   setDataTypeList(DataTypeItemList value)       { dataTypeList = value; }
    public void   setSegmentList(SegmentList value)         { segList = value; }
    public void   setTableItemList(TableItemList value)     { tableItemList = value; }  
    
    private SegmentList         segList         = new SegmentList();
    private DataElementList     dataElementList = new DataElementList();
    private DataTypeItemList    dataTypeList    = new DataTypeItemList();
    private TableItemList       tableItemList   = new TableItemList();
    private ValidateMapper      validate        = new ValidateMapper();
    private String              description     = "";
    private String              name            = "";
    private String              schemaVersion   = "";    
    
    public StringVector validate() {
        StringVector result = new StringVector();
        
        // Validate data types
        Enumeration dataTypes = getDataTypeList().elements();
        while (dataTypes.hasMoreElements()) {
            DataTypeItem dt = (DataTypeItem)dataTypes.nextElement();
                                    
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
        Enumeration dataElements = getDataElementList().elements();
        while (dataElements.hasMoreElements()) {
            DataElement de = (DataElement)dataElements.nextElement();
                        
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
