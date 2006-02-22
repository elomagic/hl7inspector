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

package de.elomagic.hl7inspector.gui.profiles.panels;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.gui.profiles.*;
import de.elomagic.hl7inspector.gui.profiles.actions.*;
import de.elomagic.hl7inspector.gui.profiles.model.ProfileModel;
import de.elomagic.hl7inspector.gui.profiles.model.SortedTableModel;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author rambow
 */
public abstract class ProfileTablePanel extends ProfilePanel implements TableModelListener {
    
    /** Creates a new instance of DefaultProfilePane */
    public ProfileTablePanel(PanelDialog d) {
        super(d);
        
        init();
    }
    
    protected void init() {
        setSize(300, 500);
        
        SortedTableModel sortedModel = new SortedTableModel(model);               
        table       = new JTable();
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getTable().setModel(sortedModel);
        sortedModel.setTableHeader(getTable().getTableHeader());
        
        getModel().addTableModelListener(this);          
        //table.setModel(new VectorTableModel());
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        scroll      = new JScrollPane(table);
        btAdd       = new JButton(new AddItemAction(getTable()));
        btRemove    = new JButton(new DeleteItemAction(getTable()));
        btDeleteAll = new JButton(new DeleteAllItemsAction(getTable()));
        btImport    = new JButton(new ImportProfileAction(getTable()));
                        
        FormLayout layout = new FormLayout(
                "p:grow, 4dlu, p",
                "p, 4dlu, p, 4dlu, p, 4dlu, p, 8dlu, p , p:grow, p");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
        builder.add(scroll,                   cc.xywh(1,   1,  1, 11));
        builder.add(btAdd,                    cc.xy(3,   1));
        
        builder.add(btRemove,                 cc.xy(3,   3));
                
        builder.add(btRemove,                 cc.xy(3,   5));
        
        builder.add(btDeleteAll,              cc.xy(3,   7));

        builder.add(btImport,                 cc.xy(3,   9));
                
        add(builder.getPanel(), BorderLayout.CENTER);       
        
        //setSize(400, 300);                
    }
    
    public JTable getTable() { return table; }
    protected ProfileModel model;    
    
    public ProfileModel getModel() { return (ProfileModel)((SortedTableModel)getTable().getModel()).getTableModel(); }
    
    private JScrollPane scroll;
    private JTable      table;
    private JButton     btAdd;
    private JButton     btRemove;
    private JButton     btDeleteAll;
    private JButton     btImport;

    public void tableChanged(TableModelEvent e) {
        ((ProfileDefinitionDialog)getDialog()).getCommonPanel().resetValidateStatus();
    }
}
