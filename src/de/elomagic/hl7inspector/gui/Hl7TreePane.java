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

import de.elomagic.hl7inspector.gui.actions.FileRecentOpenAction;
import de.elomagic.hl7inspector.model.Hl7Tree;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.tree.*;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class Hl7TreePane extends JScrollPane implements DropTargetListener  {
    
    /** Creates a new instance of Hl7Tree */
    public Hl7TreePane() { init(null); }
    
    public Hl7TreePane(Hl7TreeModel model) { init(model); }
    
    public void init(Hl7TreeModel model) {
        tree = (model == null)?new Hl7Tree():new Hl7Tree(model);
        tree.setCellRenderer(new TreeCellRenderer());
        tree.setOpaque(false);
        
        new DropTarget(tree, this);
        
        setViewport(new ImageBackground());
        setViewportView(tree);
    }
    
    public void setModel(TreeModel model) { tree.setModel(model); }
    
    public TreeModel getModel() { return tree.getModel(); }
    
    public Hl7Tree getTree() { return tree; }
    
    private Hl7Tree tree;
    
    // Interface implementationof drag an drop
    public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
        try {
            Transferable tr = dtde.getTransferable();
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                
                java.util.List fileList = (java.util.List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator iterator = fileList.iterator();
                while (iterator.hasNext()) {
                    File file = (File)iterator.next();
                    
                    Desktop.getInstance().toFront();
                    
                    //SimpleDialog.info(file.getPath());
                    
                    new FileRecentOpenAction(file).actionPerformed(null);
                }
                
                //      if (dtde.getCurrentDataFlavorsAsList().size() == 1) {
                //          SimpleDialog.info(dtde.getCurrentDataFlavors()[0].getHumanPresentableName());
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            dtde.rejectDrop();
        }
    }
    
    public void dragExit(java.awt.dnd.DropTargetEvent dte) { }
    
    public void dropActionChanged(java.awt.dnd.DropTargetDragEvent dtde) { }
    
    public void dragOver(java.awt.dnd.DropTargetDragEvent dtde) { /* SimpleDialog.info(dtde.toString()); */ }
    
    public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
        try {
            Transferable tr = dtde.getTransferable();
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                
                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
                
                java.util.List fileList = (java.util.List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                Iterator iterator = fileList.iterator();
                while (iterator.hasNext()) {
                    /*File file = (File)*/iterator.next();
                }
//            } else if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//                dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
            } else
                dtde.rejectDrag();
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            dtde.rejectDrag();
        }
    }
}
