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
package de.elomagic.hl7inspector.utils;

import de.elomagic.hl7inspector.StartupProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Handles recently used value in a list in the {@link StartupProperties}.
 */
public class RecentList {
    private Properties properties;
    private String propertyPrefix;
    private int maxItems = 5;
    private List<String> list = new ArrayList<>();

    /**
     * Creates a new instance of History.
     */
    public RecentList(Properties properties, String propertyPrefix, int maxItems) {
        this.properties = properties;
        this.propertyPrefix = propertyPrefix;
        this.maxItems = maxItems;

        fill();
    }

    private void fill() {
        list.clear();

        for(int i = 0; i < maxItems; i++) {
            int index = i + 1;
            String property = propertyPrefix + "." + Integer.toString(index);

            if(properties.containsKey(propertyPrefix)) {
                list.add(properties.getProperty(property));
            }
        }
    }

    /**
     * Put a new value at the top of the recently used item list.
     * <p/>
     * If value already exists it will be removed before
     *
     * @param value String value
     */
    public void put(String value) {
        list.remove(value);
        list.add(0, value);

        updateProperties();
    }

    private void updateProperties() {
        int index = 0;
        for(String item : list) {
            index++;
            String property = propertyPrefix + "." + Integer.toString(index);
            properties.setProperty(property, item);
        }
    }

    /**
     * Returns an unmodifiable view of the specified list.
     *
     * @return List
     */
    public List<String> getList() {
        return Collections.unmodifiableList(list);
    }
}
