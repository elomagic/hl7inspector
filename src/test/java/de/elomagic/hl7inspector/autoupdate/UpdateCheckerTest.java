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
package de.elomagic.hl7inspector.autoupdate;

import de.elomagic.hl7inspector.TestTool;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carstenrambow
 */
public class UpdateCheckerTest {

    public UpdateCheckerTest() {
    }

    @Test
    public void testCheckUpdates() throws IOException {
        System.out.println("checkUpdates");
        UpdateChecker instance = new UpdateChecker();
        String s = TestTool.readResource("/VersionText.xml");
        assertFalse(instance.checkUpdates(s, "3.0.0.0"));
        assertTrue(instance.checkUpdates(s, "2.0.0.0"));
    }

}
