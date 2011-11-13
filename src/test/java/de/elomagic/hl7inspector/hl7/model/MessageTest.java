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
package de.elomagic.hl7inspector.hl7.model;

import de.elomagic.hl7inspector.TestTool;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carstenrambow
 */
public class MessageTest {

    public MessageTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testParseNonDefaultDelimters() throws Exception {
        System.out.println("parseNonDefaultDelimters");

        String messageText = TestTool.readMessageResource("/NonDefaultDelimiters.HL7");

        Message instance = new Message();
        instance.parse(messageText, new Delimiters("|^~\\`"));
        char expResult = 0x0d;
        assertEquals(expResult, instance.getSubDelimiter());
    }

    @Test
    public void testGetSubDelimiter() {
        System.out.println("getSubDelimiter");
        Message instance = new Message();
        char expResult = 0x0d;
        assertEquals(expResult, instance.getSubDelimiter());
    }

    @Test
    public void testGetChildClass() {
        System.out.println("getChildClass");
        Message instance = new Message();
        Class result = instance.getChildClass();
        assertEquals(Segment.class, result);
    }

    @Test
    public void testSetSource() {
        System.out.println("get/setSource");
        Message instance = new Message();
        instance.setSource("BLABLABLA");
        assertEquals("BLABLABLA", instance.getSource());
    }

//    @Test
//    public void testSetFile() {
//        System.out.println("setFile");
//        File f = null;
//        Message instance = new Message();
//        instance.setFile(f);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testGetParent() {
//        System.out.println("getParent");
//        Message instance = new Message();
//        TreeNode expResult = null;
//        TreeNode result = instance.getParent();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testSetParent() {
//        System.out.println("setParent");
//        TreeNode value = null;
//        Message instance = new Message();
//        instance.setParent(value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    @Test
    public void testIndexOfName() throws IOException {
        System.out.println("indexOfName");

        String messageText = TestTool.readMessageResource("/NonDefaultDelimiters.HL7");

        Message instance = new Message();
        instance.parse(messageText, new Delimiters("|^~\\`"));

        int result = instance.indexOfName("DG1");
        assertEquals(4, result);

        result = instance.indexOfName("XYZ");
        assertEquals(-1, result);
    }

    @Test
    public void testGetSegment() throws IOException {
        System.out.println("getSegment");

        String messageText = TestTool.readMessageResource("/NonDefaultDelimiters.HL7");

        Message instance = new Message();
        instance.parse(messageText, new Delimiters("|^~\\`"));

        Segment result = instance.getSegment("PV1");
        assertEquals("PV1", result.get(0).toString());
    }

}
