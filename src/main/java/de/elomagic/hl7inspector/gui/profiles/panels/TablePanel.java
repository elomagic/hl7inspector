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

import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.gui.profiles.*;
import de.elomagic.hl7inspector.gui.profiles.model.TableModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.TableItemList;

/**
 *
 * @author rambow
 */
public class TablePanel extends ProfileTablePanel {
    
    /** Creates a new instance of TablePane */
    public TablePanel(PanelDialog d) { super(d); }
    
    protected void init() {
        model = new TableModel();
        
        super.init();              
    }   
    
    public void write(Profile profile) {
        TableModel model = (TableModel)getModel();
        
        TableItemList list = new TableItemList();
        
        for (int i=0; i<model.getRowCount(); i++) {
            list.addTableItem(model.getTableItem(i));
        }
        
       profile.setTableItemList(list); 
    }
    
    public void read(Profile profile) {
        ((TableModel)model).setModel(profile.getTableItemList());
    }
    public String getTitle() { return "Tables"; }
    
    public javax.swing.Icon getIcon() { return ResourceLoader.loadImageIcon("x-profile-data-editor.png", ResourceLoader.LARGE_IMAGE); }
    
    public String getDescription() { return ""; }    
}
