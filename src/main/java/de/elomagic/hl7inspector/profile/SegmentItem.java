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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rambow
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SegmentItem extends Properties {
    private static final long serialVersionUID = -4257952051912790214L;
    private String chapter = "";
    private String description = "";
    private String id = "";

    /** Creates a new instance of SegmentDefinitions */
    public SegmentItem() {
    }

    public SegmentItem(String id, String desc, String chapter) {
        this.id = id;
        this.description = desc;
        this.chapter = chapter;
    }

    @XmlElement(name = "chapter", required = false)
    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    @XmlElement(name = "description", required = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    @XmlElement(name = "id", required = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
