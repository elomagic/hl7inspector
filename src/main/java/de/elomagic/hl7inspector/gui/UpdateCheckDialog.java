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
import de.elomagic.hl7inspector.autoupdate.UpdateChecker;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author rambow
 */
public class UpdateCheckDialog extends BaseDialog {

    /** Creates a new instance of UpdateCheckDialog */
    public UpdateCheckDialog() {
        super(Desktop.getInstance());

        init();
    }

    public static void check(boolean confirmCheck) throws Exception /* IOException, ParseException */ {
        StartupProperties p = StartupProperties.getInstance();

        Calendar lc = p.getLastUpdateCheck();

        boolean doCheck = !(p.getAutoUpdatePeriod() < 0);

        if (doCheck) {
            lc.add(Calendar.DAY_OF_MONTH, p.getAutoUpdatePeriod());
        }

        Calendar n = Calendar.getInstance();
        n.setTime(new Date());
        n.set(Calendar.HOUR_OF_DAY, lc.get(Calendar.HOUR_OF_DAY));
        n.set(Calendar.MINUTE, lc.get(Calendar.MINUTE));
        n.set(Calendar.SECOND, lc.get(Calendar.SECOND));
        n.set(Calendar.MILLISECOND, lc.get(Calendar.MILLISECOND));

        doCheck = (lc.before(n) || lc.equals(n)) && (doCheck);

        if ((doCheck) && (confirmCheck) && (p.isAutoUpdateAsk())) {
            doCheck = SimpleDialog.confirmYesNo("Check for updates of Hl7Inspector ?") == 0;
        }

        boolean b = false;

        if (!confirmCheck || doCheck) {
            UpdateCheckDialog dlg = new UpdateCheckDialog();

            dlg.setVisible(true);
            try {
                dlg.repaint();
                b = UpdateChecker.checkForUpdates();
                p.setLastUpdateCheck(Calendar.getInstance());
            } finally {
                dlg.setVisible(false);
            }

            if (!b) {
                SimpleDialog.info("No update of Hl7Inspector available");
            }
        }

        if (b) {
            // FEATURE Open side with default web browser
            SimpleDialog.info("A new version of Hl7Inspector available on www.elomagic.de");
        }
    }

    private void init() {
        getBanner().setVisible(false);
        getButtonPane().setVisible(false);

        setTitle("Update check");
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout(4, 4));

        lblText.getFont().deriveFont(Font.BOLD);
        lblText.setHorizontalAlignment(JLabel.CENTER);
        pbCheck.setIndeterminate(true);

//        btCancel.addActionListener(this);
        //        buttonPanel.add(btAbort);


        FormLayout layout = new FormLayout(
                "p:grow, p, p:grow",
                //            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 8dlu, p, 8dlu, p, p:grow");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.add(lblText, cc.xy(1, 1));

        // 2nd row
        builder.add(pbCheck, cc.xyw(1, 3, 3));

        // 3rd row
//        builder.add(btCancel,           cc.xy(2,   5));        // Ok

        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);

        // Button pan

        pack();

        setSize(300, getPreferredSize() != null ? getPreferredSize().height : 230);

        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }

    private JLabel lblText = new JLabel("Looking for updates...");

    private JProgressBar pbCheck = new JProgressBar();
//    private JButton         btCancel    = new JButton("Abort");
}
