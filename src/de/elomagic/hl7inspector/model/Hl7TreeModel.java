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
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class Hl7TreeModel extends DefaultTreeModel {
    
    /** Creates a new instance of Hl7TreeModel */
    public Hl7TreeModel() { 
        super(new RootNode(), false); 
    
        setCompressedView(true); 
    }
    
    public Hl7TreeModel(boolean compactView) { 
        super(new RootNode(), false); 
        
        setCompressedView(compactView); 
    }
    
    public void addMessage(Message message) {
        RootNode rn = (RootNode)getRoot();
        rn.addMessage(message);
        
        if (_locked == 0) {
            fireTreeNodesInserted(rn, new Object[] { rn }, new int[] { rn.getVector().indexOf(message) }, new Object[] { message } );
        }
    }
    
    public Message removeMessage(int index) { 
        RootNode rn = (RootNode)getRoot();
        Message result;
        result = rn.removeMessage(index); 
        
        //fireTreeNodesRemoved(rn, new Object[] { rn }, new int[] { index }, new Object[] { result } );
        
        return result;
    }
    
    public Vector<Message> getMessages() { return ((RootNode)getRoot()).getVector(); }
    
    public void setCompressedView(boolean value) {
        if (value != isCompactView()) {            
            Hl7Object.setCompactView(value);
            
            fireTreeStructureChanged();
        }
    }
    
    public boolean isCompactView() { 
        boolean r = Hl7Object.isCompactView();
        return r;
    }
    
    private int _locked = 0;
    
    public void locked() { _locked++; }
    
    public void unlock() {
        _locked--;
        
        fireTreeStructureChanged();
    }
    
    public void fireTreeStructureChanged() {
        if (_locked == 0) {
            RootNode rn = (RootNode)getRoot();
            
            int[] indices= new int[rn.getChildCount()];
            for (int i=0; i<indices.length; i++) {
                indices[i] = i;
            }
            
            fireTreeNodesInserted(rn, new Object[] { rn }, indices, rn.getVector().toArray() );
        }
    }
    
    public void fireTreeNodesInsert(TreePath parentPath, Object[] newNodes) {
        int index[] = new int[newNodes.length];
     
        for (int i = 0; i<newNodes.length; i ++) {
            index[i] = getIndexOfChild(parentPath.getLastPathComponent(), newNodes[i]);
        }
     
        TreeModelEvent e = new TreeModelEvent(this, parentPath, index, newNodes);
     
        for (int i = 0; i < listenerList.size(); i++) {
            ((TreeModelListener)listenerList.elementAt(i)).treeNodesInserted(e);
        }
    }
     
    public void fireTreeStructureChanged(TreePath path) {
        if (_locked == 0) {
            int len = listenerList.size();
     
            TreeModelEvent e = new TreeModelEvent(this, path);
     
            for (int i = 0; i < len; i++) {
                ((TreeModelListener)listenerList.elementAt(i)).treeStructureChanged(e);
            }
        }
    }
     
    public void fireTreeStructureChanged(Object root) {
        if (_locked == 0) {
            int len = listenerList.size();
     
            TreeModelEvent e = new TreeModelEvent(this, new Object[] { root });
     
            for (int i = 0; i < len; i++) {
                ((TreeModelListener)listenerList.elementAt(i)).treeStructureChanged(e);
            }
        }
    }
    
    private Vector<TreeModelListener> listenerList = new Vector<TreeModelListener>();
    
    private boolean viewDescription = false;
    
    public boolean isViewDescription() { return viewDescription; }
    
    public void setViewDescription(boolean viewDescription) { this.viewDescription = viewDescription; }    
}
