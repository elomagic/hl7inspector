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

package de.elomagic.hl7inspector.gui.actions;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.HL7ObjectEditor;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class AddMessageItemAction extends AbstractAction {
    
    /** Creates a new instance of FileNewAction */    
    public AddMessageItemAction(Class c) {
        super("Insert " +  getObjectDescription(c) + ".");
        
        init(c);
    }

    
    private void init(Class cl) {
        c = cl;
        
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("edit_add.png"));
        putValue(SHORT_DESCRIPTION, "Add" + getObjectDescription(c) + ".");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
    }
    
    private final static String getObjectDescription(Class c) {
        String s = c.getName();
        s = s.substring(s.lastIndexOf(".")+1);
        return s;
    }
    
    private Class c;
    
    public void actionPerformed(ActionEvent e) {
        HL7ObjectEditor editor = new HL7ObjectEditor();
        
        //editor.setValue(hl7o.toString());
        
        if (editor.ask()) {
            // TODO Add node with given value
            TreePath path = Desktop.getInstance().getTree().getSelectionPath();            
            
            Hl7Object hl7o = (Hl7Object)path.getLastPathComponent();
            Hl7Object o = hl7o.add(editor.getValue());
            
            Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getTree().getModel();
            
            model.fireTreeNodesInsert(path, new Object[] { o });
            
//            Desktop.getInstance().getTree().repaint();
        }
    }
}
