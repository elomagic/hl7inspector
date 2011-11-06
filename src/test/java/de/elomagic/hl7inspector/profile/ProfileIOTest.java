/*
 * Copyright 2011 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.profile;

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
    public void testLoadFromStream() throws Exception {
        System.out.println("loadFromStream");
        Profile result = ProfileIO.loadFromStream(getProfileStream());
        assertEquals("Sample Profile", result.getName());
        assertEquals("Sample profile based on HL7 Version 2.5", result.getDescription());
        assertEquals("2.1.0.497", result.getSchemaVersion());
    }

//    @Test
//    public void testSaveToStream() throws Exception {
//        System.out.println("saveToStream");
//        Profile profile = null;
//        OutputStream out = null;
//        ProfileIO.saveToStream(profile, out);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
