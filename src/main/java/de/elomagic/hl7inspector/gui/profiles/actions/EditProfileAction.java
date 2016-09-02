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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.swing.AbstractAction;
import javax.swing.JList;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.profiles.ProfileDefinitionDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.profile.ProfileIO;

/**
 *
 * @author Carsten Rambow
 */
public class EditProfileAction extends AbstractAction {

    private static final long serialVersionUID = -6049725574076007265L;
    private final JList list;

    /**
     * Creates a new instance of FileOpenAction.
     *
     * @param list
     */
    public EditProfileAction(final JList list) {
        super("Edit", ResourceLoader.loadImageIcon("edit.png"));

        this.list = list;

        putValue(SHORT_DESCRIPTION, "Edit selected profile");
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        try {
            if(list.getSelectedValue() != null) {
                ProfileFile profileFile = (ProfileFile)list.getSelectedValue();

                if(Files.notExists(profileFile.getFile())) {
                    Notification.error("Profile not found!");
                } else {
                    ProfileDefinitionDialog dialog = new ProfileDefinitionDialog(profileFile);
                    if(dialog.ask()) {
                        try {
                            try (OutputStream out = Files.newOutputStream(dialog.getProfileFile().getFile(), StandardOpenOption.CREATE)) {
                                ProfileIO.save(out, dialog.getProfile());
                                dialog.getProfileFile().setDescription(dialog.getProfile().getName());
                            }

                            StartupProperties.setDefaultProfileFile(profileFile.getFile());

                            if(profileFile.equals(new ProfileFile(StartupProperties.getDefaultProfileFile()))) {
                                Desktop.getInstance().setProfileFile(profileFile);
                            }
                        } catch(IOException | JAXBException ee) {
                            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                            Notification.error(ee);
                        }
                    }
                }
            } else {
                Notification.error("No profile selected!");
            }
        } catch(Exception ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            Notification.error(ex);
        }
    }
}
