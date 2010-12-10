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
import java.util.Vector;
import nanoxml.XMLElement;

/**
 *
 * @author rambow
 */
public class DataTypeItem {
    
    /** Creates a new instance of DataType */
    public DataTypeItem() { }
    public DataTypeItem(String parentDataType, int index, String desc, String dataType) {
        setParentDataType(parentDataType);
        setIndex(index);
        setDescription(desc);
        setDataType(dataType);
    }
    
    public DataTypeItem(XMLElement xml) {
        List v = xml.getChildren();
        for (int i=0;i<v.size();i++) {
            XMLElement el = (XMLElement)v.get(i);
            
            if (el.getName().equals("parent")) {
                setParentDataType(el.getContent());
            } else if (el.getName().equals("index")) {
                setIndex(Integer.parseInt(el.getContent()));
            } else if (el.getName().equals("parent")) {
                setParentDataType(el.getContent());
            } else if (el.getName().equals("datatype")) {
                setDataType(el.getContent());
            } else if (el.getName().equals("description")) {
                setDescription(el.getContent());
            } else if (el.getName().equals("length")) {
                setLength(Integer.parseInt(el.getContent()));
            } else if (el.getName().equals("opt")) {
                setOptionality(el.getContent());
            } else if (el.getName().equals("chapter")) {
                setChapter(el.getContent());
            } else if (el.getName().equals("parent-name")) {
                setParentDataTypeName(el.getContent());
            } else if (el.getName().equals("table")) {
                setTable(el.getContent());
            }
        }
    }
    
    public XMLElement getXMLElement() {
        XMLElement xml = new XMLElement();
        xml.setName("data-type");
        xml.setAttribute("id", getParentDataType());
        
        XMLElement el = new XMLElement();
        el.setName("parent");
        el.setContent(getParentDataType());
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("datatype");
        el.setContent(getDataType());
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("index");
        el.setContent(Integer.toString(getIndex()));
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("length");
        el.setContent(Integer.toString(getLength()));
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("description");
        el.setContent(getDescription());
        xml.addChild(el);
        
        el = new XMLElement();
        el.setName("opt");
        el.setContent(getOptionality());
        xml.addChild(el);        
        
        el = new XMLElement();
        el.setName("chapter");
        el.setContent(getChapter());
        xml.addChild(el);                        
        
        el = new XMLElement();
        el.setName("parent-name");
        el.setContent(getParentDataTypeName());
        xml.addChild(el);                        

        el = new XMLElement();
        el.setName("table");
        el.setContent(getTable());
        xml.addChild(el);                                                       
        
        return xml;
    }
    
    
    private int     index           = 0;
    private String  dataType        = "";
    private String  description     = "";
    private String  parentDataType  = "";
    private String  parentDataTypeName  = "";
    private int     len             = 0;
    private String  opt             = "O";
    private String  chapter         = "";
    private String  table           = "";
    
    public int getLength() { return len; }    
    public void setLength(int l) { this.len = l; }
    public void setLength(String l) { 
        try {
            this.len = Integer.parseInt(l);            
        } catch (Exception e) {
            this.len = 0;
        }
    }
    
    public int getIndex() { return index; }    
    public void setIndex(int index) { this.index = index; }
    public void setIndex(String index) { 
        try {
            this.index = Integer.parseInt(index);            
        } catch (Exception e) {
            this.index = 0;
        }
    }
    
    public String getDescription() { return description; }    
    public void setDescription(String description) { this.description = description; }
    
    public String getDataType() { return dataType; }    
    public void setDataType(String dataType) { this.dataType = dataType.trim(); }
    
    public String getParentDataType() { return parentDataType; }    
    public void setParentDataType(String dataType) { this.parentDataType = dataType.trim(); }
    
    public String getOptionality() { return opt; }    
    public void setOptionality(String value) { opt = value.trim(); }    
    
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }    

    public String getParentDataTypeName() { return parentDataTypeName; }
    public void setParentDataTypeName(String parentDataTypeName) { this.parentDataTypeName = parentDataTypeName.trim(); }
    
    public String getTable() { return table; }
    public void setTable(String table) {
        try {
            this.table = Integer.valueOf(table).toString();
        } catch (Exception e) {
            this.table = table.trim();
        }
    }   
}
