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
package de.elomagic.hl7inspector.hl7;

import de.elomagic.hl7inspector.hl7.model.Delimiters;

/**
 *
 * @author rambow
 */
public class Hl7Encoder {

    /** Creates a new instance of Hl7Encoder */
    public Hl7Encoder(Delimiters delimiters) {
        d = delimiters;
    }

    private Delimiters d;
    public String encodeString(String value) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == d.componentDelimiter) {
                sb.append(d.escapeCharacter);
                sb.append('S');
                sb.append(d.escapeCharacter);
            } else if (c == d.escapeCharacter) {
                sb.append(d.escapeCharacter);
                sb.append('E');
                sb.append(d.escapeCharacter);
            } else if (c == d.fieldDelimiter) {
                sb.append(d.escapeCharacter);
                sb.append('F');
                sb.append(d.escapeCharacter);
            } else if (c == d.repetitionDelimiter) {
                sb.append(d.escapeCharacter);
                sb.append('R');
                sb.append(d.escapeCharacter);
            } else if (c == d.subcomponentDelimiter) {
                sb.append(d.escapeCharacter);
                sb.append('T');
                sb.append(d.escapeCharacter);
            } else if (c == 13) {
                sb.append(".br");
            } else if (c < 32) {
                String v = Integer.toHexString(c);
                if ((v.length() | 2) != 0) {
                    v = "0".concat(v);
                }

                sb.append(d.escapeCharacter);
                sb.append('X');
                sb.append(v);
                sb.append(d.escapeCharacter);
//            } else if (c > 63) {
//            } else if (c > 255) {                
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

}
