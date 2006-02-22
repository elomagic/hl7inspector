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

import java.util.Vector;
import nanoxml.XMLElement;

/**
 *
 * @author rambow
 */
public class DataElement {
    
    /** Creates a new instance of FieldId */
    public DataElement() { }
    
    public DataElement(XMLElement xml) {
        Vector v = xml.getChildren();
        for (int i=0;i<v.size();i++) {
            XMLElement el = (XMLElement)v.get(i);
            if (el.getName().equals("item"))
                setItem(el.getContent());
            else if (el.getName().equals("name"))
                setName(el.getContent());
            else if (el.getName().equals("segment"))
                setSegment(el.getContent());
            else if (el.getName().equals("sequence"))
                setSequence(Integer.parseInt(el.getContent()));
            else if (el.getName().equals("chapter"))
                setChapter(el.getContent());
            else if (el.getName().equals("length"))
                setLen(Integer.parseInt(el.getContent()));
            else if (el.getName().equals("data-type"))
                setDataType(el.getContent());
            else if (el.getName().equals("repeatable"))
                setRepeatable(el.getContent());
            else if (el.getName().equals("table")) {
                setTable(el.getContent());
            }
        }
    }
    
    //Name	Item#	Seg	Seq#	Chp	Len	DT	Rep	Qty	Table
    
    
    public DataElement(String _id, String _dataType, String _desc, int _len, String _table) {
        setItem(_id);
        setDataType(_dataType);
        setName(_desc);
        setLen(_len);
        setTable(_table);
    }
    
    public XMLElement getXMLElement() {
        XMLElement xml = new XMLElement();
        xml.setName(getItem());
        
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
    
    private String  name            = "";
    private String  item            = "";
    private String  seg             = "";
    private int     seq             = 0;
    private String  chapter         = "";
    private int     len             = 0;
    private String  dataType        = "";
    private String  repeat          = "N";
//    private String  qty             = "";
    private String  table           = "";
    
    public String getName() { return name; }    
    public void setName(String name) { this.name = name; }
    
    public String getItem() { return item; }    
    public void setItem(String id) { 
        try {
            this.item = Integer.toString(Integer.parseInt(id));
        } catch (Exception e) {
            this.item = id;             
        }
    }
    
    public String getSegment() { return seg; }
    public void setSegment(String value) { this.seg = value.trim(); }
    
    public int getSequence() { return seq; }
    public void setSequence(int s) { this.seq = s; }

    public String getChapter() { return chapter; }
    public void setChapter(String value) { this.chapter = value; }

    public int getLen() { return len; }    
    public void setLen(int len) { this.len = len; }

    public String getDataType() { return dataType; }    
    public void setDataType(String dataType) { this.dataType = dataType.trim(); }
    
    public String getRepeatable() { return repeat; }
    public int getRepeatableCount() { 
        int c;
        
        try {
            c = Integer.parseInt(repeat);       
        } catch (Exception e) {
            c = (repeat.startsWith("Y"))?Integer.MAX_VALUE:1;
        }
        
        return c; 
    }
    
    public void setRepeatable(String value) { 
        this.repeat = value.toUpperCase(); 
        
        if (repeat.startsWith("Y")) {
            repeat = "Y";
        }

        if (repeat.startsWith("N")) {
            repeat = "N";
        }
    }
    
    //public String getQuantity
    
    public String getTable() { return table; }    
    public void setTable(String table) { 
        try {
            this.table = Integer.toString(Integer.parseInt(table));
        } catch (Exception e) {
            this.table = table.trim();             
        }
    }
}
