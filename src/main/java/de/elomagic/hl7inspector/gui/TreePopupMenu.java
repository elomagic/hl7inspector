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
package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.gui.actions.*;
import de.elomagic.hl7inspector.hl7.model.EncodingObject;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class TreePopupMenu extends JPopupMenu implements PopupMenuListener {

    public TreePopupMenu() {
        init();
    }

    protected final void init() {
        addPopupMenuListener(this);
    }

    private void createMenusItems() {
        removeAll();

        TreePath selPath = Desktop.getInstance().getTree().getSelectionPath();
        if (selPath != null) {
            if (selPath.getLastPathComponent() instanceof Hl7Object) {
                Hl7Object hl7o = (Hl7Object) selPath.getLastPathComponent();

                if (!(hl7o instanceof EncodingObject)) {
                    if (!(hl7o instanceof Message)) {
                        add(new JMenuItem(new EditMessageItemAction(hl7o)));
                    }

                    if (hl7o.getChildClass() != null) {
                        add(new JMenuItem(new AddMessageItemAction(hl7o.getChildClass())));
                    }

                    if (!(hl7o instanceof Message)) {
                        add(new JMenuItem(new ClearMessageItemAction()));
                    }

                    addSeparator();
                }
            }
        }

        add(new JMenuItem(new FileSaveAsAction()));
        add(new JMenuItem(new PasteTextAction()));

//        JMenu miFile = new JMenu("File");
//        miFile.add(new JMenuItem(new FileNewAction()));
//        miFile.add(new JMenuItem(new FileOpenAction()));
//        miFile.add(miOpenRecentFiles);
        addSeparator();
        add(miSendWindow);

        addSeparator();
        add(miCompactView);
        add(miNodeDescription);
        add(miNodeDetails);

        /* FEATURE Print message support needed
        menuItem.addSeparator();
        menuItem.add(new JMenuItem(new PrintAction())); */


        Hl7TreeModel model = (Hl7TreeModel) Desktop.getInstance().getTree().getModel();

        miCompactView.setSelected(model.isCompactView());
        miNodeDescription.setSelected(model.isViewDescription());
        miNodeDetails.setSelected(Desktop.getInstance().getDetailsWindow().isVisible());
        miParseWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getInputTraceWindow()) != -1);
        miReceiveWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getReceiveWindow()) != -1);
        miSendWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getSendWindow()) != -1);
    }

    private JCheckBoxMenuItem miCompactView = new JCheckBoxMenuItem(new ViewCompressedAction());

    private JCheckBoxMenuItem miNodeDescription = new JCheckBoxMenuItem(new ViewNodeDescriptionAction());

    private JCheckBoxMenuItem miNodeDetails = new JCheckBoxMenuItem(new ViewNodeDetailsAction());

    private JCheckBoxMenuItem miParseWindow = new JCheckBoxMenuItem(new ShowParserWindowAction());

    private JCheckBoxMenuItem miReceiveWindow = new JCheckBoxMenuItem(new ReceiveMessageAction());

    private JCheckBoxMenuItem miSendWindow = new JCheckBoxMenuItem(new SendMessageAction());
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
