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

package de.elomagic.hl7inspector.hl7.parser;

import de.elomagic.hl7inspector.hl7.model.Delimiters;

/**
 *
 * @author rambow
 */
public class Hl7Parser {
    
    /** Creates a new instance of Hl7Parser */
    public Hl7Parser() {
    }
    
    public void parse(String text) {
        // FEATURE Dynamic encoding support (Custimize encoding)
        
/*        clear();
        
        try {
            StringBuffer subText = new StringBuffer();
            int p = 0;
            
            while (p < text.length()) {
                char c = text.charAt(p);
                
                if (!((text.startsWith("MSH|^~\\&")) && (p == 6)) && (c == Delimiters.DEFAULT_ESCAPE_CHAR)) {
                    // Wenn Escapezeichen kommt dann nächstes Zeichen nicht interpretieren
//                    subText = subText.append(c);
//                    p++;
//                    if (text.length() > p) {
//                        subText = subText.append(c);
//                    }
                    
                    // Wenn Escapezeichen kommt dann nächstes Zeichen nicht interpretieren
                    
                    do {
                        subText = subText.append(c);
                        p++;
                        
                        if (text.length() > p) {
                            c = text.charAt(p);
                        }
                    } while ((text.length() > p) && (c != Delimiters.DEFAULT_ESCAPE_CHAR));
                    if (c == Delimiters.DEFAULT_ESCAPE_CHAR) {
                        subText = subText.append(c);
                    }
                } else {
                    if (c == getSubDelimiter()) {
                        // Wenn neues Feld dann altes in Array sichern
//            add(subText, not ((subText = (COMPONENT_CHAR + REPEATION_CHAR + ESCAPE_CHAR + SUBCOMPONENT_CHAR)) and (p == 9) and (copy(text, 1, 3) = 'MSH')));
                        
//                        if (subText.eq)
                        
                        if ((c == Delimiters.DEFAULT_FIELD) && (subText.toString().equals("MSH")) && (objList.size() == 0)) {
                            add("MSH");
                            add("" + Delimiters.DEFAULT_FIELD);
                            add(new EncodingObject());
                            p+=5;
                        } else
                            add(subText.toString());
                        
                        subText = new StringBuffer();
                    } else {
                        subText = subText.append(c);
                    }
                }
                
                p++;
            }
            
            if (subText.length() != 0) {
                add(subText.toString());
            }
        } catch (Exception e) {
            System.err.println("Error parsing message!");
        }*/
    }        
}
