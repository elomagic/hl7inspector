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

import com.l2fprod.common.swing.BaseDialog;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.ToolKit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 *
 * @author rambow
 */
public class ArchiveFileSelectDialog extends BaseDialog implements MouseListener {
    
    /** Creates a new instance of ProfileRegistrationDialog */
    public ArchiveFileSelectDialog(ZipFile file) { 
        super(Desktop.getInstance(), "Choose File", true);         
        
        init(); 
        
        getBanner().setTitle("There is more the one file in the compressed file. Please choose file");
        // TODO Sortable ZipEntryModel
        
        tblFiles.setModel(new ZipEntryModel(file));
    }
    
    private void init() {
        getBanner().setTitle("Select files");
        getBanner().setVisible(true);
        getButtonPane().setVisible(true);
        
        tblFiles = new JTable();        
        tblFiles.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFiles.addMouseListener(this);
        JScrollPane scroll = new JScrollPane(tblFiles);       
        
        getContentPane().add(scroll);
       
        setSize(600, 400);
        
        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }
    
    public Object getSelectedObject() {
        int row = tblFiles.getSelectedRow();        
        return (row == -1)?null:((VectorTableModel)tblFiles.getModel()).getRow(row);        
    }
    
    public ZipEntry getSelectedEntry() {
        Object o = getSelectedObject();        
        return (o instanceof ZipEntry)?(ZipEntry)o:null;
    }
    
    private JTable  tblFiles;

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) { }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) { }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) { }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) { }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) { ok(); }
    }
}