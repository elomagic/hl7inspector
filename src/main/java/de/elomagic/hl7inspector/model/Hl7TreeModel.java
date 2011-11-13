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
import de.elomagic.hl7inspector.hl7.model.RepetitionField;
import de.elomagic.hl7inspector.hl7.model.Segment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class Hl7TreeModel implements TreeModel, TreeNode {

    /** Creates a new instance of Hl7TreeModel */
    public Hl7TreeModel() {
        setCompressedView(true);
    }

    public Hl7TreeModel(boolean compressedView) {
        setCompressedView(compressedView);
    }

    public void addMessage(Message message) {
        message.setRoot(this);
        message.setParent(this);
        objList.add(message);
        fireTreeStructureChanged(this);
    }

    public List<Message> getMessages() {
        return objList;
    }

    public final void setCompressedView(boolean value) {
        if (value != compressed) {
            compressed = value;
            System.setProperty(Hl7Object.COMPRESSED_KEY, (value) ? "t" : "f");

            fireTreeStructureChanged(this);
        }
    }

    public boolean isCompactView() {
        return compressed;
    }

    public void locked() {
        _locked++;
    }

    public void unlock() {
        _locked--;

        if (_locked == 0) {
            fireTreeStructureChanged(this);
        }
    }

    public Object removeChild(Object parent, int index) {
        if (parent instanceof Hl7TreeModel) {
            return objList.remove(index);
        }
//    else
//      return ((Hl7Object)parent)
        return null;
    }

    private int _locked = 0;
//    private boolean _changed = false;
    // Interface TreeModel

    @Override
    public boolean isLeaf(Object node) {
        boolean result;

        if (node instanceof Hl7TreeModel) {
            result = objList.isEmpty();
        } else {
            result = ((Hl7Object) node).isSinglePath();
        }

        return result;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof Hl7TreeModel) {
            return objList.size();
        } else {
            int result;

            if (!compressed) {
                result = ((Hl7Object) parent).size();
            } else {
                result = ((Hl7Object) parent).sizeCompressed();
            }

            if (parent instanceof Segment) {
                result--; // Filter segment from fields
            }

            return result;
        }
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof Hl7TreeModel) {
            return objList.get(index);
        } else {
            if (parent instanceof Segment) {
                index++; // Filter segment from fields
            }

            Hl7Object result;

            if (!compressed) {
                result = ((Hl7Object) parent).get(index);
            } else {
                result = ((Hl7Object) parent).getCompressed(index);
            }

            if ((result instanceof RepetitionField) && (result.size() == 1)) {
                result = result.get(0);
            }

            return result;
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof Hl7TreeModel) {
            return objList.indexOf(child);
        } else {
            int result;

            if (!compressed) {
                result = ((Hl7Object) parent).indexOf((Hl7Object) child);
            } else {
                result = ((Hl7Object) parent).indexCompressedOf((Hl7Object) child);
            }

            if (parent instanceof Segment) {
                result--; // Filter segment from fields
            }

            return result;
        }
    }

    public TreePath getNextMatch(String phrase, Hl7Object startingNode, boolean deepest) {

        return null;
    }

    @Override
    public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
    }

    @Override
    public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
        listenerList.remove(l);
    }

    @Override
    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
        listenerList.add(l);
    }

    @Override
    public Object getRoot() {
        return (objList.isEmpty()) ? null : this;
    }

    @Override
    public String toString() {
        return "Parsed hl7 messages";
    }

//    /** @deprecated */
//    public void fireTreeNodesRemoved(TreePath path, int _index, Hl7Object o) {
//        int index[] = new int[1];
//        Object nodes[] = new Object[1];
//
//        index[0] = _index;
//        nodes[0] = o;
//
//        TreeModelEvent e = new TreeModelEvent(this, path.getParentPath(), index, nodes);
//
//        for (TreeModelListener l : listenerList) {
//            l.treeNodesRemoved(e);
//        }
//    }
    public void fireTreeNodesInsert(TreePath parentPath, Object[] newNodes) {
        int index[] = new int[newNodes.length];

        for (int i = 0; i < newNodes.length; i++) {
            index[i] = getIndexOfChild(parentPath.getLastPathComponent(), newNodes[i]);
        }

        TreeModelEvent e = new TreeModelEvent(this, parentPath, index, newNodes);

        for (TreeModelListener l : listenerList) {
            l.treeNodesInserted(e);
        }
    }

    public void fireTreeStructureChanged(TreePath path) {
        if (_locked == 0) {
            TreeModelEvent e = new TreeModelEvent(this, path);

            for (TreeModelListener l : listenerList) {
                l.treeStructureChanged(e);
            }
        }
    }

    public void fireTreeStructureChanged(Object root) {
        if (_locked == 0) {
            int len = listenerList.size();

            TreeModelEvent e = new TreeModelEvent(this, new Object[]{root});

            for (TreeModelListener l : listenerList) {
                l.treeStructureChanged(e);
            }
        }
    }

    private List<Message> objList = new ArrayList<Message>();
    private List<TreeModelListener> listenerList = new ArrayList<TreeModelListener>();
    private boolean compressed;// = true;
    private boolean viewDescription = false;

    public boolean isViewDescription() {
        return viewDescription;
    }

    public void setViewDescription(boolean viewDescription) {
        this.viewDescription = viewDescription;
    }

    // Interface TreeNode
    /**
     * Returns the child <code>TreeNode</code> at index 
     * <code>childIndex</code>.
     */
    @Override
    public TreeNode getChildAt(int childIndex) {
        return objList.get(childIndex);
    }

    /**
     * Returns the index of <code>node</code> in the receivers children.
     * If the receiver does not contain <code>node</code>, -1 will be
     * returned.
     */
    @Override
    public int getIndex(TreeNode node) {
        return objList.indexOf(node);
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    @Override
    public boolean isLeaf() {
        return !objList.isEmpty();
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    @Override
    public TreeNode getParent() {
        return null;
    }

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver
     * contains.
     */
    @Override
    public int getChildCount() {
        return objList.size();
    }

    /**
     * Returns true if the receiver allows children.
     */
    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    @Override
    public Enumeration<Message> children() {
        return Collections.enumeration(objList);
    }

}
