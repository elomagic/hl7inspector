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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;
import de.elomagic.hl7inspector.StartupProperties;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author rambow
 */
public class HL7ObjectEditor extends BaseDialog {
    
    /** Creates a new instance of ImportOptionsDialog */
    public HL7ObjectEditor() {
        super(Desktop.getInstance());
        
        init();
    }   
    
    private void init() {        
        getBanner().setVisible(false);
        
        setTitle("Hl7 Editor");
        //setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        
        editValue       = new JTextField();
        cbEncode        = new JCheckBox();        
                
        FormLayout layout = new FormLayout(
                "p, 4dlu, p:grow",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p:grow");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
//        builder.add(new GradientLabel("Source"),        cc.xyw(1,   1,  13));
        
        // 2nd row
        builder.addLabel("Value:",          cc.xy(1,   3));      // Ok
        builder.add(editValue,              cc.xy(3,   3));
        
        // 3rd row
        builder.addLabel("Encode:",         cc.xy(1,   5));        // Ok
        builder.add(cbEncode,               cc.xy(3,   5));        
        
        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        
        pack();
        
        setSize(400, getSize().height);
        
        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }
    
    private JTextField      editValue;
    private JCheckBox       cbEncode;    
    
    public boolean isEncode() { return cbEncode.isSelected(); }
    public String getValue() { return editValue.getText(); }
    public void setValue(String value) { editValue.setText(value); }    
}
