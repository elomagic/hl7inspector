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

import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author rambow
 */
public class BottomPanel extends JPanel {

    /** Creates a new instance of BottomPanel */
    public BottomPanel() {
        super(new BorderLayout());

        statusPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(profileLabel, BorderLayout.EAST);

        add(FindBar.getInstance(), BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
    }

    public void setProfileText(String text) {
        profileLabel.setText("Profile: ".concat(text).concat(" "));
    }

    public void setProfileTooltTip(String text) {
        profileLabel.setToolTipText(text);
    }

    public void setStatusText(String text) {
        statusLabel.setText(text);
    }

    public void setStatusIcon(Icon icon) {
        statusLabel.setIcon(icon);
    }

    private JPanel statusPanel = new JPanel(new BorderLayout());

    private JLabel statusLabel = new JLabel("");

    private JLabel profileLabel = new JLabel("Profile: No profile selected");
}
