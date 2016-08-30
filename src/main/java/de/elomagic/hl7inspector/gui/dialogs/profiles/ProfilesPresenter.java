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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.profile.ProfileFile;

/**
 *
 * @author Carsten Rambow
 */
public class ProfilesPresenter implements Initializable {

    @FXML
    private ListView<ProfileFile> listView;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        loadContent();
    }

    private void loadContent() {

        ObservableList<ProfileFile> checkOptions = FXCollections.observableList(StartupProperties.getInstance().getProfiles());
        listView.setItems(checkOptions);

    }

    @FXML
    private void handleButtonImportAction(final ActionEvent event) {

    }

    @FXML
    private void handleButtonNewAction(final ActionEvent event) {

    }

    @FXML
    private void handleButtonEditAction(final ActionEvent event) {

    }

    @FXML
    private void handleButtonRemoveAction(final ActionEvent event) {

    }

    @FXML
    private void handleButtonSetDefaultAction(final ActionEvent event) {

    }

    @FXML
    private void handleButtonCloseAction(final ActionEvent event) {

    }

    @FXML
    private void handleButtonAction(final ActionEvent event) {

    }

}
