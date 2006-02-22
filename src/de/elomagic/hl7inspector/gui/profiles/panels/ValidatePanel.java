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
import de.elomagic.hl7inspector.gui.GradientLabel;
import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.gui.profiles.*;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.validate.ValidateStatus;
import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 *
 * @author rambow
 */
public class ValidatePanel extends ProfilePanel {
    
    /** Creates a new instance of CommonPane */
    public ValidatePanel(PanelDialog d) { super(d); }
    
    protected void init() {
        Vector<ValidateStatus> v = new Vector<ValidateStatus>();
        v.add(new ValidateStatus(ValidateStatus.OK));
        v.add(new ValidateStatus(ValidateStatus.INFO));
        v.add(new ValidateStatus(ValidateStatus.WARN));
        v.add(new ValidateStatus(ValidateStatus.ERROR));
        
        cbLength        = new JComboBox(v);
        cbDeprecated    = new JComboBox(v);
        cbConditional   = new JComboBox(v);
        cbRequired      = new JComboBox(v);
        cbItemInTable   = new JComboBox(v);
        cbDefNotFound   = new JComboBox(v);
        cbRepetition    = new JComboBox(v);
        
        FormLayout layout = new FormLayout(
                "8dlu, p, 4dlu, p, p:grow",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, " +
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, " +
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
        builder.add(new GradientLabel("Validate Mapping"),    cc.xyw(1,  1, 5));
        
        builder.addLabel("Field, component, subcomponent length too large:",                 cc.xy(2,   3));      // Ok
        builder.add(cbLength,                       cc.xy(4,   3));
        
        builder.addLabel("Item not found:",         cc.xy(2,   5));      // Ok
        builder.add(cbItemInTable,                  cc.xy(4,   5));
        
        builder.addLabel("Deprecated field, component, subcomponent:",       cc.xy(2,   7));      // Ok
        builder.add(cbDeprecated,                   cc.xy(4,   7));
        
        builder.addLabel("Conditional field, component, subcomponent:",      cc.xy(2,   9));      // Ok
        builder.add(cbConditional,                  cc.xy(4,   9));

        builder.addLabel("Required field, component, subcomponent:",         cc.xy(2,   11));      // Ok
        builder.add(cbRequired,                     cc.xy(4,   11));

        builder.addLabel("Definition not found:",   cc.xy(2,   13));      // Ok
        builder.add(cbDefNotFound,                  cc.xy(4,   13));

        builder.addLabel("Repetition count overflow:",    cc.xy(2,   15));      // Ok
        builder.add(cbRepetition,                   cc.xy(4,   15));

        add(builder.getPanel(), BorderLayout.CENTER);
    }
        
    private JComboBox   cbLength;
    private JComboBox   cbDeprecated;
    private JComboBox   cbConditional;
    private JComboBox   cbRequired;
    private JComboBox   cbItemInTable;
    private JComboBox   cbDefNotFound;
    private JComboBox   cbRepetition;    
    
    public void write(Profile profile) {
        profile.getValidateMapper().setMapLength(cbLength.getSelectedIndex());
        profile.getValidateMapper().setMapDeprecated(cbDeprecated.getSelectedIndex());
        profile.getValidateMapper().setMapConditional(cbConditional.getSelectedIndex());
        profile.getValidateMapper().setMapRequired(cbRequired.getSelectedIndex());
        profile.getValidateMapper().setMapItemMiss(cbItemInTable.getSelectedIndex());
        profile.getValidateMapper().setMapRepetition(cbRepetition.getSelectedIndex());        
    }
    
    public void read(Profile profile) {
        cbLength.setSelectedIndex(profile.getValidateMapper().getMapLength());
        cbDeprecated.setSelectedIndex(profile.getValidateMapper().getMapDeprecated());
        cbConditional.setSelectedIndex(profile.getValidateMapper().getMapConditional());
        cbRequired.setSelectedIndex(profile.getValidateMapper().getMapRequired());
        cbItemInTable.setSelectedIndex(profile.getValidateMapper().getMapItemMiss());
        cbDefNotFound.setSelectedIndex(profile.getValidateMapper().getMapDefNotFound());
        cbRepetition.setSelectedIndex(profile.getValidateMapper().getMapRepetition());
    }
    
    public String getTitle() { return "Validator"; }
    
    public javax.swing.Icon getIcon() { return ResourceLoader.loadImageIcon("spellcheck.png", ResourceLoader.LARGE_IMAGE); } 
    
    public String getDescription() { return ""; }
}
