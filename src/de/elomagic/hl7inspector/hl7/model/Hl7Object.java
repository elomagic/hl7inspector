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

/**
 *
 * @author rambow
 */
public abstract class Hl7Object {
    
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
    
    public Hl7Object add(String text) {
        Hl7Object obj = getNewClientInstance();
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
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<objList.size(); i++) {
            if (!((this instanceof Segment) && (i == 1) && (get(0).toString().equals("MSH"))))  {
                sb.append(objList.get(i).toString());
                sb.append(getSubDelimiter());
            }
        }
        
        if ((sb.length() != 0) && (!(this instanceof Segment))) {
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
    
    public Hl7Object getParent() { return parent; }
    public void setParent(Hl7Object parent) { this.parent = parent; }
    
    public void setRoot(Object value) { root = value; }
    public Object getRoot() { return root; }
    
    public abstract char getSubDelimiter();
    
    public abstract Hl7Object getNewClientInstance();
    
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
    
    public int indexOf(Hl7Object value) { return objList.indexOf(value); }
    
    public int indexCompressedOf(Hl7Object value) {
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
    
    // TODO Muste be implemented
    // Interface TreeNode
    
    /** Returns the children of the receiver as an Enumeration. */
    public Enumeration 	children() { return objList.elements(); }
    
    //** Returns true if the receiver allows children. */
    public boolean getAllowsChildren() { return getNewClientInstance() != null; }
    
    /** Returns the number of children TreeNodes the receiver contains. */
    public int getChildCount() { return objList.size(); }
    
/*             TreeNode 	getChildAt(int childIndex)
            Returns the child TreeNode at index childIndex.
 
            int 	getIndex(TreeNode node)
            Returns the index of node in the receivers children.
            TreeNode 	getParent()
            Returns the parent TreeNode of the receiver.
            boolean 	isLeaf()
            Returns true if the receiver is a leaf.
 */
}
