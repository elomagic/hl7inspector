/*
 * Copyright 2016 Carsten Rambow
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
package de.elomagic.hl7inspector.gui.security;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.security.KeyStore;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.utils.StringVector;

/**
 *
 * @author Carsten Rambow
 */
public class CommonPanel extends KeyStorePanel {

    private JTextField editProv;
    private JTextField editVersion;
    private JTextArea editInfo;
    private JTextField editType;
    private JLabel lbLastUpdateDate;
    private JLabel lbValidateStatus;

    /**
     * Creates a new instance of CommonPanel.
     *
     * @param d
     */
    public CommonPanel(final PanelDialog d) {
        super(d);
    }

    @Override
    protected void init() {
        editProv = new JTextField();
        editVersion = new JTextField();
        editInfo = new JTextArea();
        editInfo.setLineWrap(true);
        editType = new JTextField();
        lbLastUpdateDate = new JLabel();
        lbLastUpdateDate.setFont(lbLastUpdateDate.getFont().deriveFont(Font.BOLD));

        FormLayout layout = new FormLayout(
                "p, 4dlu, p:grow",
                "p, 3dlu, p, 3dlu, p, top:max(100dlu;pref), 3dlu, p, p:grow");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.addLabel("Provider", cc.xy(1, 1));
        builder.add(editProv, cc.xy(3, 1));

        builder.addLabel("Version:", cc.xy(1, 3));
        builder.add(editVersion, cc.xy(3, 3));

        builder.addLabel("Info:", cc.xy(1, 5));
        builder.add(editInfo, cc.xywh(3, 5, 1, 2));

        builder.addLabel("Type:", cc.xy(1, 8));
        builder.add(editType, cc.xy(3, 8));

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
        builder.add(btValidate, cc.xy(1, 1));

        builder.add(lbValidateStatus, cc.xyw(1, 3, 3));

        add(builder.getPanel(), BorderLayout.CENTER);
    }

    public void resetValidateStatus() {
        lbValidateStatus.setText("The keystore validation status is unknown. Press 'Validate' button.");
        lbValidateStatus.setIcon(ResourceLoader.loadImageIcon("warning.png"));
    }

    public void setValidateStatus(final boolean valid) {
        lbValidateStatus.setText((valid) ? "The keystore is valid" : "The keystore includes invalid or expired certificate(s). Press 'Validate' to show the list of errors.");
        lbValidateStatus.setIcon(ResourceLoader.loadImageIcon((valid) ? "ok.png" : "warning.png"));
    }

    @Override
    public void write(final KeyStore keyStore) {
//        keyStore.get
//        profile.setDescription(editDesc.getText());
//        profile.setName(editName.getText());
    }

    @Override
    public void read(final KeyStore keyStore) {
        editProv.setText(keyStore.getProvider().getName());
        editVersion.setText(Double.toString(keyStore.getProvider().getVersion()));
        editInfo.setText(keyStore.getProvider().getInfo());
        editProv.setText(keyStore.getProvider().getName());
        editType.setText(keyStore.getType());
//        lbLastUpdateDate.setText(profile.getSchemaVersion());
    }

    @Override
    public String getTitle() {
        return "Common";
    }

    @Override
    public javax.swing.Icon getIcon() {
        return ResourceLoader.loadImageIcon("info.png", ResourceLoader.LARGE_IMAGE);
    }

    @Override
    public String getDescription() {
        return "";
    }

    class ValidateProfileAction extends AbstractAction {

        public ValidateProfileAction() {
            super("Validate keystore");
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            KeyStoreDialog dlg = (KeyStoreDialog)getDialog();

            try {
                StringVector val = new StringVector(dlg.getKeyStore().aliases());

                if(!val.isEmpty()) {
                    Notification.warn("List of aliases", val.toString((char)10));
                }
            } catch(Exception ee) {
                Notification.error(ee);
            }

        }
    }
}
