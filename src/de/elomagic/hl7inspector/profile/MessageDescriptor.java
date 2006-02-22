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

import de.elomagic.hl7inspector.hl7.model.Component;
import de.elomagic.hl7inspector.hl7.model.Field;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.hl7.model.RepetitionField;
import de.elomagic.hl7inspector.hl7.model.Segment;
import de.elomagic.hl7inspector.hl7.model.Subcomponent;

/**
 *
 * @author rambow
 */
public class MessageDescriptor {
    
    /** Creates a new instance of MessageDescriptor */
    public MessageDescriptor(Profile profile) {
        p = profile;
    }
    
    private Message msg;
    private Profile p;
    
    public SegmentItem getSegmentType(Hl7Object o) {        
        SegmentItem seg = null;

        Segment s = (Segment)getObjectOfType(o, Segment.class);        
        if (s != null) {
            seg = p.getSegmentList().getSegment(((Segment)s).get(0).toString());
        }
        
        return seg;
    }
    
    public DataElement getDataElement(Hl7Object o) {
        DataElement de = null;
        
        Hl7Object field = (Field)getObjectOfType(o, Field.class);        
        if (field == null) {
            field = getObjectOfType(o, RepetitionField.class);                    
        }
        
        if (field != null) {
            int index = field.getIndex();
            
            if (field.getParent() instanceof RepetitionField) {
                index = field.getParent().getIndex();                
            }
            
            SegmentItem seg = getSegmentType(o);
            
            if (seg != null) {
                de = p.getDataElementList().getDataElement(seg.getId(), index);
            }
        }        
        
        return de;
    }
    
    public DataTypeItem getDataType(Hl7Object o) {
        DataTypeItem dt = null;
        
        Subcomponent s = (Subcomponent)getObjectOfType(o, Subcomponent.class);
        if (s != null) {
            DataTypeItem pdt = getDataType(s.getParent());
            if (pdt != null) {
                dt = p.getDataTypeList().getDataType(pdt.getDataType(), s.getIndex()+1);
            }
        } else {
            Component c = (Component)getObjectOfType(o, Component.class);
            if (c != null) {
                DataElement de = getDataElement(o);
                if (de != null) {           
                    dt = p.getDataTypeList().getDataType(de.getDataType(), c.getIndex()+1);
                }
            }
        }
        
        return dt;
    }
    
    public final static Hl7Object getObjectOfType(Hl7Object child, Class hl7ObjectClass) {
        Hl7Object result = null;
        
        while ((child.getParent() != null) && (result == null)) {
            if (child.getClass().equals(hl7ObjectClass)) {
                result = child;
            }            
           
            child = child.getParent();
        }
        
        return result;
        
        
    }
    
    
}
