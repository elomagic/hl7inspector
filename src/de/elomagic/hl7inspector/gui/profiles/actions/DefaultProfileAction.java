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
package de.elomagic.hl7inspector.gui.profiles.actions;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.profile.ProfileFile;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JList;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class DefaultProfileAction extends AbstractAction {

    /** Creates a new instance of FileOpenAction */
    public DefaultProfileAction(JList _list) {
        super("Set default", null);//ResourceLoader.loadImageIcon("edit_add.png"));

        list = _list;

        putValue(SHORT_DESCRIPTION, "Set selected profile as default");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (list.getSelectedValue() != null) {
                ProfileFile file = (ProfileFile) list.getSelectedValue();

                StartupProperties.getInstance().setProperty(StartupProperties.DEFAULT_PROFILE, (file).toString());
                list.repaint();

                Desktop.getInstance().setProfileFile(file);
            } else {
                SimpleDialog.error("No profile selected!");
            }
        } catch (Exception ee) {
            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
        }
    }

    private JList list;
}
