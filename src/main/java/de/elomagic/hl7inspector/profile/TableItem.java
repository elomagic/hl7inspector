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
@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.NONE)
public class TableItem {

    /** Creates a new instance of TableItem */
    public TableItem() {
    }

    private String type = "HL7";

    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String id = "";

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        try {
            this.id = Integer.toString(Integer.parseInt(id));
        } catch (Exception e) {
            this.id = id;
        }
    }

    private String tableDescription = "";

    @XmlElement(name = "table-description")
    public String getTableDescription() {
        return tableDescription;
    }

    public void setTableDescription(String tableDescription) {
        this.tableDescription = tableDescription;
    }

    private String value = "";

    @XmlElement(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String desciption = "";

    @XmlElement(name = "description")
    public String getDescription() {
        return desciption;
    }

    public void setDescription(String desciption) {
        this.desciption = desciption;
    }

}
