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
    private Message msg;
    private Profile p;

    /** Creates a new instance of MessageDescriptor */
    public MessageDescriptor(Profile profile) {
        p = profile;
    }

    public SegmentItem getSegmentType(Hl7Object o) {
        SegmentItem seg = null;

        Segment s = (Segment)getObjectOfType(o, Segment.class);
        if(s != null) {
            seg = s.size() == 0 ? null : p.getSegment(((Segment)s).get(0).toString());
        }

        return seg;
    }

    public DataElement getDataElement(Hl7Object o) {
        DataElement de = null;

        Hl7Object field = (Field)getObjectOfType(o, Field.class);
        if(field == null) {
            field = getObjectOfType(o, RepetitionField.class);
        }

        if(field != null) {
            int index = field.getIndex();

            if(field.getHl7Parent() instanceof RepetitionField) {
                index = field.getHl7Parent().getIndex();
            }

            SegmentItem seg = getSegmentType(o);

            if(seg != null) {
                de = p.getDataElement(seg.getId(), index);
            }
        }

        return de;
    }

    public DataTypeItem getDataType(Hl7Object o) {
        DataTypeItem dt = null;

        Subcomponent s = (Subcomponent)getObjectOfType(o, Subcomponent.class);
        if(s != null) {
            DataTypeItem pdt = getDataType(s.getHl7Parent());
            if(pdt != null) {
                dt = p.getDataType(pdt.getDataType(), s.getIndex() + 1);
            }
        } else {
            Component c = (Component)getObjectOfType(o, Component.class);
            if(c != null) {
                DataElement de = getDataElement(o);
                if(de != null) {
                    dt = p.getDataType(de.getDataType(), c.getIndex() + 1);
                }
            }
        }

        return dt;
    }

    public static Hl7Object getObjectOfType(Hl7Object child, Class hl7ObjectClass) {
        Hl7Object result = null;

        while((child.getHl7Parent() != null) && (result == null)) {
            if(child.getClass().equals(hl7ObjectClass)) {
                result = child;
            }

            child = child.getHl7Parent();
        }

        return result;


    }

    public String getSimpleDescription(Hl7Object o, boolean htmlFormated) {
        String HS = (htmlFormated) ? "<B>" : "";
        String HE = (htmlFormated) ? "</B>" : "";

        String s = "";

        if(o instanceof Segment) {
            SegmentItem seg = getSegmentType(o);
            if(seg != null) {
                s = HS.concat(seg.getDescription().concat(HE));
            }
        } else if((o instanceof Field) || (o instanceof RepetitionField)) {
            DataElement de = getDataElement(o);
            if(de != null) {
                s = HS.concat("[" + de.getDataType() + "] " + de.getName().concat(HE));
            }
        } else if((o instanceof Component) || (o instanceof Subcomponent)) {
            DataTypeItem dt = getDataType(o);
            if(dt != null) {
                s = HS.concat("[" + dt.getDataType() + "] " + dt.getDescription().concat(HE));
            }
        }

        if((htmlFormated) && (s.length() != 0)) {
            s = "<font face=\"Arial\">".concat(s).concat("</font>");
        }

        return s;
    }

    public String getDescription(Hl7Object o, boolean htmlFormated) {
        String BR = (htmlFormated) ? "<BR>" : Character.toString((char)13);
        String HS = (htmlFormated) ? "<B>" : "";
        String HE = (htmlFormated) ? "</B>" : "";

        String s = "";

        if(o instanceof Segment) {
            SegmentItem seg = getSegmentType(o);
            if(seg != null) {
                s = HS.concat(seg.getId() + " " + seg.getDescription().concat(HE).concat(BR));
                s = s + "Chapter: " + seg.getChapter();
            }
        } else if((o instanceof Field) || (o instanceof RepetitionField)) {
            DataElement de = getDataElement(o);
            if(de != null) {
                s = HS.concat(de.getSegment() + "." + de.getSequence() + " " + de.getName().concat(HE).concat(BR));
                s = s + "Chapter: " + de.getChapter().concat(BR);
                s = s + "Data Type: " + de.getDataType().concat(BR);
                s = s + "Data Element Id: " + de.getItem().concat(BR);
                s = s + "Length: " + Integer.toString(de.getLen()).concat(BR);
                s = s + "Table: " + de.getTable().concat(BR);
                s = s + "Repetition: " + de.getRepeatable().concat(BR);
            }
        } else if((o instanceof Component) || (o instanceof Subcomponent)) {
            DataTypeItem dt = getDataType(o);
            if(dt != null) {
                s = HS.concat(dt.getParentDataType() + "." + dt.getIndex() + " " + dt.getDescription().concat(HE).concat(BR));
                s = s + "Chapter: " + dt.getChapter().concat(BR);
                s = s + "Data Type: " + dt.getDataType().concat(BR);
                s = s + "Length: " + Integer.toString(dt.getLength()).concat(BR);
                s = s + "Table: " + dt.getTable().concat(BR);
                s = s + "Optionality: " + dt.getOptionality().concat(BR);
            }
        }

        if((htmlFormated) && (s.length() != 0)) {
            s = "<font face=\"Arial\">".concat(s).concat("</font>");
        }

        return s;
    }
}
