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
import java.awt.Color;
import java.awt.Font;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTextField;

/**
 *
 * @author rambow
 */
public class FontsColorOptionPanel extends AbstractPanel {
    private JTextField lblColorNodePrefix;
    private JTextField lblNodeText;
    private JTextField lblNodeDescription;
    private JTextField lblNodeTruncate;
    private JButton btNodePrefix;
    private JButton btNodeText;
    private JButton btNodeDescription;
    private JButton btNodeTruncate;

    /**
     * Creates a new instance of GeneralOptionPanel.
     */
    public FontsColorOptionPanel(PanelDialog d) {
        super(d);
    }

    @Override
    public Icon getIcon() {
        return ResourceLoader.loadImageIcon("colorize.png", ResourceLoader.LARGE_IMAGE);
    }

    @Override
    public String getTitle() {
        return "Color";
    }

    @Override
    public String getDescription() {
        return "Color Options";
    }

    @Override
    public void init() {
        lblNodeText = new JTextField("MSH|^~\\&|");
        lblNodeText.setEditable(false);
        lblColorNodePrefix = new JTextField("<#1>");
        lblColorNodePrefix.setEditable(false);
        lblColorNodePrefix.setFont(lblColorNodePrefix.getFont().deriveFont(Font.BOLD));
        lblNodeDescription = new JTextField("Message Header");
        lblNodeDescription.setEditable(false);
        lblNodeDescription.setFont(lblNodeDescription.getFont().deriveFont(Font.BOLD));
        lblNodeTruncate = new JTextField("###");
        lblNodeTruncate.setEditable(false);
        lblNodeTruncate.setFont(lblNodeTruncate.getFont().deriveFont(Font.BOLD));

        btNodeText = new JButton(new ChangeColorAction(lblNodeText));
        btNodePrefix = new JButton(new ChangeColorAction(lblColorNodePrefix));
        btNodeDescription = new JButton(new ChangeColorAction(lblNodeDescription));
        btNodeTruncate = new JButton(new ChangeColorAction(lblNodeTruncate));

        FormLayout layout = new FormLayout(
                "p, 4dlu, p:grow, 20dlu",
                //            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, "
                + "p, 3dlu, p, 3dlu, p, p:grow");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
//        builder.addSeparator("Desktop",         cc.xyw(1,  1, 4));
        builder.add(new GradientLabel("Tree colors"), cc.xyw(1, 1, 4));      // Ok

        builder.addLabel("Node text:", cc.xy(1, 3));      // Ok
        builder.add(lblNodeText, cc.xy(3, 3));
        builder.add(btNodeText, cc.xy(4, 3));

        builder.addLabel("Node prefix:", cc.xy(1, 5));      // Ok
        builder.add(lblColorNodePrefix, cc.xy(3, 5));
        builder.add(btNodePrefix, cc.xy(4, 5));

        builder.addLabel("Node description:", cc.xy(1, 7));      // Ok
        builder.add(lblNodeDescription, cc.xy(3, 7));
        builder.add(btNodeDescription, cc.xy(4, 7));

        builder.addLabel("Node truncate string:", cc.xy(1, 9));      // Ok
        builder.add(lblNodeTruncate, cc.xy(3, 9));
        builder.add(btNodeTruncate, cc.xy(4, 9));

        add(builder.getPanel(), BorderLayout.CENTER);
    }

    @Override
    public void read() {
        StartupProperties p = StartupProperties.getInstance();

        lblNodeText.setForeground(p.getColor(StartupProperties.COLOR_NODE_TEXT));
        lblColorNodePrefix.setForeground(p.getColor(StartupProperties.COLOR_NODE_PREFIX));
        lblNodeDescription.setForeground(p.getColor(StartupProperties.COLOR_NODE_DESCRIPTION));
        lblNodeTruncate.setForeground(p.getColor(StartupProperties.COLOR_NODE_TRUNCATE));
    }

    @Override
    public void write() {
        StartupProperties p = StartupProperties.getInstance();

        p.setColor(StartupProperties.COLOR_NODE_TEXT, lblNodeText.getForeground());
        p.setColor(StartupProperties.COLOR_NODE_PREFIX, lblColorNodePrefix.getForeground());
        p.setColor(StartupProperties.COLOR_NODE_DESCRIPTION, lblNodeDescription.getForeground());
        p.setColor(StartupProperties.COLOR_NODE_TRUNCATE, lblNodeTruncate.getForeground());
    }

    class ChangeColorAction extends AbstractAction {
        public ChangeColorAction(JTextField textField) {
            super("..");
            tf = textField;
        }
        private JTextField tf;

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            JColorChooser dlg = new JColorChooser();
            Color c = JColorChooser.showDialog(tf, "Choose color", tf.getForeground());

            if(c != null) {
                tf.setForeground(c);
            }
        }
    }
}
