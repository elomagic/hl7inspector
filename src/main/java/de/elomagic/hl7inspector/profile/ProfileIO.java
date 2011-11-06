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
import java.io.StringWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

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

    public static Profile load(InputStream in) throws Exception {
        InputStream fin = new BufferedInputStream(in);
        byte[] prefixBuffer = new byte[2];
        fin.mark(2);
        fin.read(prefixBuffer);
        fin.reset();

        String prefix = new String(prefixBuffer);

        boolean utf = false;

        if (prefix.equals("PK")) {
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry entry = zin.getNextEntry();
            utf = entry.getName().toLowerCase().endsWith(".xml");
            fin = zin;
        }

        InputStreamReader reader = utf ? new InputStreamReader(fin, "utf-8") : new InputStreamReader(fin);
        try {
            //return loadFromOld(reader);
            return JAXB.unmarshal(reader, Profile.class);
        } finally {
            reader.close();
        }
    }

    public static void save(OutputStream out, Profile p) throws IOException, JAXBException {
        BufferedOutputStream bout = new BufferedOutputStream(out);
        ZipOutputStream zout = new ZipOutputStream(bout);
        zout.putNextEntry(new ZipEntry("profile-data.xml"));
        zout.setComment("Generated by HL7 Inspector Version " + Hl7Inspector.getVersion());

        OutputStreamWriter writer = new OutputStreamWriter(zout, "utf-8");

        //JAXBContext context = JAXBContext.newInstance(Profile.class);
        //Marshaller m = context.createMarshaller();
        //m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //m.marshal(p, System.out);

        StringWriter sw = new StringWriter();
        JAXB.marshal(p, sw);
        zout.write(sw.toString().getBytes("utf-8"));

        zout.closeEntry();
        zout.flush();

        p.reindex();
    }

}
