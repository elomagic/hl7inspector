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

/**
 *
 * @author rambow
 */
public class StringEscapeUtils {
    
    /** Creates a new instance of StringEscapeUtils */
    private StringEscapeUtils() { }
    
    public final static String escapeHtml(String str) {
        StringBuffer sb = new StringBuffer(str.length());
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
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\n')
                    // Handle Newline
                    sb.append("&lt;br/&gt;");
                else if (c == '/')
                    sb.append("&frasl;");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160 )
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
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
    
    public final static String unescapeHtml(String str) {
        StringBuffer sb = new StringBuffer(str.length());
        
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            
            if (c != '&') {
                sb.append(c);
                
            } else {
                int q = str.indexOf(';', i);
                String token = str.substring(i, q-i);
                
                if (token.equals("&gt"))
                    sb.append('>');
                else if (token.equals("&amp;"))
                    sb.append('&');
                else if (token.equals("&quot;"))
                    sb.append('"');
                else if (token.equals("&agrave"))
                    sb.append('à');
                else if (token.equals("&auml"))
                    sb.append('ä');
                else if (token.equals("&Auml"))
                    sb.append('Ä');
                else if (token.equals("&ouml"))
                    sb.append('ö');
                else if (token.equals("&Ouml"))
                    sb.append('Ö');
                else if (token.equals("&szlig"))
                    sb.append('ß');
                else if (token.equals("&uuml"))
                    sb.append('ü');
                else if (token.equals("&Uuml"))
                    sb.append('Ü');
                else if (token.equals("&copy"))
                    sb.append('©');
                else if (token.equals("&euro"))
                    sb.append('€');
                else if (token.equals("&acirc"))
                    sb.append('â');
                else if (token.equals("&Acirc"))
                    sb.append('Â');
                else if (token.equals("&aring"))
                    sb.append('å');
                else if (token.equals("&Aring"))
                    sb.append('Å');
                else if (token.equals("&Agrave"))
                    sb.append('À');
                else if (token.equals("&aelig"))
                    sb.append('æ');
                else if (token.equals("&AElig"))
                    sb.append('Æ');
                else if (token.equals("&ccedil"))
                    sb.append('ç');
                else if (token.equals("&Ccedil"))
                    sb.append('Ç');
                else if (token.equals("&eacute"))
                    sb.append('é');
                else if (token.equals("&Eacute"))
                    sb.append('É');
                else if (token.equals("&egrave"))
                    sb.append('è');
                else if (token.equals("&Egrave"))
                    sb.append('È');
                else if (token.equals("&ecirc"))
                    sb.append('ê');
                else if (token.equals("&Ecirc"))
                    sb.append('Ê');
                else if (token.equals("&euml"))
                    sb.append('ë');
                else if (token.equals("&Euml"))
                    sb.append('Ë');
                else if (token.equals("&iuml"))
                    sb.append('ï');
                else if (token.equals("&Iuml"))
                    sb.append('Ï');
                else if (token.equals("&ocirc"))
                    sb.append('ô');
                else if (token.equals("&Ocirc"))
                    sb.append('Ô');
                else if (token.equals("&oslash"))
                    sb.append('ø');
                else if (token.equals("&Oslash"))
                    sb.append('Ø');
                else if (token.equals("&ugrave"))
                    sb.append('ù');
                else if (token.equals("&Ugrave"))
                    sb.append('Ù');
                else if (token.equals("&ucirc"))
                    sb.append('û');
                else if (token.equals("&Ucirc"))
                    sb.append('Û');
                else if (token.equals("&reg"))
                    sb.append('®');
                else if (token.startsWith("&#")) {
                    int uc = Integer.parseInt(token.substring(2));
                    sb.append((char)uc);
                } else
                    sb.append('?');
            }
        }
        
        return sb.toString();
    }
}
