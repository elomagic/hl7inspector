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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rambow
 */
@XmlRootElement(name = "data-element")
@XmlAccessorType(XmlAccessType.NONE)
public final class DataElement {

    /** Creates a new instance of FieldId */
    public DataElement() {
    }

    //Name	Item#	Seg	Seq#	Chp	Len	DT	Rep	Qty	Table
    public DataElement(String id, String dataType, String name, int len, String table) {
        this.item = id;
        this.dataType = dataType;
        this.name = name;
        this.len = len;
        setTable(table);
    }

    private String name = "";

    @XmlElement(name = "name", required = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String item = "";

    @XmlElement(name = "item", required = false)
    public String getItem() {
        return item;
    }

    public void setItem(String id) {
        try {
            this.item = Integer.toString(Integer.parseInt(id));
        } catch (Exception e) {
            this.item = id;
        }
    }

    private String segment = "";

    @XmlElement(name = "segment", required = false)
    public String getSegment() {
        return segment;
    }

    public void setSegment(String value) {
        this.segment = value.trim();
    }

    private int seq = 0;

    @XmlElement(name = "sequence", required = false)
    public int getSequence() {
        return seq;
    }

    public void setSequence(int s) {
        this.seq = s;
    }

    private String chapter = "";

    @XmlElement(name = "chapter", required = false)
    public String getChapter() {
        return chapter;
    }

    public void setChapter(String value) {
        this.chapter = value;
    }

    private int len = 0;

    @XmlElement(name = "length", required = false)
    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    private String dataType = "";

    @XmlElement(name = "data-type", required = false)
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType.trim();
    }

    private String repeat = "N";

    @XmlElement(name = "repeatable", required = false)
    public String getRepeatable() {
        return repeat;
    }

    public void setRepeatable(String value) {
        this.repeat = value.toUpperCase();

        if (repeat.startsWith("Y")) {
            repeat = "Y";
        } else if (repeat.startsWith("N")) {
            repeat = "N";
        }
    }

    private String table = "";

    @XmlElement(name = "table")
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        try {
            this.table = Integer.toString(Integer.parseInt(table));
        } catch (Exception e) {
            this.table = table.trim();
        }
    }

    public int getRepeatableCount() {
        int c;

        try {
            c = Integer.parseInt(repeat);
        } catch (Exception e) {
            c = (repeat.startsWith("Y")) ? Integer.MAX_VALUE : 1;
        }

        return c;
    }

}
