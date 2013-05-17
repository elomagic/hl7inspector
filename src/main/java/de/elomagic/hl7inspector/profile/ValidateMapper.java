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

import de.elomagic.hl7inspector.validate.ValidateStatus;

/**
 *
 * @author rambow
 */
@XmlRootElement(name = "validate")
@XmlAccessorType(XmlAccessType.NONE)
public class ValidateMapper {
    private int mapLength = ValidateStatus.WARN;
    private int mapDeprecated = ValidateStatus.INFO;
    private int mapConditional = ValidateStatus.INFO;
    private int mapRequired = ValidateStatus.ERROR;
    private int mapItemMissing = ValidateStatus.ERROR;
    private int mapDefNotFound = ValidateStatus.WARN;
    private int mapRepetition = ValidateStatus.ERROR;

    /** Creates a new instance of ValidateMapper */
    public ValidateMapper() {
    }

    @XmlElement(name = "length")
    public int getMapLength() {
        return mapLength;
    }

    public void setMapLength(int map) {
        mapLength = map;
    }

    @XmlElement(name = "deprecated")
    public int getMapDeprecated() {
        return mapDeprecated;
    }

    public void setMapDeprecated(int map) {
        mapDeprecated = map;
    }

    @XmlElement(name = "conditional")
    public int getMapConditional() {
        return mapConditional;
    }

    public void setMapConditional(int map) {
        mapConditional = map;
    }

    @XmlElement(name = "required")
    public int getMapRequired() {
        return mapRequired;
    }

    public void setMapRequired(int map) {
        mapRequired = map;
    }

    @XmlElement(name = "item-missing")
    public int getMapItemMiss() {
        return mapItemMissing;
    }

    public void setMapItemMiss(int map) {
        mapItemMissing = map;
    }

    @XmlElement(name = "definition-not-found")
    public int getMapDefNotFound() {
        return mapDefNotFound;
    }

    public void setMapDefNotFound(int map) {
        mapDefNotFound = map;
    }

    @XmlElement(name = "repetition")
    public int getMapRepetition() {
        return mapRepetition;
    }

    public void setMapRepetition(int map) {
        mapRepetition = map;
    }
}
