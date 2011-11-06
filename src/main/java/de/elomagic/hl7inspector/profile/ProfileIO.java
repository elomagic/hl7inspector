/*
 * Copyright 2011 Carsten Rambow
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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXB;
import nanoxml.XMLElement;

/**
 *
 * @author carstenrambow
 */
public class ProfileIO {

    public static Profile getDefault() {
        return defaultProfile;
    }

    public static void setDefault(Profile profile) {
        defaultProfile = profile;
    }

    private static Profile defaultProfile = new Profile();

    public static Profile loadFromStream(InputStream in) throws Exception {


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

        InputStreamReader reader = new InputStreamReader(fin);
        try {
            return loadFromOld(reader);
            //return load(reader);
        } finally {
            reader.close();
        }
    }

    public static Profile load(Reader reader) throws Exception {
        return JAXB.unmarshal(reader, Profile.class);
    }

    private static Profile loadFromOld(Reader reader) throws IOException {
        Profile profile = new Profile();

        XMLElement xml = new XMLElement();
        xml.parseFromReader(reader);

        ElementTable rootElements = new ElementTable(xml.getChildren());

        // Read profile description
        if (rootElements.containsKey("description")) {
            profile.setDescription(rootElements.get("description").getContent());
        }

        if (rootElements.containsKey("name")) {
            profile.setName(rootElements.get("name").getContent());
        }

        if (rootElements.containsKey("schema-version")) {
            profile.setSchemaVersion(rootElements.get("schema-version").getContent());
        } else {
            profile.setSchemaVersion(Hl7Inspector.getVersion());
        }

        // Read data elements
        List dataElements = rootElements.get("data-elements").getChildren();
        for (int i = 0; i < dataElements.size(); i++) {
            XMLElement element = (XMLElement) dataElements.get(i);

            DataElement de = new DataElement(element);

            profile.getDataElementList().addDataElement(de);
        }

        // Read data types
        if (rootElements.get("data-types") != null) {
            List dataTypes = rootElements.get("data-types").getChildren();
            for (int i = 0; i < dataTypes.size(); i++) {
                XMLElement element = (XMLElement) dataTypes.get(i);

                DataTypeItem dt = new DataTypeItem(element);

                profile.getDataTypeList().addDataType(dt);
            }
        }

        // Read segments
        if (rootElements.get("segments") != null) {
            List segments = rootElements.get("segments").getChildren();
            for (int i = 0; i < segments.size(); i++) {
                XMLElement element = (XMLElement) segments.get(i);

                SegmentItem seg = new SegmentItem(element);

                profile.getSegmentList().addSegment(seg);
            }
        }

        // Read table data
        if (rootElements.get("table-data") != null) {
            List tableData = rootElements.get("table-data").getChildren();
            for (int i = 0; i < tableData.size(); i++) {
                XMLElement element = (XMLElement) tableData.get(i);

                TableItem ti = new TableItem(element);

                profile.getTableItemList().addTableItem(ti);
            }
        }

        // Read validation mapping
        if (rootElements.get("validate") != null) {
            profile.getValidateMapper().read(rootElements.get("validate"));
        }

        return profile;
    }

    public static void saveToStream(Profile profile, OutputStream out) throws Exception {
        XMLElement xml = new XMLElement();
        xml.setName("hl7inspector-profile");

        XMLElement xmlName = new XMLElement();
        xmlName.setName("name");
        xmlName.setContent(profile.getName());
        xml.addChild(xmlName);

        XMLElement xmlDesc = new XMLElement();
        xmlDesc.setName("description");
        xmlDesc.setContent(profile.getDescription());
        xml.addChild(xmlDesc);

        XMLElement xmlSchemaVersion = new XMLElement();
        xmlSchemaVersion.setName("schema-version");
        xmlSchemaVersion.setContent(Hl7Inspector.getVersion());
        xml.addChild(xmlSchemaVersion);

        // Data Elements
        XMLElement xmlDataElements = new XMLElement();
        xmlDataElements.setName("data-elements");
        Iterator<DataElement> it1 = profile.getDataElementList().values().iterator();
        while (it1.hasNext()) {
            DataElement de = it1.next();

            xmlDataElements.addChild(de.getXMLElement());
        }
        xml.addChild(xmlDataElements);

        // Data Types
        XMLElement xmlDataTypes = new XMLElement();
        xmlDataTypes.setName("data-types");
        Iterator<DataTypeItem> it2 = profile.getDataTypeList().values().iterator();
        while (it2.hasNext()) {
            DataTypeItem de = it2.next();

            xmlDataTypes.addChild(de.getXMLElement());
        }
        xml.addChild(xmlDataTypes);

        // Segments
        XMLElement xmlSegments = new XMLElement();
        xmlSegments.setName("segments");
        Iterator<SegmentItem> it3 = profile.getSegmentList().values().iterator();
        while (it3.hasNext()) {
            SegmentItem se = it3.next();

            xmlSegments.addChild(se.getXMLElement());
        }
        xml.addChild(xmlSegments);

        // Table Data
        XMLElement xmlTableData = new XMLElement();
        xmlTableData.setName("table-data");
        Iterator<TableItem> it4 = profile.getTableItemList().values().iterator();
        while (it4.hasNext()) {
            TableItem ti = it4.next();

            xmlTableData.addChild(ti.getXMLElement());
        }
        xml.addChild(xmlTableData);

        // Table Data
        XMLElement xmlValidateData = profile.getValidateMapper().write();
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

}
