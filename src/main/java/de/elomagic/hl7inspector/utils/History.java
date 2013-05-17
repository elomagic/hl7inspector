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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author rambow
 */
public class History {
    private String HISTORY = "runtime-history.";
    private String path = "";
    private int buffer = 5;
    private List<String> v = new ArrayList<>();

    /**
     * Creates a new instance of History.
     */
    public History(String path) {
        this.path = HISTORY.concat(path);

        if(path.lastIndexOf(".") != path.length() - 1) {
            this.path = path.concat(".");
        }

        read(System.getProperties());
    }

    public void clear() {
        v.clear();
    }

    public int size() {
        return v.size();
    }

    public void set(String o) {
        if(v.contains(o)) {
            v.remove(o);
        }

        while(v.size() >= buffer) {
            v.remove(v.size() - 1);
        }

        v.add(0, o);

        write(System.getProperties());
    }

    public List<String> getList() {
        return v;
    }

    public Iterator getIterator() {
        return v.iterator();
    }

    private void clearProperties(Properties prop) {
        int i = 1;
        boolean next = false;

        do {
            String key = path.concat(Integer.toString(i));

            next = prop.containsKey(key);

            if(next) {
                prop.setProperty(key, null);
            }

            i++;
        } while(next);
    }

    public void write(Properties prop) {
        String p = path;

        if(!System.getProperties().equals(prop)) {
            clearProperties(prop);
            p = path.substring(HISTORY.length());
        }

        for(int i = 0; i < v.size(); i++) {
            String key = p + Integer.toString(i + 1);
            String value = v.get(i);

            prop.setProperty(key, value);
        }
    }

    public final void read(Properties prop) {
        clear();

        int i = 1;
        boolean next;

        do {
            String key = path.concat(Integer.toString(i));

            next = prop.containsKey(key);

            if(next) {
                String value = prop.getProperty(key);
                v.add(value);
            }

            i++;
        } while(next);
    }
}
