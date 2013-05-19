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
 */
package de.elomagic.hl7inspector.gui.actions;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.FindBar;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.TreeNodeSearchEngine;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class FindNextAction extends BasicAction {
    /**
     * Creates a new instance of FindNextAction.
     */
    public FindNextAction() {
        super();

        putValue(NAME, "Find next");
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("go-next.png"));
        putValue(SHORT_DESCRIPTION, "Find next occurrences of the phrase ");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Desktop d = Desktop.getInstance();
        FindBar f = FindBar.getInstance();

        try {
            if(d.getTree().getModel().getRoot() != null) {
                String phrase = f.getEscapedPhrase();
                boolean cs = f.isCaseSensitive();
                if(!phrase.isEmpty() && (d.getTree().getModel().getChildCount(d.getTree().getModel().getRoot()) != 0)) {
                    TreeNode startingNode = (TreeNode)((d.getTree().getSelectionPath() != null) ? d.getTree().getSelectionPath().getLastPathComponent() : d.getTree().getModel().getRoot());
                    TreePath path = TreeNodeSearchEngine.findNextNode(phrase, cs, startingNode);

                    if(path == null) {
                        SimpleDialog.info("The end of message tree reached.");
                    } else {
                        d.getTree().expandPath(path.getParentPath());

                        int row = d.getTree().getRowForPath(path);

                        d.getTree().scrollRowToVisible(row);
                        d.getTree().setSelectionRow(row);
                    }
                }
            }
        } catch(Exception ex) {
            SimpleDialog.error(ex.getMessage());
        }
    }
}
