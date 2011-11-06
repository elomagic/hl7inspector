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
@XmlRootElement(name = "data-type")
@XmlAccessorType(XmlAccessType.NONE)
public final class DataTypeItem {

    /** Creates a new instance of DataType */
    public DataTypeItem() {
    }

    public DataTypeItem(String parentDataType, int index, String desc, String dataType) {
        setParentDataType(parentDataType);
        setIndex(index);
        setDescription(desc);
        setDataType(dataType);
    }

    private int len = 0;

    @XmlElement(name = "length")
    public int getLength() {
        return len;
    }

    public void setLength(int l) {
        this.len = l;
    }

    public void setLength(String l) {
        try {
            this.len = Integer.parseInt(l);
        } catch (Exception e) {
            this.len = 0;
        }
    }

    private int index = 0;

    @XmlElement(name = "index")
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIndex(String index) {
        try {
            this.index = Integer.parseInt(index);
        } catch (Exception e) {
            this.index = 0;
        }
    }

    private String description = "";

    @XmlElement(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String dataType = "";

    @XmlElement(name = "datatype")
    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType.trim();
    }

    private String parentDataType = "";

    @XmlElement(name = "parent")
    public String getParentDataType() {
        return parentDataType;
    }

    public void setParentDataType(String dataType) {
        this.parentDataType = dataType.trim();
    }

    private String opt = "O";

    @XmlElement(name = "opt")
    public String getOptionality() {
        return opt;
    }

    public void setOptionality(String value) {
        opt = value.trim();
    }

    private String chapter = "";

    @XmlElement(name = "chapter")
    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    private String parentDataTypeName = "";

    @XmlElement(name = "parent-name")
    public String getParentDataTypeName() {
        return parentDataTypeName;
    }

    public void setParentDataTypeName(String parentDataTypeName) {
        this.parentDataTypeName = parentDataTypeName.trim();
    }

    private String table = "";

    @XmlElement(name = "table")
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        try {
            this.table = Integer.valueOf(table).toString();
        } catch (Exception ex) {
            this.table = table.trim();
        }
    }

}
