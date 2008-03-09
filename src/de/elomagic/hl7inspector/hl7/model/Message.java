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

import de.elomagic.hl7inspector.hl7.parser.MessageEncoding;
import java.io.File;
import javax.swing.tree.TreeNode;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class Message extends Hl7Object {
    
    /** Creates a new instance of Message */
    public Message() { }
    
    public char getSubDelimiter() { return (char)0x0d; }
    
    public Class getChildClass() { return Segment.class; }
    
    private String source = "";
    public void setSource(String messageSource) { source = messageSource; }
    public String getSource() { return source; }
    
    private MessageEncoding format = MessageEncoding.HL7_FORMAT;
    public MessageEncoding getMessageFormat() { return format; }
    
    public void setDelimiters(Delimiters d) { del = d; }
    
    public void setFile(File f) {
        try {
            source = (f==null)?"":f.toURI().toURL().toString();
        } catch (Exception e) {
            source = "";
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
    }
    
    private TreeNode parent;
    
    @Override
    public TreeNode getParent() { return parent; }
    public void setParent(TreeNode value) { parent = value; }
    
    public int indexOfName(String segmentName) {
        int r = -1;
        
        for (int i=0; (i<size()) && (r == -1); i++) {
            if (get(i).size() != 0) {
                if (segmentName.equals(get(i).get(0).toString())) {
                    r = i;
                }
            }
        }
        
        return r;
    }
    
    public Segment getSegment(String segName) {
        return (indexOfName(segName) == -1)?null:(Segment)get(indexOfName(segName));
    }
    
    @Override
    protected String toXmlString() {
        StringBuffer sb = new StringBuffer();
        
        String element = get(0).get(9).get(0).get(0).toString() + "_" + get(0).get(9).get(0).get(1).toString();
        
        sb.append("<" + element + ">\n");        
        
        for (int i=0; i<size(); i++) {
            sb.append(get(i).toXmlString());
        }
        
        sb.append("</" + element + ">\n");        
        
        return sb.toString();        
    }    
}
