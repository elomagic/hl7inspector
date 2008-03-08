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
import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
        setResizable(true);
        
        setLayout(new BorderLayout());        
        
        JTabbedPane tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        // Start with tabbed page 1
        
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
        
        JPanel p = builder.getPanel();        
        int h = p.getPreferredSize().height;
        
        tabbedPane.add("About", p);
        
        // Start with tabbed page 2
        
        layout = new FormLayout(
                "p, 8dlu, p:grow",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, p:grow");   // rows
        
        builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        
        cc = new CellConstraints();
        
        // 1st row
        JLabel title = new JLabel("HL7 Inspector Product Information");
        title.setFont(Font.decode("Arial"));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        title.setFont(title.getFont().deriveFont(Float.parseFloat("20")));
        
        builder.add(title,        cc.xyw(1, 1, 3));
        
        // 2nd row
        builder.addLabel("Product Version:",                                    cc.xy(1, 3));
        builder.add(new JLabel(Hl7Inspector.getVersionString()),                cc.xy(3, 3));
        
        builder.addLabel("Operation System:",                                   cc.xy(1, 5));
        builder.add(new JLabel(getProperty("os.name")+" "+getProperty("os.version")+" running on "+getProperty("os.arch")),        cc.xy(3, 5));

        builder.addLabel("Java:",                                               cc.xy(1, 7));
        builder.add(new JLabel(getProperty("java.version")),   cc.xy(3, 7));

        builder.addLabel("VM:",                                                 cc.xy(1, 9));
        builder.add(new JLabel(getProperty("java.vm.name")+" "+getProperty("java.vm.version")),     cc.xy(3, 9));

        builder.addLabel("Vendor:",                                             cc.xy(1, 11));
        builder.add(new JLabel(getProperty("java.vendor")),    cc.xy(3, 11));
                               
//        builder.addLabel("System Locale:",                                    cc.xy(1, 13));
//        builder.add(new JLabel(System.getProperty("java.version", "Unkown")),     cc.xy(3, 13));
        
        p = builder.getPanel();        
        tabbedPane.add("Details", p);

        pack();
        
        //setSize(300, getPreferredSize()!=null?getPreferredSize().height:230);
        setSize(getPreferredSize().width, 300);
        
        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }
    
    private String getProperty(String key) { return System.getProperty(key, "Unknown"); }
    
    private JLabel          lblLogo     = new JLabel(ResourceLoader.loadImageIcon("64x64/hl7inspector.png"));
    private JLabel          lblCompany;
    private JLabel          lblName;
    private JLabel          lblVersion;
    private JLabel          lblLicense;    
    private JLabel          lblContact;
}
