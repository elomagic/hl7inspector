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

import java.util.Properties;
import java.util.Vector;
import nanoxml.XMLElement;
import org.simpleframework.xml.Element;

/**
 *
 * @author rambow
 */
public class SegmentItem extends Properties {
    
    /** Creates a new instance of SegmentDefinitions */    
    public SegmentItem()  { }
    
    public SegmentItem(String _id, String desc, String _chapter)  {
        
        setId(_id);
        description = desc;
        chapter     = _chapter;
    }
    
    public SegmentItem(XMLElement xml) {
        Vector v = xml.getChildren();
        for (int i=0;i<v.size();i++) {
            XMLElement el = (XMLElement)v.get(i);
            
            if (el.getName().equals("id")) {
                setId(el.getContent());
            } else if (el.getName().equals("description")) {
                setDescription(el.getContent());
            } else if (el.getName().equals("chapter")) {
                setChapter(el.getContent());
            }
        }
    }
    
    public XMLElement getXMLElement() {
        XMLElement xml = new XMLElement();
        xml.setName("segment");
        xml.setAttribute("id", getId());
        
        XMLElement el = new XMLElement();
        el.setName("id");
        el.setContent(getId());
        xml.addChild(el);
        
        el = new XMLElement();
        el.setName("description");
        el.setContent(getDescription());
        xml.addChild(el);
        
        el = new XMLElement();
        el.setName("chapter");
        el.setContent(getChapter());
        xml.addChild(el);
        
        return xml;
    }    
    
    @Element(name="chapter", required=false)
    private String chapter = "";
    public String getChapter() { return chapter; }    
    public void setChapter(String chapter) { this.chapter = chapter; }
    
    @Element(name="description", required=false)
    private String description = "";
    public String getDescription() { return description; }    
    public void setDescription(String d) { description = d; }        
    
    @Element(name="id", required=false)
    private String id = "";
    public String getId() { return id; }    
    public void setId(String id) { this.id = id; }
    
}
