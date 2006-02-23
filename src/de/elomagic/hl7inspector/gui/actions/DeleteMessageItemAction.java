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

import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 *
 * @author rambow
 */
public class DeleteMessageItemAction extends AbstractAction {
    
    /** Creates a new instance of FileNewAction */
    public DeleteMessageItemAction() {
        super("Delete selected node(s)");
        
        init();
    }
    
    private void init() {
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("edit_remove.png"));
        putValue(SHORT_DESCRIPTION, "Delete selected node(s).");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }
    
//    private String getObjectDescription(Hl7Object o) {
//        String s = o.getClass().getName();
//        s = s.substring(s.lastIndexOf(".")+1);
//        return s;        
//    }
    
    public void actionPerformed(ActionEvent e) {
        if (SimpleDialog.confirmYesNo("Delete selected node(s)?") == SimpleDialog.YES_OPTION) {
            // TODO Delete selected nodes
        }
    }    
}
