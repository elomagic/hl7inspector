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
package de.elomagic.hl7inspector.io;

import de.elomagic.hl7inspector.TestTool;
import de.elomagic.hl7inspector.gui.ImportOptionBean.StreamFormat;
import de.elomagic.hl7inspector.hl7.model.Message;
import java.io.IOException;
import java.io.StringReader;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carstenrambow
 */
public class MessageParserStreamReaderTest {

    public MessageParserStreamReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void before() throws Exception {

        String msg = TestTool.readMessageResource("/NonDefaultDelimiters.HL7");
        StringReader sr = new StringReader(msg);

        reader = new MessageParserStreamReader(sr, StreamFormat.TEXT_LINE, new Frame());
    }

    MessageParserStreamReader reader;

    @Test
    public void testReadMessage() throws Exception {
        System.out.println("readMessage");
        Message result = reader.readMessage();
        assertEquals(5, result.getChildCount());
    }

    @Test
    public void testGetBytesRead() throws IOException {
        System.out.println("getBytesRead");
        reader.readMessage();
        assertTrue(420 < reader.getBytesRead());
    }

//    @Test
//    public void testAddListener() {
//        System.out.println("addListener");
//        IOCharListener value = null;
//        MessageParserStreamReader instance = null;
//        instance.addListener(value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testRemoveListener() {
//        System.out.println("removeListener");
//        IOCharListener value = null;
//        MessageParserStreamReader instance = null;
//        instance.removeListener(value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFireCharReceived_String() {
//        System.out.println("fireCharReceived");
//        String s = "";
//        MessageParserStreamReader instance = null;
//        instance.fireCharReceived(s);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    @Test
//    public void testFireCharReceived_char() {
//        System.out.println("fireCharReceived");
//        char c = ' ';
//        MessageParserStreamReader instance = null;
//        instance.fireCharReceived(c);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
