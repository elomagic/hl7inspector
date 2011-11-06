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
import java.util.Properties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import nanoxml.XMLElement;

/**
 *
 * @author rambow
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SegmentItem extends Properties {

    private static final long serialVersionUID = -4257952051912790214L;

    /** Creates a new instance of SegmentDefinitions */
    public SegmentItem() {
    }

    public SegmentItem(String _id, String desc, String _chapter) {
        this.id = _id;
        this.description = desc;
        this.chapter = _chapter;
    }

    public SegmentItem(XMLElement xml) {
        List v = xml.getChildren();
        for (int i = 0; i < v.size(); i++) {
            XMLElement el = (XMLElement) v.get(i);

            if (el.getName().equals("id")) {
                this.id = el.getContent();
            } else if (el.getName().equals("description")) {
                this.description = el.getContent();
            } else if (el.getName().equals("chapter")) {
                this.chapter = el.getContent();
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

    private String chapter = "";

    @XmlElement(name = "chapter", required = false)
    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    private String description = "";

    @XmlElement(name = "description", required = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    private String id = "";

    @XmlElement(name = "id", required = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
