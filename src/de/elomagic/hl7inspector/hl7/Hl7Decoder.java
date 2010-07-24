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
public class Hl7Decoder {

    /** Creates a new instance of Hl7Encoder */
    public Hl7Decoder(Delimiters delimiters) {
        d = delimiters;
    }

    public enum Status {

        OK, WARNING, ERROR
    };

    private Delimiters d;
    /** Returns a html formated decoded hl7 string without html tag frame */
    public String decodeString(String encodedValue) {
        setStatus(Status.OK, "");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < encodedValue.length(); i++) {
            char c = encodedValue.charAt(i);

            if (c == d.escapeCharacter) {
                i++;
                int li = encodedValue.indexOf(String.valueOf(d.escapeCharacter), i);

                if (li == -1) {
                    setStatus(Status.ERROR, "Invalid escape sequence in hl7 object");
                    result.append(c);
                    i--;
                } else {
                    String seq = encodedValue.substring(i, li);
                    i = li;

                    if (seq.length() != 0) {
                        char control = seq.charAt(0);
                        String value = (seq.length() == 0) ? "" : seq.substring(1);

                        switch (control) {
                            case 'H': {
                                result.append("<B>");
                                break;
                            }                                  // Start higlighting
                            case 'N': {
                                result.append("</B>");
                                break;
                            }                                 // normal text (end highlighting)
                            case 'F': {
                                result.append(d.fieldDelimiter);
                                break;
                            }                      // field separator
                            case 'S': {
                                result.append(d.componentDelimiter);
                                break;
                            }                   // component separator
                            case 'T': {
                                result.append(d.subcomponentDelimiter);
                                break;
                            }                // subcomponent separator
                            case 'R': {
                                result.append(d.repetitionDelimiter);
                                break;
                            }                  // repetition separator
                            case 'E': {
                                result.append(d.escapeCharacter);
                                break;
                            }                      // escape character
                            case 'X': {
                                result.append(((char) Integer.parseInt(value, 16)));
                                break;
                            }    // hexadecimal data
//                            case 'Z': { /* TODO "Locally escape sequence" support */; break; }
//                            case 'C': { /* TODO "Single byte character set" support */; break; }        // single byte character set
//                            case 'M': { /* TODO "Multi byte character set" support */; break; }         // multi byte character set
                            case '.': {
                                if (value.equals("br")) {
                                    result.append("<BR>");
                                } else if (value.equals("sp")) {
                                    result.append("<BR>");
                                } else if (value.equals("fi")) {
                                    if (no_wrap) {
                                        result.append("</nobr>");
                                    }
                                    no_wrap = false;
                                } else if (value.equals("nf")) {
                                    result.append("<nobr>");
                                    no_wrap = true;
                                } else if (value.startsWith("in")) {
                                    // Indent <number> of spaces, where <number> is a positive or negative integer. This command cannot appear after the first printable character of a line.
                                } else if (value.startsWith("ti")) {
                                    // Temporarily indent <number> of spaces where number is a positive or negative integer. This command cannot appear after the first printable character of a line.
                                } else if (value.startsWith("sk")) {
                                    // Skip <number> spaces to the right. Example .sk+3   OR  .sk-2
                                } else if (value.equals("ce")) {
                                    // End current output line and center the next line.
                                } else {
                                    result.append(seq);
                                    setStatus(Status.ERROR, "Unsupported escape sequence '" + seq + "' in hl7 object");
                                }
                                break;
                            }
                            default: {
                                result.append(seq);
                                setStatus(Status.ERROR, "Unsupported escape sequence '" + seq + "' in hl7 object");
                            }
                        }
                    }
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private Status status = Status.OK;

    private String statusText = "";
    private void setStatus(Status s, String text) {
        status = s;
        statusText = text;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    private boolean no_wrap = false;
}
