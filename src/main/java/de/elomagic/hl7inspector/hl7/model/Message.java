/*
 * Copyright 2010 Carsten Rambow
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

import java.io.File;

import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class Message extends Hl7Object {
    private String source = "";
    private TreeNode parent;

    /** Creates a new instance of Message */
    public Message() {
    }

    @Override
    public char getSubDelimiter() {
        return (char)0x0d;
    }

    @Override
    public Class getChildClass() {
        return Segment.class;
    }

    public void setSource(String messageSource) {
        source = messageSource;
    }

    public String getSource() {
        return source;
    }

    public void setFile(File f) {
        try {
            source = f == null ? "" : f.toURI().toString();
        } catch(Exception ex) {
            source = "";
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
        }
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode value) {
        parent = value;
    }

    public int indexOfName(String segmentName) {
        for(int i = 0; i < size(); i++) {
            if(get(i).size() != 0) {
                if(segmentName.equals(get(i).get(0).toString())) {
                    return i;
                }
            }
        }

        return -1;
    }

    public Segment getSegment(String segName) {
        return indexOfName(segName) == -1 ? null : (Segment)get(indexOfName(segName));
    }
}
