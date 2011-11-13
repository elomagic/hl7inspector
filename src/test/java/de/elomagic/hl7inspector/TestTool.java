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
package de.elomagic.hl7inspector;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author carstenrambow
 */
public class TestTool {

    public static String readResource(String resourceName) throws IOException {
        InputStream in = TestTool.class.getResourceAsStream(resourceName);
        return IOUtils.toString(in);
    }

    public static String readMessageResource(String resourceName) throws IOException {
        String s = readResource(resourceName);

        s = s.replace("\r\n", "\r");
        s = s.replace("\n", "\r");

        return s;
    }

}
