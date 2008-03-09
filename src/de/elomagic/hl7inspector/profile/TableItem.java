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
import org.simpleframework.xml.Element;

/**
 *
 * @author rambow
 */
public class TableItem {
    
    /** Creates a new instance of TableItem */
    public TableItem() { }
    
    public TableItem(XMLElement xml) {
        Vector v = xml.getChildren();
        for (int i=0;i<v.size();i++) {
            XMLElement el = (XMLElement)v.get(i);
            
            if (el.getName().equals("id")) {
                setId(el.getContent());
            } else if (el.getName().equals("type")) {
                setType(el.getContent());
            } else if (el.getName().equals("table-description")) {
                setTableDescription(el.getContent());
            } else if (el.getName().equals("value")) {
                setValue(el.getContent());
            } else if (el.getName().equals("description")) {
                setDescription(el.getContent());
            }
        }
    }
    
    public XMLElement getXMLElement() {
        XMLElement xml = new XMLElement();
        xml.setName("table");
        xml.setAttribute("id", getId());
        
        XMLElement el = new XMLElement();
        el.setName("id");
        el.setContent(getId());
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("type");
        el.setContent(getType());
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("table-description");
        el.setContent(getTableDescription());
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("value");
        el.setContent(getValue());
        xml.addChild(el);        

        el = new XMLElement();
        el.setName("description");
        el.setContent(getDescription());
        xml.addChild(el);        
        
        return xml;
    }    
    
    @Element(name="type", required=false)
    private String type = "HL7";
    public String getType() { return type; }    
    public void setType(String type) { this.type = type; }
    
    @Element(name="id", required=false)
    private String id = "";
    public String getId() { return id; }    
    public void setId(String id) { 
        try {
            this.id = Integer.toString(Integer.parseInt(id));
        } catch (Exception e) {
            this.id = id;             
        }
    }
    
    @Element(name="table-description", required=false)
    private String tableDescription = "";
    public String getTableDescription() { return tableDescription; }    
    public void setTableDescription(String tableDescription) { this.tableDescription = tableDescription; }
    
    @Element(name="value", required=false)
    private String value = "";
    public String getValue() { return value; }    
    public void setValue(String value) { this.value = value; }
    
    @Element(name="desciption", required=false)
    private String desciption = "";
    public String getDescription() { return desciption; }    
    public void setDescription(String desciption) { this.desciption = desciption; }
}
