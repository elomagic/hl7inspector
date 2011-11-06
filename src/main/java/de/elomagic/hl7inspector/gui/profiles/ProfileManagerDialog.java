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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;
import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.VectorListModel;
import de.elomagic.hl7inspector.gui.actions.DefaultCloseWindowAction;
import de.elomagic.hl7inspector.gui.profiles.actions.*;
import de.elomagic.hl7inspector.profile.ProfileFile;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author rambow
 */
public class ProfileManagerDialog extends BaseDialog {

    private static final long serialVersionUID = -1202538406494514391L;

    /** Creates a new instance of ProfileRegistrationDialog */
    public ProfileManagerDialog() {
        super(Desktop.getInstance(), "Profile Manager", true);
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
        builder.add(btAdd, cc.xy(3, 1));

        builder.add(btNew, cc.xy(3, 3));

        builder.add(btEdit, cc.xy(3, 5));

        builder.add(btRemove, cc.xy(3, 7));

        builder.add(btDefault, cc.xy(3, 9));

        builder.add(btClose, cc.xy(3, 11));

        getContentPane().add(builder.getPanel());

        lstProfiles.setModel(new VectorListModel<ProfileFile>(StartupProperties.getInstance().getProfiles()));
        lstProfiles.setCellRenderer(new ProfileCellRenderer());
        lstProfiles.addMouseListener(new ProfileMouseClickListener());
        setSize(400, 300);

        setLocationRelativeTo(getOwner());
    }

    private JList lstProfiles = new JList();
    private JButton btAdd = new JButton(new AddProfileAction(lstProfiles));
    private JButton btEdit = new JButton(new EditProfileAction(lstProfiles));
    private JButton btNew = new JButton(new NewProfileAction(lstProfiles));
    private JButton btRemove = new JButton(new RemoveProfileAction(lstProfiles));
    private JButton btDefault = new JButton(new DefaultProfileAction(lstProfiles));
    private JButton btClose = new JButton(new DefaultCloseWindowAction(this));

    class ProfileMouseClickListener implements MouseListener {

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() == 2) {
                btEdit.doClick();
            }
        }

    }

}
