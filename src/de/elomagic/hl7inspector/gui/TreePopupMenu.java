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
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author rambow
 */
public class TreePopupMenu extends JPopupMenu implements PopupMenuListener {
    
    public TreePopupMenu() { init(); }
    
    protected void init() { addPopupMenuListener(this); }
    
    private void createMenusItems() {
        removeAll();
        
        TreePath selPath = Desktop.getInstance().getTree().getSelectionPath();
        Hl7Object hl7o = (selPath!= null)?(Hl7Object)(selPath.getLastPathComponent()):null;
        
        if (hl7o != null) {
            add(new JMenuItem(new EditMessageItemAction(hl7o)));
            
            if (hl7o.getNewClientInstance() != null) {
                add(new JMenuItem(new AddMessageItemAction(hl7o.getNewClientInstance().getClass())));
            }
            add(new JMenuItem(new DeleteMessageItemAction()));
            
            addSeparator();
        }
        
        add(new JMenuItem(new FileSaveAsAction()));
        
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
        
        
        Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getTree().getModel();
        
        miCompactView.setSelected(model.isCompactView());
        miNodeDescription.setSelected(model.isViewDescription());
        miNodeDetails.setSelected(Desktop.getInstance().getDetailsWindow().isVisible());
        miParseWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getInputTraceWindow()) != -1);
        miReceiveWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getReceiveWindow()) != -1);
        miSendWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getSendWindow()) != -1);
    }
    
    private JCheckBoxMenuItem   miCompactView       = new JCheckBoxMenuItem(new ViewCompressedAction());
    private JCheckBoxMenuItem   miNodeDescription   = new JCheckBoxMenuItem(new ViewNodeDescriptionAction());
    private JCheckBoxMenuItem   miNodeDetails       = new JCheckBoxMenuItem(new ViewNodeDetailsAction());
    private JCheckBoxMenuItem   miParseWindow       = new JCheckBoxMenuItem(new ShowParserWindowAction());
    private JCheckBoxMenuItem   miReceiveWindow     = new JCheckBoxMenuItem(new ReceiveMessageAction());
    private JCheckBoxMenuItem   miSendWindow        = new JCheckBoxMenuItem(new SendMessageAction());
    
    // Interface PopupMenuListener
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) { createMenusItems(); }    
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }    
    public void popupMenuCanceled(PopupMenuEvent e) { }
}