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

package de.elomagic.hl7inspector.hl7.model;

import de.elomagic.hl7inspector.utils.StringEscapeUtils;
import de.elomagic.hl7inspector.validate.ValidateStatus;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public abstract class Hl7Object implements TreeNode {
    
    /** Creates a new instance of Hl7Object */
    public Hl7Object() { }
    
    public void parse(String text) {
        // FEATURE Dynamic encoding support (Custimize encoding)
        
        clear();
        
        try {
            StringBuffer subText = new StringBuffer();
            int p = 0;
            
            while (p < text.length()) {
                char c = text.charAt(p);
                
                if (!((text.startsWith("MSH|^~\\&")) && (p == 6)) && (c == Delimiters.DEFAULT_ESCAPE_CHAR)) {
                    // Wenn Escapezeichen kommt dann nächstes Zeichen nicht interpretieren
//                    subText = subText.append(c);
//                    p++;
//                    if (text.length() > p) {
//                        subText = subText.append(c);
//                    }
                    
                    // Wenn Escapezeichen kommt dann nächstes Zeichen nicht interpretieren
                    
                    do {
                        subText = subText.append(c);
                        p++;
                        
                        if (text.length() > p) {
                            c = text.charAt(p);
                        }
                    } while ((text.length() > p) && (c != Delimiters.DEFAULT_ESCAPE_CHAR));
                    if (c == Delimiters.DEFAULT_ESCAPE_CHAR) {
                        subText = subText.append(c);
                    }
                } else {
                    if (c == getSubDelimiter()) {
                        // Wenn neues Feld dann altes in Array sichern
//            add(subText, not ((subText = (COMPONENT_CHAR + REPEATION_CHAR + ESCAPE_CHAR + SUBCOMPONENT_CHAR)) and (p == 9) and (copy(text, 1, 3) = 'MSH')));
                        
//                        if (subText.eq)
                        
                        if ((c == Delimiters.DEFAULT_FIELD) && (subText.toString().equals("MSH")) && (objList.size() == 0)) {
                            add("MSH");
                            add("" + Delimiters.DEFAULT_FIELD);
                            add(new EncodingObject());
                            p+=5;
                        } else
                            add(subText.toString());
                        
                        subText = new StringBuffer();
                    } else {
                        subText = subText.append(c);
                    }
                }
                
                p++;
            }
            
            if (subText.length() != 0) {
                add(subText.toString());
            }
        } catch (Exception e) {
            System.err.println("Error parsing message!");
        }
    }
    
    public void clear() {
        objList.clear();
        description = "";
        
        resetTreeData(this);
    }
    
    private void resetTreeData(Hl7Object o) {
        while (o != null) {
            o.htmlText = null;
            o.nodeText = null;
            o.val = null;
            o.validationText = "";
            
            o = o.getParent();
        }
    }
    
    private void add(Hl7Object obj) {
        obj.setRoot(getRoot());
        obj.setParent(this);
        objList.add(obj);
    }
    
    public Hl7Object add(String text) throws Exception {
        Class child = getChildClass();
        if (child == null) {
            throw new Exception("Child items are not allowed for this item type");
        }
        
        Hl7Object obj = (Hl7Object)(child.newInstance());
        obj.setRoot(getRoot());
        obj.setParent(this);
        obj.parse(text);
        objList.add(obj);
        
        
        return obj;
    }
    
    public void remove(Hl7Object child) {
        Hl7Object parent = child.getParent();
        
        objList.remove(child);
        
        resetTreeData(parent);
    }
    
    private String validationText = "";
    public String getValidationText() { return validationText; }
    public void setValidationText(String value) { validationText = value; }
    
    private String description = "";
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description.trim(); }
    
    private String nodeText = null;
    public String getText() { return nodeText; }
    public void setText(String value) { nodeText = value; }
    
    private ValidateStatus val = null;
    public ValidateStatus getValidateStatus() { return val; }
    public void setValidateStatus(ValidateStatus v) { val = v; }
    
    private String htmlText = null;
    public String toHtmlEscapedString() {
        if (htmlText == null) {
            htmlText = StringEscapeUtils.escapeHtml(toString());
        }
        
        return htmlText;
    }
    
    private String getTypeDescription() {
        String s = getClass().getName();
        s = s.substring(s.lastIndexOf(".")+1);
        return s.toLowerCase();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<objList.size(); i++) {
            if (!((this instanceof Segment) && (i == 1) && (get(0).toString().equals("MSH"))))  {
                sb.append(objList.get(i).toString());
                sb.append(getSubDelimiter());
            }
        }
        
        if ((sb.length() != 0) && (!(this instanceof Message))) {
            sb.deleteCharAt(sb.length()-1);
        }
        
        return sb.toString();
    }
    
    public boolean isSinglePath() {
        boolean r = (objList.size() == 0);
        if (objList.size() == 1) {
            r = objList.get(0).isSinglePath();
        }
        
        return r;
    }
    
    public TreePath getPath(Object rootNode) {
        Vector<Object> v = new Vector<Object>();
        
        Hl7Object o = this;
        v.add(o);
        
        while (o.getParent() != null) {
            o = o.getParent();
            
            v.insertElementAt(o, 0);
        }
        
        v.insertElementAt(rootNode, 0);
        
        TreePath path = new TreePath(v.toArray());
        
        return path;
    }
    
    //public Hl7Object getParent() { return parent; }
    public void setParent(Hl7Object parent) { this.parent = parent; }
    
    public void setRoot(Object value) { root = value; }
    public Object getRoot() { return root; }
    
    public abstract char getSubDelimiter();
    
    public abstract Class getChildClass();
    
    public int size() { return objList.size(); }
    
    public int sizeCompressed() {
        int r = 0;
        
        for (int i=0;i<size();i++) {
            if (!get(i).isNULL()) {
                r++;
            }
        }
        
        return r;
    }
    
    public Hl7Object get(int index) { return objList.get(index); }
    
    public Hl7Object getCompressed(int index) throws IndexOutOfBoundsException {
        int idx = -1;
        for (int i=0;i<size();i++) {
            if (!get(i).isNULL()) {
                idx++;
            }
            
            if (idx == index) {
                return get(i);
            }
        }
        
        throw new IndexOutOfBoundsException();
    }
    
    public int getIndex() {
        int r = -1;
        
        if (getParent() != null) {
            r = getParent().indexOf(this);
        }
        
        return r;
    }
    
    public int indexOf(TreeNode value) { return objList.indexOf(value); }
    
    public int indexCompressedOf(TreeNode value) {
        int idx = indexOf(value);
        int r = 0;
        for (int i=0;i<idx;i++) {
            if (!get(i).isNULL()) {
                r++;
            }
        }
        return r;
    }
    
    protected boolean isNULL() { return objList.size() == 0; }
    
    private Vector<Hl7Object> objList = new Vector<Hl7Object>();
    private Object    root    = null;
    private Hl7Object parent  = null;
    
    private final static String COMPRESSED_KEY = Hl7Object.class.getName().concat(".compressed");
    
    // TODO Muste be implemented
    // Interface TreeNode
    
    /** Returns the children of the receiver as an Enumeration. */
    public Enumeration children() { 
        Vector<TreeNode> result = new Vector<TreeNode>();
        
        for (int i=0; i<getChildCount(); i++) {            
            result.add(getChildAt(i));
        }
        
        return result.elements(); 
    }
    
    /** 
     * Returns true if the receiver allows children. 
     */
    public boolean getAllowsChildren() { return getChildClass() != null; }
    
    /** 
     * Returns the number of children TreeNodes the receiver contains. 
     */
    public int getChildCount() { 
        boolean compressed = "t".equals(System.getProperty(COMPRESSED_KEY, "f"));
        return (compressed)?sizeCompressed():size();
    }
    
    /**
     * Returns the child TreeNode at index childIndex.
     */
    public TreeNode getChildAt(int childIndex) {
        if (this instanceof Segment) {
            childIndex++; // Filter segment from fields
        }

        TreeNode result = null;
        
        boolean compressed = "t".equals(System.getProperty(COMPRESSED_KEY, "f"));

        if (!compressed) {
            result = get(childIndex);
        } else {
            result = getCompressed(childIndex);
        }

        if ((result instanceof RepetitionField) && (result.getChildCount() == 1)) {
            result = result.getChildAt(0);
        }

        return result;
    }
    
    /**
     * Returns the index of node in the receivers children.
     */
    public int getIndex(TreeNode node) {
        boolean compressed = "t".equals(System.getProperty(COMPRESSED_KEY, "f"));

        int result = -1;
        
        result = (compressed)?getParent().indexCompressedOf(node):getParent().indexOf(node);                
        return result;
    }
    
    /**
     * Returns the parent TreeNode of the receiver.
     */
    //public TreeNode getParent() { return parent; }
    public Hl7Object getParent() { return parent; }    
    
    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf() { return isSinglePath(); }
}
