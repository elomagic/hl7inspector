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

package de.elomagic.hl7inspector.model;

import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class TreeNodeSearchEngine {
    
    /** Creates a new instance of TextNodeSearchEngine */
    private TreeNodeSearchEngine() {
    }    
    
    public final static TreePath findNextNode(String phrase, Object rootNode, Object startingNode, boolean forward) {        
        TreePath result = null;
        
        if (startingNode == null) {
            startingNode = rootNode;            
        }
        
        int level = 0;
        
        System.out.println(startingNode.getClass().getName());
        
        Object o = startingNode;
        
        String t = o.toString();
        
        if (t.indexOf(phrase) != -1) {
            Hl7Object ho = (Hl7Object)o;
            
            for (int i=0; (i<ho.getChildCount()) && (result == null); i++) {
                t = ho.get(i).toString();
                if (t.indexOf(phrase) != -1) {
                    result = findNextNode(phrase, rootNode, ho.get(i), forward);
                    
                    if (result == null) {
                        result = ho.getPath(rootNode);
                    }
                }
            }
        } else {            
            Hl7Object h = (Hl7Object)o;
            
            while ((result == null) && (h.getParent() != null)) {
                result = findNextNode(phrase, rootNode, h.getParent(), forward);
            }
        }
        
        return result;
    }
    
}
