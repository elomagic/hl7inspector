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
import com.sun.media.sound.Toolkit;
import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.autoupdate.UpdateChecker;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author rambow
 */
public class AboutDialog extends BaseDialog {
    
    /** Creates a new instance of UpdateCheckDialog */
    public AboutDialog() {
        super(Desktop.getInstance());
        
        init();        
    }
    
        
    private void init() {
        getBanner().setVisible(false);
        
        setDialogMode(BaseDialog.CLOSE_DIALOG);        
        setTitle("About Dialog");
        setResizable(false);
        
        setLayout(new BorderLayout(4, 4));
        
        lblLogo.setPreferredSize(new Dimension(64, 64));
        lblCompany  = new JLabel("elomagic");
        lblName     = new JLabel("HL7 Inspector");
        lblName.setFont(lblName.getFont().deriveFont(new Float(24)));
        
        lblVersion  = new JLabel("Version: ".concat(Hl7Inspector.getVersionString()));
        lblLicense  = new JLabel("Developed under GNU Public License (GPL)");
        lblContact  = new JLabel("Contact: hl7inspector@elomagic.de");
        
        FormLayout layout = new FormLayout(
                "p:grow, p, 8dlu, p:grow",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, p:grow");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
        builder.add(lblLogo,        cc.xywh(2, 1, 1, 7));
        
        // 2nd row
        builder.add(lblCompany,     cc.xy(4, 1));
        
        builder.add(lblName,        cc.xy(4, 3));

        builder.add(lblVersion,     cc.xy(4, 5));

        builder.add(lblLicense,     cc.xy(4, 7));

        builder.add(lblContact,     cc.xy(4, 9));
                
        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        
        // Button pan
        
        pack();
        
        //setSize(300, getPreferredSize()!=null?getPreferredSize().height:230);
        setSize(getPreferredSize());
        
        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }
    
    private JLabel          lblLogo     = new JLabel(ResourceLoader.loadImageIcon("64x64/hl7inspector.png"));
    private JLabel          lblCompany;
    private JLabel          lblName;
    private JLabel          lblVersion;
    private JLabel          lblLicense;    
    private JLabel          lblContact;
}
