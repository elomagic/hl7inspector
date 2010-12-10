/*
 * Copyright 2010 Carsten Rambow
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

/**
 *
 * @author rambow
 */
public class StringEscapeUtils {

    /** Creates a new instance of StringEscapeUtils */
    private StringEscapeUtils() {
    }

    public static String escapeHtml(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = str.length();
        char c;

        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"') {
                    sb.append("&quot;");
                } else if (c == '&') {
                    sb.append("&amp;");
                } else if (c == '<') {
                    sb.append("&lt;");
                } else if (c == '>') {
                    sb.append("&gt;");
                } else if (c == '\n') {
                    // Handle Newline
                    sb.append("&lt;br/&gt;");
                } else {
                    int ci = 0xffff & c;
                    if (ci < 160) // nothing special only 7 Bit
                    {
                        sb.append(c);
                    } else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String unescapeHtml(String str) {
        StringBuilder sb = new StringBuilder(str.length());

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c != '&') {
                sb.append(c);

            } else {
                int q = str.indexOf(';', i);
                String token = str.substring(i, q - i);

                if (token.equals("&gt")) {
                    sb.append('>');
                } else if (token.equals("&amp;")) {
                    sb.append('&');
                } else if (token.equals("&quot;")) {
                    sb.append('"');
                } else if (token.equals("&agrave")) {
                    sb.append('‡');
                } else if (token.equals("&auml")) {
                    sb.append('‰');
                } else if (token.equals("&Auml")) {
                    sb.append('ƒ');
                } else if (token.equals("&ouml")) {
                    sb.append('ˆ');
                } else if (token.equals("&Ouml")) {
                    sb.append('÷');
                } else if (token.equals("&szlig")) {
                    sb.append('ﬂ');
                } else if (token.equals("&uuml")) {
                    sb.append('¸');
                } else if (token.equals("&Uuml")) {
                    sb.append('‹');
                } else if (token.equals("&copy")) {
                    sb.append('©');
                } else if (token.equals("&euro")) {
                    sb.append('Ä');
                } else if (token.equals("&acirc")) {
                    sb.append('‚');
                } else if (token.equals("&Acirc")) {
                    sb.append('¬');
                } else if (token.equals("&aring")) {
                    sb.append('Â');
                } else if (token.equals("&Aring")) {
                    sb.append('≈');
                } else if (token.equals("&Agrave")) {
                    sb.append('¿');
                } else if (token.equals("&aelig")) {
                    sb.append('Ê');
                } else if (token.equals("&AElig")) {
                    sb.append('∆');
                } else if (token.equals("&ccedil")) {
                    sb.append('Á');
                } else if (token.equals("&Ccedil")) {
                    sb.append('«');
                } else if (token.equals("&eacute")) {
                    sb.append('È');
                } else if (token.equals("&Eacute")) {
                    sb.append('…');
                } else if (token.equals("&egrave")) {
                    sb.append('Ë');
                } else if (token.equals("&Egrave")) {
                    sb.append('»');
                } else if (token.equals("&ecirc")) {
                    sb.append('Í');
                } else if (token.equals("&Ecirc")) {
                    sb.append(' ');
                } else if (token.equals("&euml")) {
                    sb.append('Î');
                } else if (token.equals("&Euml")) {
                    sb.append('À');
                } else if (token.equals("&iuml")) {
                    sb.append('Ô');
                } else if (token.equals("&Iuml")) {
                    sb.append('œ');
                } else if (token.equals("&ocirc")) {
                    sb.append('Ù');
                } else if (token.equals("&Ocirc")) {
                    sb.append('‘');
                } else if (token.equals("&oslash")) {
                    sb.append('¯');
                } else if (token.equals("&Oslash")) {
                    sb.append('ÿ');
                } else if (token.equals("&ugrave")) {
                    sb.append('˘');
                } else if (token.equals("&Ugrave")) {
                    sb.append('Ÿ');
                } else if (token.equals("&ucirc")) {
                    sb.append('˚');
                } else if (token.equals("&Ucirc")) {
                    sb.append('€');
                } else if (token.equals("&reg")) {
                    sb.append('Æ');
                } else if (token.startsWith("&#")) {
                    int uc = Integer.parseInt(token.substring(2));
                    sb.append((char) uc);
                } else {
                    sb.append('?');
                }
            }
        }

        return sb.toString();
    }

}
