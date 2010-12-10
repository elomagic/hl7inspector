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

package de.elomagic.hl7inspector.file.filters;

/**
 *
 * @author rambow
 */
public class TextFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
    
    /** Creates a new instance of HIPFileFilter */
    public TextFileFilter() { super(); }

    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(java.io.File f) {
        boolean r = f.isDirectory();
        
        try {
            if (!r) {
                String ap = f.getAbsolutePath();

                int i =  ap.lastIndexOf(".");

                if (i != -1)  {
                    r = ap.substring(i+1).equalsIgnoreCase("txt");
                }
            }
        } catch (Exception e) {
            r = false;
        }
        
        return r;        
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     * @see FileView#getName
     */
    public String getDescription() { return "Text files"; }
}
