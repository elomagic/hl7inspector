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

import de.elomagic.hl7inspector.validate.ValidateStatus;
import nanoxml.XMLElement;

/**
 *
 * @author rambow
 */
public class ValidateMapper {

    /** Creates a new instance of ValidateMapper */
    public ValidateMapper() {
    }

    public void read(XMLElement element) {
        ElementTable elements = new ElementTable(element.getChildren());

        if (elements.containsKey("length")) {
            mapLength = Integer.parseInt(elements.get("length").getContent());
        }

        if (elements.containsKey("deprecated")) {
            mapDeprecated = Integer.parseInt(elements.get("deprecated").getContent());
        }

        if (elements.containsKey("conditional")) {
            mapConditional = Integer.parseInt(elements.get("conditional").getContent());
        }

        if (elements.containsKey("required")) {
            mapRequired = Integer.parseInt(elements.get("required").getContent());
        }

        if (elements.containsKey("item-missing")) {
            mapItemMissing = Integer.parseInt(elements.get("item-missing").getContent());
        }

        if (elements.containsKey("definition-not-found")) {
            mapDefNotFound = Integer.parseInt(elements.get("definition-not-found").getContent());
        }

        if (elements.containsKey("repetition")) {
            mapRepetition = Integer.parseInt(elements.get("repetition").getContent());
        }
    }

    public XMLElement write() {
        XMLElement xmlElement = new XMLElement();
        xmlElement.setName("validate");

        XMLElement xmlMap = new XMLElement();
        xmlMap.setName("length");
        xmlMap.setContent(Integer.toString(mapLength));
        xmlElement.addChild(xmlMap);

        xmlMap = new XMLElement();
        xmlMap.setName("deprecated");
        xmlMap.setContent(Integer.toString(mapDeprecated));
        xmlElement.addChild(xmlMap);

        xmlMap = new XMLElement();
        xmlMap.setName("conditional");
        xmlMap.setContent(Integer.toString(mapConditional));
        xmlElement.addChild(xmlMap);

        xmlMap = new XMLElement();
        xmlMap.setName("required");
        xmlMap.setContent(Integer.toString(mapRequired));
        xmlElement.addChild(xmlMap);

        xmlMap = new XMLElement();
        xmlMap.setName("item-missing");
        xmlMap.setContent(Integer.toString(mapItemMissing));
        xmlElement.addChild(xmlMap);

        xmlMap = new XMLElement();
        xmlMap.setName("definition-not-found");
        xmlMap.setContent(Integer.toString(mapDefNotFound));
        xmlElement.addChild(xmlMap);

        xmlMap = new XMLElement();
        xmlMap.setName("repetition");
        xmlMap.setContent(Integer.toString(mapRepetition));
        xmlElement.addChild(xmlMap);

        return xmlElement;
    }

    public int getMapLength() {
        return mapLength;
    }

    public void setMapLength(int map) {
        mapLength = map;
    }

    public int getMapDeprecated() {
        return mapDeprecated;
    }

    public void setMapDeprecated(int map) {
        mapDeprecated = map;
    }

    public int getMapConditional() {
        return mapConditional;
    }

    public void setMapConditional(int map) {
        mapConditional = map;
    }

    public int getMapRequired() {
        return mapRequired;
    }

    public void setMapRequired(int map) {
        mapRequired = map;
    }

    public int getMapItemMiss() {
        return mapItemMissing;
    }

    public void setMapItemMiss(int map) {
        mapItemMissing = map;
    }

    public int getMapDefNotFound() {
        return mapDefNotFound;
    }

    public void setMapDefNotFound(int map) {
        mapDefNotFound = map;
    }

    public int getMapRepetition() {
        return mapRepetition;
    }

    public void setMapRepetition(int map) {
        mapRepetition = map;
    }

    private int mapLength = ValidateStatus.WARN;

    private int mapDeprecated = ValidateStatus.INFO;

    private int mapConditional = ValidateStatus.INFO;

    private int mapRequired = ValidateStatus.ERROR;

    private int mapItemMissing = ValidateStatus.ERROR;

    private int mapDefNotFound = ValidateStatus.WARN;

    private int mapRepetition = ValidateStatus.ERROR;
}
