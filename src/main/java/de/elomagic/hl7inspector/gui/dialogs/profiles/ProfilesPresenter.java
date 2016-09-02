/*
 * Copyright 2016 Carsten Rambow
 * 
 * Licensed under the GNU Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.gnu.org/licenses/gpl.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.elomagic.hl7inspector.gui.dialogs.profiles;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.file.filters.ExtensionFilters;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.UITool;
import de.elomagic.hl7inspector.gui.dialogs.profile.ProfileDialog;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.profile.ProfileIO;

/**
 *
 * @author Carsten Rambow
 */
public class ProfilesPresenter implements Initializable {

    @FXML
    private ListView<ProfileFile> listProfiles;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        loadContent();
    }

    private void loadContent() {

        ObservableList<ProfileFile> checkOptions = FXCollections.observableList(StartupProperties.getInstance().getProfiles());
        listProfiles.setItems(checkOptions);

    }

    @FXML
    private void handleButtonImportAction(final ActionEvent event) {
        //StartupProperties prop = StartupProperties.getInstance();

        Path file = UITool.chooseFile("Choose HL7 profile", listProfiles.getScene().getWindow(), ExtensionFilters.getProfileExtensionFilters(), Paths.get(System.getProperty("user.dir")));

        if(file != null) {
            ProfileFile profileFile = new ProfileFile(file);

            try (InputStream in = Files.newInputStream(profileFile.getFile(), StandardOpenOption.READ)) {
                Profile p = ProfileIO.load(in);
                profileFile.setDescription(p.getDescription());

                listProfiles.getItems().add(profileFile);
                listProfiles.getSelectionModel().select(profileFile);
            } catch(Exception ex) {
                Logger.getLogger(getClass()).error(ex);
                Notification.error("Invalid file format!");
            }
        }
    }

    @FXML
    private void handleButtonNewAction(final ActionEvent event) {
        try {
            Path file = UITool.saveFile("Create new profile", listProfiles.getScene().getWindow(), ExtensionFilters.getProfileExtensionFilters(), Paths.get(System.getProperty("user.dir")));

            if(file != null) {
                ProfileFile profileFile = new ProfileFile(file);

                if(Files.exists(profileFile.getFile())) {
                    if(Notification.confirmOkCancel("File already exists. Overwrite?").get() == ButtonType.OK) {
                        Files.deleteIfExists(profileFile.getFile());
                    }
                }
                try (OutputStream out = Files.newOutputStream(profileFile.getFile(), StandardOpenOption.CREATE)) {
                    ProfileIO.save(out, new Profile());
                }

                listProfiles.getItems().add(profileFile);
                listProfiles.getSelectionModel().select(profileFile);

                handleButtonEditAction(null);
            }
        } catch(Exception ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            Notification.error(ex);
        }
    }

    @FXML
    private void handleButtonEditAction(final ActionEvent event) {
        try {
            ProfileFile profileFile = listProfiles.getSelectionModel().getSelectedItem();
            if(profileFile == null) {
                Notification.error("No profile selected!");
            } else if(Files.exists(profileFile.getFile())) {
                ProfileDialog dialog = new ProfileDialog(profileFile);
                if(dialog.ask()) {
                    try {
                        try (OutputStream out = Files.newOutputStream(profileFile.getFile(), StandardOpenOption.CREATE)) {
                            ProfileIO.save(out, dialog.getProfile());
                            dialog.getProfileFile().setDescription(dialog.getProfile().getName());
                        }

                        StartupProperties.setDefaultProfileFile(profileFile.getFile());

                        if(profileFile.equals(new ProfileFile(StartupProperties.getDefaultProfileFile()))) {
                            Desktop.getInstance().setProfileFile(profileFile);
                        }
                    } catch(Exception ex) {
                        Logger.getLogger(getClass()).error(ex.getMessage(), ex);
                        Notification.error(ex);
                    }
                }
            } else {
                Notification.error("Profile not found!");
            }
        } catch(Exception ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            Notification.error(ex);
        }
    }

    @FXML
    private void handleButtonRemoveAction(final ActionEvent event) {
        ProfileFile profileFile = listProfiles.getSelectionModel().getSelectedItem();
        if(profileFile != null) {
            if(Notification.confirmOkCancel("Profile will only removed from the HL7 Inspector and will not be deleted. Are you sure?").get() == ButtonType.OK) {
                listProfiles.getItems().remove(profileFile);
            }
        } else {
            Notification.error("No profile selected!");
        }
    }

    @FXML
    private void handleButtonSetDefaultAction(final ActionEvent event) {

        try {
            ProfileFile profileFile = listProfiles.getSelectionModel().getSelectedItem();
            if(profileFile != null) {

                StartupProperties.setDefaultProfileFile(profileFile.getFile());

                listProfiles.refresh();

                Profile profile = Desktop.getInstance().setProfileFile(profileFile);
                if(profile != null) {
                    ProfileIO.setDefault(profile);
                }
            } else {
                Notification.error("No profile selected!");
            }
        } catch(Exception ex) {
            Logger.getLogger(getClass()).error(ex);
            Notification.error(ex);
        }
    }

    @FXML
    private void handleButtonCloseAction(final ActionEvent event) {
        Stage stage = (Stage)listProfiles.getScene().getWindow();
        stage.close();
    }

}
