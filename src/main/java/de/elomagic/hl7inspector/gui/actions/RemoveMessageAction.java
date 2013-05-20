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
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.mac.MacApplication;
import de.elomagic.hl7inspector.model.Hl7TreeModel;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class RemoveMessageAction extends BasicAction {
    /**
     * Creates a new instance of RemoveMessageAction.
     */
    public RemoveMessageAction() {
        super();

        putValue(NAME, bundle.getString("remove_selected_messages"));
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("edit-clear.png"));
        putValue(SHORT_DESCRIPTION, bundle.getString("remove_selected_messages_description"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, MacApplication.isMacOS() ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selCount = Desktop.getInstance().getTree().getSelectionCount();

        if(selCount == 0) {
            SimpleDialog.info(bundle.getString("no_message_selected"));
        } else {
            if(SimpleDialog.confirmYesNo(bundle.getString("remove_selected_message_ask")) == SimpleDialog.YES_OPTION) {
                List<Message> messageList = new ArrayList<>();
                TreePath[] paths = Desktop.getInstance().getTree().getSelectionPaths();
                for(TreePath tp : paths) {
                    if(tp.getPathCount() > 1) {
                        Message message = (Message)tp.getPathComponent(1);

                        if(!messageList.contains(message)) {
                            messageList.add(message);
                        }
                    }
                }

                Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getTree().getModel();

                for(Message message : messageList) {
                    model.remove(message);
                }

                if(model.isCompactView()) {
                    model.fireTreeStructureChanged(model.getRoot());
                }
            }
        }
    }
}
