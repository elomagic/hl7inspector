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
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.RepetitionField;
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
public class ClearMessageItemAction extends AbstractAction {
    
    /** Creates a new instance of FileNewAction */
    public ClearMessageItemAction() {
        super("Clear selected node(s)");
        
        init();
    }
    
    private void init() {
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("edit-clear.png"));
        putValue(SHORT_DESCRIPTION, "Delete selected node(s)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }
    
//    private String getObjectDescription(Hl7Object o) {
//        String s = o.getClass().getName();
//        s = s.substring(s.lastIndexOf(".")+1);
//        return s;
//    }
    
    public void actionPerformed(ActionEvent e) {
        int selCount = Desktop.getInstance().getTree().getSelectionCount();
        
        if (selCount == 0) {
            SimpleDialog.info("No node selected.");
        } else if (selCount > 1){
            SimpleDialog.error("Only one selected node can clear.");
        } else {
            if (SimpleDialog.confirmYesNo("Clear selected node(s)?") == SimpleDialog.YES_OPTION) {                
                
                TreePath path = Desktop.getInstance().getTree().getSelectionPath();
                Hl7Object hl7o = (Hl7Object)path.getLastPathComponent();
                hl7o.clear();
                if ((hl7o.getHl7Parent() instanceof RepetitionField) && (hl7o.getHl7Parent().getChildCount() == 1)) {
                    hl7o.getHl7Parent().clear();
                }
                
                Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getTree().getModel();
                
                if (model.isCompactView()) {
                    model.fireTreeStructureChanged(path.getParentPath());
                }
            }
        }
    }
}
