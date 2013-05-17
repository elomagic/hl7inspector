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
package de.elomagic.hl7inspector.hl7.model;

/**
 *
 * @author rambow
 */
public class Delimiters {
    /** Creates a new instance of Delimiters */
    public Delimiters(String value) {
        if(value.length() < 5) {
            throw new IllegalArgumentException("Delimiter string must at least 5 character long");
        }

        fieldDelimiter = value.charAt(0);
        componentDelimiter = value.charAt(1);
        repetitionDelimiter = value.charAt(2);
        escapeCharacter = value.charAt(3);
        subcomponentDelimiter = value.charAt(4);
    }

    public Delimiters() {
    }
    public final static char DEFAULT_REPETITION = '~';
    public final static char DEFAULT_FIELD = '|';
    public final static char DEFAULT_COMPONENT = '^';
    public final static char DEFAULT_ESCAPE_CHAR = '\\';
    public final static char DEFAULT_SUPCOMPONENT = '&';
    public char repetitionDelimiter = '~';
    public char fieldDelimiter = '|';
    public char componentDelimiter = '^';
    public char escapeCharacter = '\\';
    public char subcomponentDelimiter = '&';

    @Override
    public String toString() {
        return "" + fieldDelimiter + componentDelimiter + repetitionDelimiter + escapeCharacter + subcomponentDelimiter;
    }
}
