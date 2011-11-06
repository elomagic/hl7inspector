/*
 * Copyright 2011 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carstenrambow
 */
public class ProfileIOTest {

    public ProfileIOTest() {
    }

    private InputStream getProfileStream() {
        return getClass().getResourceAsStream("/Sample Profile.hip");
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

//    @Test
//    public void testGetDefault() {
//        System.out.println("getDefault");
//        Profile expResult = null;
//        Profile result = ProfileIO.getDefault();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSetDefault() {
//        System.out.println("setDefault");
//        Profile profile = null;
//        ProfileIO.setDefault(profile);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    @Test
    public void testLoad() throws Exception {
        System.out.println("load");
        Profile result = ProfileIO.load(getProfileStream());
        assertEquals("Sample Profile", result.getName());
        assertEquals("Sample profile based on HL7 Version 2.5", result.getDescription());
        assertEquals("2.1.0.497", result.getSchemaVersion());

        assertEquals(25, result.getDataElementList().size());
        assertEquals(3, result.getSegmentList().size());
        assertEquals(36, result.getDataTypeList().size());
        assertEquals(129, result.getTableDataList().size());
    }

    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        
        DataElement de = new DataElement("1", "CE", "DESC", 1, "1234");
        de.setSegment("PID");                
        de.setSequence(3);

        // Prepate test
        Profile profile = new Profile();
        profile.setName("NAME");
        profile.setSchemaVersion("1234");
        profile.setDescription("DESC");
        profile.getDataElementList().add(de);
        profile.getSegmentList().add(new SegmentItem("MSH", "DESC-SEG", "1"));

        File file = File.createTempFile("HL7I_TEST_", ".xml");
        file.deleteOnExit();

        // Do test
        FileOutputStream fout = new FileOutputStream(file, false);
        try {
            ProfileIO.save(fout, profile);

            fout.flush();
        } finally {
            fout.close();
        }

        // Check results
        Profile p;
        FileInputStream fis = new FileInputStream(file);
        try {
            p = ProfileIO.load(fis);
        } finally {
            fis.close();
        }

        assertEquals("NAME", p.getName());
        assertEquals("1234", p.getSchemaVersion());
        assertEquals("DESC", p.getDescription());
        assertEquals("DESC-SEG", p.getSegment("MSH").getDescription());
        assertEquals("DESC", p.getDataElement("PID", 3).getName());
    }

}
