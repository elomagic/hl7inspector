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

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author rambow
 */
public class DataTypeItemList extends Hashtable<String, DataTypeItem> {
  
  /** Creates a new instance of DataTypeList */
  public DataTypeItemList() { }
  
  public DataTypeItem getDataType(String dataType, int index) { return get(dataType + '-' + index); }
  
  public void addDataType(DataTypeItem value) { put(value.getParentDataType() + '-' + value.getIndex(), value); }
  
    public boolean containsDataType(String dataType) {
        boolean result = false;
        
        Enumeration enu = elements();
        while ((enu.hasMoreElements()) && (!result)) {
            DataTypeItem dt = (DataTypeItem)enu.nextElement();
        
            result = dt.getParentDataType().equals(dataType);
        }
        
        return result;
    }  
}
