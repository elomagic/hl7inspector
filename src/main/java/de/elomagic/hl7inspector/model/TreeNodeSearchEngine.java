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
 */
package de.elomagic.hl7inspector.model;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.elomagic.hl7inspector.hl7.model.Hl7Object;

/**
 *
 * @author rambow
 */
public class TreeNodeSearchEngine {
    /**
     * Creates a new instance of TextNodeSearchEngine.
     */
    private TreeNodeSearchEngine() {
    }

    public static TreePath findNextNode(String phrase, boolean caseSensitive, TreeNode sNode) {
        TreePath result = findNode(phrase, caseSensitive, sNode, 0);

        if(result == null) {
            result = findNode2(phrase, caseSensitive, sNode);
        }

        return result;
    }

    private static TreePath findNode(String phrase, boolean caseSensitive, TreeNode sNode, int index) {
        TreePath result = null;

        if(sNode == null) {
            throw new IllegalArgumentException("No starting node selected.");
        }

        String t = sNode.toString();

        if(!caseSensitive) {
            t = t.toUpperCase();
            phrase = phrase.toUpperCase();
        }

        if((t.contains(phrase) || sNode instanceof Hl7TreeModel)) {
            for(int i = index; i < sNode.getChildCount() && result == null; i++) {
                TreeNode node = sNode.getChildAt(i);
                String tt = node.toString();

                if(!caseSensitive) {
                    tt = tt.toUpperCase();
                }

                if(tt.contains(phrase)) {
                    if(!node.isLeaf()) {
                        result = ((Hl7Object)node).getPath();
                    } else {
                        result = findNode(phrase, caseSensitive, node, 0);
                    }

                    if(result == null) { //&& (!startingNode.isLeaf())){
                        result = ((Hl7Object)node).getPath();
                    }
                }
            }

//            if (sNode.equals(result)) {
//                result = null;
//            }
        }
        return result;
    }

    private static TreePath findNode2(String phrase, boolean caseSensitive, TreeNode sNode) {
        TreePath result = null;
        TreeNode node = sNode;
        TreeNode parent = sNode.getParent();

        while(result == null && parent != null) {
            int idx = parent.getIndex(node);
            String t = parent.toString();

            if(!caseSensitive) {
                t = t.toUpperCase();
                phrase = phrase.toUpperCase();
            }

            if(t.contains(phrase) || parent instanceof Hl7TreeModel) {
                result = findNode(phrase, caseSensitive, parent, idx + 1);
            }

            if(result == null) {
                node = parent;
                parent = parent.getParent();
            }
        }
        return result;
    }
}
