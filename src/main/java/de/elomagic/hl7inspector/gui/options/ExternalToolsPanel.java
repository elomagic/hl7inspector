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
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.GradientLabel;
import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
import javax.swing.JFileChooser;
//import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author rambow
 */
public class ExternalToolsPanel extends AbstractPanel {

    /** Creates a new instance of GeneralOptionPane */
    public ExternalToolsPanel(PanelDialog d) {
        super(d);
    }

    @Override
    public void init() {
        editTextViewer = new JTextField();
        editHexViewer = new JTextField();
        btChooseTextViewer = new JButton(new FileChooseAction());
        btChooseHexViewer = new JButton(new FileChooseAction());

        FormLayout layout = new FormLayout(
                "8dlu, p, 4dlu, p:grow, 4dlu, p",
                //            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, "
                + "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu, "
                + "p, 3dlu, p, 3dlu, p, 7dlu");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.add(new GradientLabel("External Viewers"), cc.xyw(1, 1, 6));

        builder.addLabel("Textviewer:", cc.xy(2, 3));      // Ok
        builder.add(editTextViewer, cc.xy(4, 3));
        builder.add(btChooseTextViewer, cc.xy(6, 3));

        builder.addLabel("Hexviewer:", cc.xy(2, 5));      // Ok
        builder.add(editHexViewer, cc.xy(4, 5));
        builder.add(btChooseHexViewer, cc.xy(6, 5));

        add(builder.getPanel(), BorderLayout.CENTER);
    }

    @Override
    public Icon getIcon() {
        return ResourceLoader.loadImageIcon("pref-external-tools.png", ResourceLoader.LARGE_IMAGE);
    }

    @Override
    public String getTitle() {
        return "External Tools";
    }

    @Override
    public String getDescription() {
        return "External Tools";
    }

    @Override
    public void read() {
        StartupProperties p = StartupProperties.getInstance();
        editTextViewer.setText(p.getExternalFileViewer() != null ? p.getExternalFileViewer().getAbsolutePath() : "");
        editHexViewer.setText(p.getExternalHexViewer() != null ? p.getExternalHexViewer().getAbsolutePath() : "");
    }

    @Override
    public void write() {
        StartupProperties p = StartupProperties.getInstance();

        p.setExternalFileViewer(editTextViewer.getText().length() != 0 ? new File(editTextViewer.getText()) : null);
        p.setExternalHexViewer(editHexViewer.getText().length() != 0 ? new File(editHexViewer.getText()) : null);
    }

    private JTextField editTextViewer;

    private JTextField editHexViewer;

    private JButton btChooseTextViewer;

    private JButton btChooseHexViewer;

    class FileChooseAction extends AbstractAction {

        public FileChooseAction() {
            super("...", null);//ResourceLoader.loadImageIcon("view_tree.png"));

            putValue(Action.SHORT_DESCRIPTION, "Choose external program.");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getDialog().setAlwaysOnTop(false);
            try {
                JFileChooser fc = new JFileChooser();
                //fc.set

                fc.setDialogTitle("Choose external viewer");
                if (fc.showOpenDialog(Desktop.getInstance()) == JFileChooser.APPROVE_OPTION) {
                    fc.setVisible(false);

                    if (e.getSource().equals(btChooseTextViewer)) {
                        editTextViewer.setText(fc.getSelectedFile().toString());
                    } else if (e.getSource().equals(btChooseHexViewer)) {
                        editHexViewer.setText(fc.getSelectedFile().toString());
                    }
                }
            } finally {
                getDialog().setAlwaysOnTop(false);
            }
        }

    };
}
