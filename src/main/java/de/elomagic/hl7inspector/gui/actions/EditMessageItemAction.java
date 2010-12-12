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
import de.elomagic.hl7inspector.hl7.model.Delimiters;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class EditMessageItemAction extends AbstractAction {
    
    /** Creates a new instance of FileNewAction */
    public EditMessageItemAction(Hl7Object o) {
        super("Edit selected " + getObjectDescription(o));
        
        init();
    }
    
    public EditMessageItemAction() {
        super("Edit selected node");
        
        init();
    }    
    
    private void init() {        
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("edit.png"));
        putValue(SHORT_DESCRIPTION, "Edit selected node");
//        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
    }
    
    private static String getObjectDescription(Hl7Object o) {
        String s = o.getClass().getName();
        s = s.substring(s.lastIndexOf(".")+1);
        return s.toLowerCase();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        TreePath path = Desktop.getInstance().getTree().getSelectionPath();            
        Hl7Object hl7o = (Hl7Object)path.getLastPathComponent();        
        
        HL7ObjectEditor editor = new HL7ObjectEditor();        
        editor.setValue(hl7o);        
        if (editor.ask()) {            
            hl7o.parse(editor.getValue(new Delimiters()));
            
            Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getTree().getModel();
            model.fireTreeStructureChanged(path);
        }
    }
}
