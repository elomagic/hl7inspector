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
package de.elomagic.hl7inspector.gui.dialogs.about;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.images.ResourceLoader;

/**
 *
 * @author Carsten Rambow
 */
public class AboutPresenter implements Initializable {

    //private JLabel lblLogo = new JLabel(ResourceLoader.loadImageIcon("xlarge/hl7inspector.png"));
    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lbVersion;
    @FXML
    private Label lbProductVersion;
    @FXML
    private Label lbOperationSystem;
    @FXML
    private Label lbJavaVersion;
    @FXML
    private Label lbJavaVM;
    @FXML
    private Label lbJavaVendor;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        imgLogo.setImage(ResourceLoader.loadImage("xlarge/hl7inspector.png"));
        imgLogo.setFitWidth(64);

        lbVersion.setText("Version: ".concat(Hl7Inspector.getVersionString()));

        lbProductVersion.setText(Hl7Inspector.getVersionString());
        lbOperationSystem.setText(getProperty("os.name") + " " + getProperty("os.version") + " running on " + getProperty("os.arch"));

        lbJavaVersion.setText(getProperty("java.version"));

        lbJavaVM.setText(getProperty("java.vm.name") + " " + getProperty("java.vm.version"));

        lbJavaVendor.setText(getProperty("java.vendor"));

    }

    private String getProperty(final String key) {
        return System.getProperty(key, "Unknown");
    }

}
