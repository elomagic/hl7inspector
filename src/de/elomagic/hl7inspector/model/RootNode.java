/*
 * RootNode.java
 *
 * Created on 27. Mai 2006, 19:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.elomagic.hl7inspector.model;

import de.elomagic.hl7inspector.hl7.model.Message;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeNode;

/**
 *
 * @author rambow
 */
public class RootNode implements TreeNode {
    
    /** Creates a new instance of RootNode */
    public RootNode() { }
    
    public void addMessage(Message message) {
        message.setRoot(this);
        message.setParent(this);
        objList.add(message);
    }
    
    public Vector<Message> getVector() { return objList; }
    
    public Message removeMessage(int index) { return objList.remove(index); }

    public boolean removeMessage(Message message) { return objList.remove(message); }
    
    public String toString() { return "Parsed hl7 messages"; }
    
    // Interface TreeNode

    /**
     * Returns the child <code>TreeNode</code> at index 
     * <code>childIndex</code>.
     */
    public TreeNode getChildAt(int childIndex) { return objList.get(childIndex); }

    /**
     * Returns the index of <code>node</code> in the receivers children.
     * If the receiver does not contain <code>node</code>, -1 will be
     * returned.
     */
    public int getIndex(TreeNode node) { return objList.indexOf(node); }

    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf() { return objList.size() != 0; }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    public TreeNode getParent() { return null; }

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver
     * contains.
     */
    public int getChildCount() { return objList.size(); }

    /**
     * Returns true if the receiver allows children.
     */
    public boolean getAllowsChildren() { return true; }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    public Enumeration children() { return objList.elements(); }    
    
    
    private Vector<Message> objList = new Vector<Message>();            
}
