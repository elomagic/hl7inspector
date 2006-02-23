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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author rambow
 */
public class History {
    
    /** Creates a new instance of History */
    public History(String path) {
        this.path = HISTORY.concat(path);

        if (path.lastIndexOf(".") != path.length()-1) {
            this.path = path.concat(".");
        }        
        
        read(System.getProperties());
    }
        
    private String  HISTORY = "runtime-history.";
    private String  path    = "";
    private int     buffer  = 5;
    private Vector<Object>  v       = new Vector<Object>();
    
    public void clear() { v.clear(); }
    
    public int size() { return v.size(); }
    
    public void set(Object o) {
        if (v.contains(o)) {
            v.remove(o);
        }
        
        while (v.size() >= buffer) {
            v.remove(v.size()-1);
        }
        
        v.insertElementAt(o, 0);
        
        write(System.getProperties());
    }
    
    public Vector getVector() { return v; }
    
    public Iterator getIterator() { return v.iterator(); }
    
    public Enumeration getEnumeration() { return v.elements(); }
    
    public Vector getStrings() {
        Vector<String> r = new Vector<String>();
        
        for (int i=0; i<v.size(); i++) {
            r.add(v.get(i).toString());
        }
        
        return r;
    }
    
    private void clearProperties(Properties prop) {        
        int i = 1;
        boolean next=false;
        
        do {
            String key = path.concat(Integer.toString(i));
            
            next = prop.containsKey(key);
            
            if (next) {
                prop.setProperty(key, null);
            }
            
            i++;                       
        } while (next);                               
    }
    
    public void write(Properties prop) {
        String p = path;

        if (!System.getProperties().equals(prop)) {
            clearProperties(prop);            
            p = path.substring(HISTORY.length());
        }
               
        for (int i=0; i<v.size(); i++) {
            Object o = v.get(i);
            
            String key = p + Integer.toString(i+1);
            
            prop.setProperty(key, o.toString());
        }                
    }
    
    public void read(Properties prop) {
        clear();
        
        int i = 1;
        boolean next=false;
        
        do {
            String key = path.concat(Integer.toString(i));
            
            next = prop.containsKey(key);
            
            if (next) {
                String value = prop.getProperty(key);                
                v.add(value);
            }
            
            i++;                       
        } while (next);               
    }
}
