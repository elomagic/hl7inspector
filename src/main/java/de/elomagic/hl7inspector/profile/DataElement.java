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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nanoxml.XMLElement;

/**
 *
 * @author rambow
 */
@XmlRootElement(name = "data-element")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataElement {

    /** Creates a new instance of FieldId */
    public DataElement() {
    }

    public DataElement(XMLElement xml) {
        List v = xml.getChildren();
        for (int i = 0; i < v.size(); i++) {
            XMLElement el = (XMLElement) v.get(i);
            if (el.getName().equals("item")) {
                setItem(el.getContent());
            } else if (el.getName().equals("name")) {
                setName(el.getContent());
            } else if (el.getName().equals("segment")) {
                setSegment(el.getContent());
            } else if (el.getName().equals("sequence")) {
                setSequence(Integer.parseInt(el.getContent()));
            } else if (el.getName().equals("chapter")) {
                setChapter(el.getContent());
            } else if (el.getName().equals("length")) {
                setLen(Integer.parseInt(el.getContent()));
            } else if (el.getName().equals("data-type")) {
                setDataType(el.getContent());
            } else if (el.getName().equals("repeatable")) {
                setRepeatable(el.getContent());
            } else if (el.getName().equals("table")) {
                setTable(el.getContent());
            }
        }
    }

    //Name	Item#	Seg	Seq#	Chp	Len	DT	Rep	Qty	Table
    public DataElement(String id, String dataType, String desc, int len, String _table) {
        this.item = id;
        this.dataType = dataType;
        this.name = desc;
        this.len = len;
        setTable(_table);
    }

    public XMLElement getXMLElement() {
        XMLElement xml = new XMLElement();
        xml.setName("data-element");
        xml.setAttribute("id", getItem());

        XMLElement el = new XMLElement();
        el.setName("item");
        el.setContent(getItem());
        xml.addChild(el);

        el = new XMLElement();
        el.setName("name");
        el.setContent(getName());
        xml.addChild(el);

        el = new XMLElement();
        el.setName("segment");
        el.setContent(getSegment());
        xml.addChild(el);

        el = new XMLElement();
        el.setName("sequence");
        el.setContent(Integer.toString(getSequence()));
        xml.addChild(el);

        el = new XMLElement();
        el.setName("chapter");
        el.setContent(getChapter());
        xml.addChild(el);

        el = new XMLElement();
        el.setName("length");
        el.setContent(Integer.toString(getLen()));
        xml.addChild(el);

        el = new XMLElement();
        el.setName("data-type");
        el.setContent(getDataType());
        xml.addChild(el);

        el = new XMLElement();
        el.setName("repeatable");
        el.setContent(getRepeatable());
        xml.addChild(el);

        el = new XMLElement();
        el.setName("table");
        el.setContent(getTable());
        xml.addChild(el);

        return xml;
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

    public int getRepeatableCount() {
        int c;

        try {
            c = Integer.parseInt(repeat);
        } catch (Exception e) {
            c = (repeat.startsWith("Y")) ? Integer.MAX_VALUE : 1;
        }

        return c;
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

}
