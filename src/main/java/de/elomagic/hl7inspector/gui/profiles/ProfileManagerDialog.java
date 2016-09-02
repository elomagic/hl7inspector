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
package de.elomagic.hl7inspector.gui.profiles;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.VectorListModel;
import de.elomagic.hl7inspector.gui.profiles.actions.EditProfileAction;

/**
 *
 * @author rambow
 */
public class ProfileManagerDialog extends BaseDialog {

    private JList lstProfiles = new JList();
    private JButton btEdit = new JButton(new EditProfileAction(lstProfiles));

    /**
     * Creates a new instance of ProfileRegistrationDialog.
     */
    public ProfileManagerDialog() {
        super(Desktop.getInstance().getMainFrame(), "Profile Manager", true);
        init();
    }

    private void init() {
        getBanner().setVisible(false);
        getButtonPane().setVisible(false);

        JScrollPane scroll = new JScrollPane(lstProfiles);

        FormLayout layout = new FormLayout(
                "min:grow, 4dlu, p",
                "p, 4dlu, p, 4dlu, p, 4dlu, p , 8dlu, p, p:grow, p");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        //builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.add(scroll, cc.xywh(1, 1, 1, 11));

        builder.add(btEdit, cc.xy(3, 5));

        getContentPane().add(builder.getPanel());

        lstProfiles.setModel(new VectorListModel<>(StartupProperties.getInstance().getProfiles()));
        lstProfiles.setCellRenderer(new ProfileCellRenderer());
        setSize(400, 300);

        setLocationRelativeTo(getOwner());
    }

}
