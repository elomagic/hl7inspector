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
import de.elomagic.hl7inspector.gui.profiles.model.DataElementModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.DataElementMap;
import de.elomagic.hl7inspector.profile.Profile;

/**
 *
 * @author rambow
 */
public class DataElementPanel extends ProfileTablePanel {
    
    /** Creates a new instance of FileIdPane */
    public DataElementPanel(PanelDialog d) { super(d); }
    
    protected void init() {
        model = new DataElementModel();
        
        super.init();        
    }
        
    public void write(Profile profile) {
        DataElementModel model = (DataElementModel)getModel();
        
        DataElementMap list = new DataElementMap();
        
        for (int i=0; i<model.getRowCount(); i++) {
            list.addDataElement(model.getDataElement(i));
        }
        
       profile.setDataElementList(list); 
    }
    
    public void read(Profile profile) {
        ((DataElementModel)model).setModel(profile.getDataElementList());
    }
    
    public String getTitle() { return "Data Elements"; }
    
    public javax.swing.Icon getIcon() { return ResourceLoader.loadImageIcon("x-profile-data-editor.png", ResourceLoader.LARGE_IMAGE); }
    
    public String getDescription() { return ""; }
}
