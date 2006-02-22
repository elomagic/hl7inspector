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
import de.elomagic.hl7inspector.gui.AbstractPanel;
import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.profiles.*;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.utils.StringVector;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author rambow
 */
public class CommonPanel extends ProfilePanel {
    
    /** Creates a new instance of CommonPane */
    public CommonPanel(PanelDialog d) { super(d); }
    
    protected void init() {
        editName            = new JTextField();
        edtDesc             = new JTextField();
        lbLastUpdateDate    = new JLabel();
        lbLastUpdateDate.setFont(lbLastUpdateDate.getFont().deriveFont(Font.BOLD));
        
        FormLayout layout = new FormLayout(
                "p, 4dlu, p:grow",
                "p, 3dlu, p, 3dlu, p");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
        builder.addLabel("Name",            cc.xy(1,   1));
        builder.add(editName,                cc.xy(3,   1));

        builder.addLabel("Description:",    cc.xy(1,   3));
        builder.add(edtDesc,                cc.xy(3,   3));
        
        builder.addLabel("Created with:",    cc.xy(1,   5));
        builder.add(lbLastUpdateDate,        cc.xy(3,   5));        
        
        add(builder.getPanel(), BorderLayout.NORTH);
        
        
        // ***************
        
        JButton btValidate = new JButton(new ValidateProfileAction());        
        
        lbValidateStatus = new JLabel();
        lbValidateStatus.setBorder(new EmptyBorder(3, 3, 3, 3));
        lbValidateStatus.setOpaque(true);
        lbValidateStatus.setBackground(SystemColor.info);
        lbValidateStatus.setForeground(SystemColor.infoText);

        layout = new FormLayout(
                "p, 4dlu, p:grow",
                "p, 8dlu, p, p:grow");   // rows
        
        builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        cc = new CellConstraints();
        
        // 1st row
        builder.add(btValidate,             cc.xy(1,   1));

        builder.add(lbValidateStatus,                cc.xyw(1,   3, 3));
        
        add(builder.getPanel(), BorderLayout.CENTER);
    }
        
    private JTextField  editName;
    private JTextField  edtDesc;
    private JLabel      lbLastUpdateDate;
    
    private JLabel      lbValidateStatus;
    
    public void resetValidateStatus() {
        lbValidateStatus.setText("Validate status unknown. Press 'Validate' button.");
        lbValidateStatus.setIcon(ResourceLoader.loadImageIcon("warning.png"));        
    }
    
    public void setValidateStatus(boolean valid) {
        lbValidateStatus.setText((valid)?"The profile is valid":"The profile includes errors. Press 'Validate' to show the list of errors.");        
        lbValidateStatus.setIcon(ResourceLoader.loadImageIcon((valid)?"ok.png":"warning.png"));
    }
    
    public void write(Profile profile) {
        profile.setDescription(edtDesc.getText());        
        profile.setName(editName.getText());        
    }
    
    public void read(Profile profile) {
        editName.setText(profile.getName());
        edtDesc.setText(profile.getDescription());
        lbLastUpdateDate.setText(profile.getSchemaVersion());
    }
    
    public String getTitle() { return "Common"; }
    
    public javax.swing.Icon getIcon() { return ResourceLoader.loadImageIcon("info.png", ResourceLoader.LARGE_IMAGE); } 
    
    public String getDescription() { return ""; }
    
    
    class ValidateProfileAction extends AbstractAction {
        public ValidateProfileAction() { super("Validate profile"); }
        
        public void actionPerformed(ActionEvent e) {
            PanelDialog dlg = getDialog();
            
            Vector<AbstractPanel> list = dlg.getPanelList();
            
            Profile p = new Profile();
            
            for (int i=0; i<list.size(); i++) {
                if (list.get(i) instanceof ProfilePanel) {
                    ((ProfilePanel)list.get(i)).write(p);
                }
            }
            
            StringVector val = p.validate();
            
            setValidateStatus(val.size() == 0);
                                               
            if (val.size() != 0) {
                SimpleDialog.warn("List of invalid profile entries", val.toString((char)10));            
            }            
        }                    
    }
}
