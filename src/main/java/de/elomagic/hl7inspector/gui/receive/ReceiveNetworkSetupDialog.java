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
package de.elomagic.hl7inspector.gui.receive;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.GradientLabel;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.ToolKit;
import de.elomagic.hl7inspector.io.SendOptionsBean;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 *
 * @author rambow
 */
public class ReceiveNetworkSetupDialog extends BaseDialog {

    /** Creates a new instance of SendOptionsDialog */
    public ReceiveNetworkSetupDialog() {
        super(Desktop.getInstance());

        init();
    }

    private void init() {
        getBanner().setVisible(false);

        setTitle("Receive Network Setup");
        //setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);

        cbServerPort = new JComboBox(new String[]{"2100", "2200", "2300", "5555", "5556"});
        cbServerPort.setEditable(true);
        cbServerPort.setSelectedItem("");
        cbReuse = new JCheckBox();
        cbReuse.setToolTipText("Reuse socket for next the message.");

        FormLayout layout = new FormLayout(
                "0dlu, p, 4dlu, 50dlu, 4dlu, p, p:grow",
                //            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.add(new GradientLabel("Network:"), cc.xyw(1, 1, 7));

        // 3rd row
        builder.addLabel("Server Port:", cc.xy(2, 3));        // Ok
        builder.add(cbServerPort, cc.xy(4, 3));

        // 6th row
        builder.addLabel("Reuse:", cc.xyw(2, 5, 3));
        builder.add(cbReuse, cc.xyw(4, 5, 2));

        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);

        pack();

        setSize(530, getPreferredSize().height);

        setLocationRelativeTo(getOwner());
    }

    public void setOptions(SendOptionsBean bean) {
        cbServerPort.setSelectedItem(Integer.toString(bean.getPort()));
        cbReuse.setSelected(bean.isReuseSocket());
    }

    @Override
    public void ok() {
        try {
            try {
                Integer.parseInt(cbServerPort.getSelectedItem().toString());
            } catch (Exception ee) {
                throw new Exception("Server port must an integer value!");
            }

            super.ok();
        } catch (Exception e) {
            SimpleDialog.error(e.getMessage());
        }
    }

    public SendOptionsBean getOptions() {
        SendOptionsBean bean = new SendOptionsBean();
        bean.setPort(Integer.parseInt(cbServerPort.getSelectedItem().toString()));
        bean.setReuseSocket(cbReuse.isSelected());

        return bean;
    }

    private JComboBox cbServerPort;

    private JCheckBox cbReuse;
}
