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
package de.elomagic.hl7inspector.profile;

import java.util.HashMap;

/**
 *
 * @author rambow
 */
public class DataElementList extends HashMap<String, DataElement> {

    /** Creates a new instance of FieldIdList */
    public DataElementList() {
    }

    public DataElement getDataElement(String segmentType, int fieldIndex) {
        return get(segmentType + '-' + fieldIndex);
    }

    public void addDataElement(DataElement value) {
        put(value.getSegment() + '-' + value.getSequence(), value);
    }

}
