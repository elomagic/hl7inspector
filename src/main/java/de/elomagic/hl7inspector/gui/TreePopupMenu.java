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
package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.gui.actions.*;
import de.elomagic.hl7inspector.hl7.model.EncodingObject;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import java.util.List;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author rambow
 */
public class TreePopupMenu extends JPopupMenu implements PopupMenuListener {
    private JCheckBoxMenuItem miCompactView = new JCheckBoxMenuItem(new ViewCompressedAction());
    private JCheckBoxMenuItem miNodeDescription = new JCheckBoxMenuItem(new ViewNodeDescriptionAction());
    private JCheckBoxMenuItem miNodeDetails = new JCheckBoxMenuItem(new ViewNodeDetailsAction());
    private JCheckBoxMenuItem miTraceWindow = new JCheckBoxMenuItem(new ShowParserWindowAction());
    private JCheckBoxMenuItem miReceiveWindow = new JCheckBoxMenuItem(new ShowReceiveWindowAction(true));
    private JCheckBoxMenuItem miSendWindow = new JCheckBoxMenuItem(new ShowSendWindowAction(true));

    public TreePopupMenu() {
        init();
    }

    protected final void init() {
        addPopupMenuListener(this);
    }

    private void createMenusItems() {
        removeAll();

        List<Hl7Object> selectedList = Desktop.getInstance().getSelectedObjects();
        if(!selectedList.isEmpty()) {
            Hl7Object hl7o = selectedList.get(0);

            if(!(hl7o instanceof EncodingObject)) {
                if(!(hl7o instanceof Message)) {
                    add(new JMenuItem(new EditMessageItemAction(hl7o)));
                }

                if(hl7o.getChildClass() != null) {
                    add(new JMenuItem(new AddMessageItemAction(hl7o.getChildClass())));
                }

                if(!(hl7o instanceof Message)) {
                    add(new JMenuItem(new RemoveMessageItemAction()));
                }

                add(new JMenuItem(new RemoveMessageAction()));

                addSeparator();
            }
        }

        add(new JMenuItem(new FileSaveAsAction()));
        add(new JMenuItem(new PasteTextAction()));

        addSeparator();
        add(miSendWindow);

        addSeparator();
        add(miCompactView);
        add(miNodeDescription);
        add(miNodeDetails);

        DesktopIntf d = Desktop.getInstance();
        miCompactView.setSelected(d.isCompressedView());
        miNodeDescription.setSelected(d.isNodeDescriptionVisible());
        miNodeDetails.setSelected(d.isNodeDetailsWindowVisible());
        miTraceWindow.setSelected(d.isInputTraceWindowVisible());
        miReceiveWindow.setSelected(d.isReceiveWindowVisible());
        miSendWindow.setSelected(d.isSendWindowVisible());
    }

    // Interface PopupMenuListener
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        createMenusItems();
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }
}
