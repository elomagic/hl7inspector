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
import de.elomagic.hl7inspector.hl7.model.Message;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
//import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class TreeNodeSearchEngine {
    
    /** Creates a new instance of TextNodeSearchEngine */
    private TreeNodeSearchEngine() {
    }
    
    public final static TreePath findNextNode(String phrase, boolean caseSensitive, TreeNode rootNode, TreeNode startingNode, int index, boolean forward) {
//        Logger log = Logger.getLogger(TreeNodeSearchEngine.class);
        TreePath result = null;
        
        if (startingNode == null) {
            startingNode = rootNode;
        }
        
        String t = startingNode.toString();
        
        if (!caseSensitive) {
            t = t.toUpperCase();
            phrase = phrase.toUpperCase();
        }
        
        if (((t.indexOf(phrase) != -1) || (startingNode instanceof Hl7TreeModel))) {// && (o.isLeaf())) {
            for (int i=index; (i<startingNode.getChildCount()) && (result == null); i++) {
                TreeNode node = startingNode.getChildAt(i);
                String tt = node.toString();
                
                if (!caseSensitive) {
                    tt = tt.toUpperCase();
                }
                
                if (tt.indexOf(phrase) != -1) {
                    if (!node.isLeaf()) {
                        result = ((Hl7Object)node).getPath(rootNode);
                    } else {
                        result = findNextNode(phrase, caseSensitive, rootNode, node, 0, forward);
                    }
                    
                    if ((result == null)) { //&& (!startingNode.isLeaf())){
                        result = (startingNode instanceof Hl7Object)?((Hl7Object)node).getPath(rootNode):(new TreePath(rootNode));
                    }
                }
            }
        } else {        
        //if ((result == null) && (t.indexOf(phrase) == -1)) {
            TreeNode node = startingNode;
            TreeNode parent = (node instanceof Message)?rootNode:startingNode.getParent();
            
            while ((result == null) && (parent != null)) {
                int idx = parent.getIndex(node);
                t = parent.toString();
                
                if (!caseSensitive) {
                    t = t.toUpperCase();
                    phrase = phrase.toUpperCase();
                }
                
                if ((t.indexOf(phrase) != -1) || (parent instanceof Hl7TreeModel)) {
                    result = findNextNode(phrase, caseSensitive, rootNode, parent, idx+1, forward);
                } 
                
                if (result == null) {
                    node = parent;
                    parent = (node instanceof Message)?rootNode:parent.getParent();
                }
            }
        }
        
        return result;
    }
}
