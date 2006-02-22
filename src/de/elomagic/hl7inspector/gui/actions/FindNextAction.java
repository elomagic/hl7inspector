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
import de.elomagic.hl7inspector.gui.FindBar;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class FindNextAction extends AbstractAction {
    
    /** Creates a new instance of ExitAction */
    public FindNextAction() {
        super("Find next", ResourceLoader.loadImageIcon("go-next.png"));
        
        putValue(SHORT_DESCRIPTION, "Find next occurrences of the phrase ");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
    }
    
    public void actionPerformed(ActionEvent e) {
        Desktop d = Desktop.getInstance();
        FindBar f = FindBar.getInstance();
                
        if (d.getTree().getModel().getRoot() != null) {
            String phrase = f.getEscapedPhrase();
            if ((phrase.length() != 0) && (d.getTree().getModel().getChildCount(d.getTree().getModel().getRoot()) != 0)) {
                int row = d.getTree().getSelectionModel().getLeadSelectionRow()+1;
                
                TreePath path = null;
                if (row < d.getTree().getRowCount()) {
                    path = d.getTree().findNextNode(phrase, row, true);
                }

                if (path == null) 
                    SimpleDialog.info("The end of message tree reached.");
                else {
                    int pr = d.getTree().getRowForPath(path.getParentPath());
                    row = d.getTree().getRowForPath(path);

                    d.getTree().expandRow(pr);
                    d.getTree().scrollRowToVisible(row);
                    d.getTree().setSelectionRow(row);
                }
            }
        }
    }
    
}
