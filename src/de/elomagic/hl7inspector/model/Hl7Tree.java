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

import de.elomagic.hl7inspector.gui.tooltip.ExtendedTooltip;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class Hl7Tree extends JTree implements MouseMotionListener {
    
    /** Creates a new instance of Hl7Tree */
    public Hl7Tree() { super(); init(); }
    
    public Hl7Tree(Hl7TreeModel model) { super(model); init(); }
    
    private void init() {
        setExpandsSelectedPaths(true);
        //setShowsRootHandles()
        addMouseMotionListener(this);
        
        // Enable ToolTiptext;
        setToolTipText("");
    }    
    
    public TreePath findNextNode(String pharse, int startingRow, boolean forward) {
        int max = getRowCount();
        if (pharse == null) {
            throw new IllegalArgumentException();
        }
        if (startingRow < 0 || startingRow >= max) {
            throw new IllegalArgumentException();
        }
        pharse = pharse.toUpperCase();
        
        // start search from the next/previous element froom the
        // selected element
        int increment = (forward) ? 1 : -1;
        int row = startingRow;
        do {
            TreePath path = getPathForRow(row);
            String text = convertValueToText(
                    path.getLastPathComponent(), isRowSelected(row),
                    isExpanded(row), true, row, false);
            
            if (text.toUpperCase().indexOf(pharse) != -1) {
                return path;
            }
            row = (row + increment + max) % max;
        } while (row != startingRow);
        return null;
    }
    
    public Vector<Message> getSelectedMessages() {
        Vector<Message> messages = new Vector<Message>();
        
        TreePath[] paths = getSelectionPaths();
        
        if (paths != null) {
            for (int i=0; i<paths.length; i++) {
                TreePath path = paths[i];
                
                Hl7Object node = (Hl7Object)path.getLastPathComponent();
                
                while (!(node instanceof Message))
                    node = node.getParent();
                
                Message message = (Message)node;
                
                if (messages.indexOf(message) == -1) {
                    messages.add(message);
                }
            }
        }
        
        return messages;
    }
    
    public JToolTip createToolTip() {
        ExtendedTooltip tip = new ExtendedTooltip();
        tip.setComponent(this);
        return tip;
    }
    
    public void mouseMoved(MouseEvent evt) {
/*        TreePath path = getPathForLocation(evt.getX(), evt.getY());
 
        if ((path != null) && (path.getLastPathComponent() instanceof Hl7Object)) {
            Hl7Object obj = (Hl7Object)path.getLastPathComponent();
 
            String tt = obj.getValidationText();
            setToolTipText(tt);
        }*/
        
    }
    
    public void mouseDragged(MouseEvent evt) { }
    
    public String getToolTipText(MouseEvent event) {
        String tt = "";
        TreePath path = getPathForLocation(event.getX(), event.getY());
        
        if ((path != null) && (path.getLastPathComponent() instanceof Hl7Object)) {
            Hl7Object obj = (Hl7Object)path.getLastPathComponent();
            
            tt = "";
            
            /*
            // Get profile Information
             
            if (obj.getDescription().length() != 0) {
                tt = obj.getDescription();
            }
             
            if ((obj.getDescription().length() != 0) && (obj.getValidationText().length() != 0)) {
                tt = tt.concat("\n---\n");
            }*/
            
            if (obj.getValidationText() != null) {
                tt = tt.concat(obj.getValidationText());
            }
            //setToolTipText(tt);
        }
        
        return (tt.length() == 0)?null:tt;
    }
    
}
