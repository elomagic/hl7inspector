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

package de.elomagic.hl7inspector.gui.options;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.AbstractPanel;
import de.elomagic.hl7inspector.gui.GradientLabel;
import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author rambow
 */
public class TreeViewPanel extends AbstractPanel {
    
    /** Creates a new instance of GeneralOptionPane */
    public TreeViewPanel(PanelDialog d) { super(d); }
    
    public void init() {
        editViewMode        = new JComboBox(new String[] { "Plain HL7 message in root message node", "Compact HL7 message information in root message node"} );
        //editNodeLength      = new JTextField();
        btNodeLength        = new JCheckBox(new AbstractAction() {
            public void actionPerformed(ActionEvent e) { btSpinNodeLength.setEnabled(btNodeLength.isSelected()); }            
        });
        btSpinNodeLength        = new JSpinner();
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        editFontSample = new JTextField("Text sample. ABCDEF 0123456");
        editFontSample.setEditable(false);
                
        cbFont                  = new JComboBox(ge.getAvailableFontFamilyNames());
        cbFont.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editFontSample.setFont(Font.decode(cbFont.getSelectedItem().toString()));
            }
        });
        cbFont.setEditable(false);                
//        cbFont.setM
        
        FormLayout layout = new FormLayout(
                "8dlu, p,  4dlu, 60dlu, 4dlu, p:grow, 4dlu, p",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 7dlu, " +
                "p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
        builder.add(new GradientLabel("View Options"),  cc.xyw(1,  1, 6));
        
        builder.addLabel("View mode:",                  cc.xy(2,   3));      // Ok
        builder.add(editViewMode,                       cc.xyw(4,   3, 3));
        
        builder.addLabel("Node text limit:",            cc.xy(2,   5));      // Ok
        builder.add(btNodeLength,                       cc.xy(4,   5));

        builder.addLabel("Length:",                     cc.xy(2,   7));      // Ok
        builder.add(btSpinNodeLength,                   cc.xy(4,   7));
        
        builder.add(new GradientLabel("Font"),          cc.xyw(1,  9, 6));
        
        builder.addLabel("Name:",                       cc.xy(2,   11));      // Ok
        builder.add(cbFont,                             cc.xyw(4,   11, 3));        
        
        builder.addLabel("Example:",                    cc.xy(2,   13));      // Ok
        builder.add(editFontSample,                     cc.xyw(4,   13, 3));        
        
        add(builder.getPanel(), BorderLayout.CENTER);
    }
    
    public Icon getIcon() { return ResourceLoader.loadImageIcon("view_tree.png", ResourceLoader.LARGE_IMAGE); }
    
    public String getTitle() { return "Tree"; }
    
    public String getDescription() { return "Tree view selections"; }
    
    public void read() {
        StartupProperties p = StartupProperties.getInstance();
        
        editViewMode.setSelectedIndex(p.getTreeViewMode());
        btNodeLength.setSelected(p.getTreeNodeLength()!=0);
        btSpinNodeLength.setValue(new Integer(p.getTreeNodeLength()==0?200:p.getTreeNodeLength()));
        btSpinNodeLength.setEnabled(p.getTreeNodeLength()!=0);
        cbFont.setSelectedItem(p.getTreeFontName());
    }
    
    public void write() {
        StartupProperties p = StartupProperties.getInstance();
        
        p.setTreeViewMode(editViewMode.getSelectedIndex());
        p.setTreeNodeLength(btNodeLength.isSelected()?Integer.parseInt(btSpinNodeLength.getValue().toString()):0);
        p.setTreeFontName(cbFont.getSelectedItem().toString());
    }       
    
    private JComboBox   editViewMode;
    private JCheckBox   btNodeLength;
    private JSpinner    btSpinNodeLength;        
    private JComboBox   cbFont;
    private JTextField  editFontSample;
}
