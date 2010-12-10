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
package de.elomagic.hl7inspector.utils;

import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author rambow
 */
public class StringVector extends ArrayList<String> {

    /** Creates a new instance of StringVector */
    public StringVector() {
    }

    public StringVector(Enumeration enu) {
        while (enu.hasMoreElements()) {
            add(enu.nextElement().toString());
        }
    }

    public StringVector(String text) {
        parse(text);
    }

    public StringVector(String text, char seperatorChar) {
        sep = seperatorChar;
        parse(text);
    }

    public StringVector(String text, char seperatorChar, char stringEncodingChar) {
        sep = seperatorChar;
        enc = stringEncodingChar;
        parse(text);
    }

    public final void parse(String text) {
        clear();

        String item = "";

        boolean ignore = false;

        int i = 0;
        while (i < text.length()) {
            char c = text.charAt(i);

            if (c == sep && !ignore) {
                add(item);
                item = "";
            } else {
                if (c == '"') {
                    ignore = !ignore;
                } else {
                    item = item + c;
                }
            }
            i++;
        }

        if (item.length() != 0) {
            add(item);
        }
    }

    @Override
    public String toString() {
        return toString(sep);
    }

    public String toString(char seperatorChar) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size(); i++) {
            sb.append(get(i));
            sb.append(seperatorChar);
        }


        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString().trim();
    }

    private char sep = ',';

    private char enc = '"';
}
