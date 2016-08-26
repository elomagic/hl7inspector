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
package de.elomagic.hl7inspector.gui.profiles.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JList;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.profile.ProfileIO;

/**
 *
 * @author Carsten Rambow
 */
public class DefaultProfileAction extends AbstractAction {

    private static final long serialVersionUID = -4333787361797188249L;
    private final JList list;

    /**
     * Creates a new instance of FileOpenAction.
     *
     * @param list
     */
    public DefaultProfileAction(final JList list) {
        super("Set default", null);

        this.list = list;

        putValue(SHORT_DESCRIPTION, "Set selected profile as default");
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if(list.getSelectedValue() != null) {
                ProfileFile file = (ProfileFile)list.getSelectedValue();

                StartupProperties.getInstance().setProperty(StartupProperties.DEFAULT_PROFILE, (file).toString());
                list.repaint();

                Profile profile = Desktop.getInstance().setProfileFile(file);
                if(profile != null) {
                    ProfileIO.setDefault(profile);
                }
            } else {
                Notification.error("No profile selected!");
            }
        } catch(Exception ex) {
            Logger.getLogger(getClass()).error(ex);
        }
    }
}
