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
package de.elomagic.hl7inspector.gui.dialogs.options;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.UITool;

/**
 *
 * @author Carsten Rambow
 */
public class OptionsPresenter implements Initializable {

    @FXML
    private CheckBox btOneInstance;
    @FXML
    private ComboBox cbCheckPeriod;
    @FXML
    private TextField tfProxyHost;
    @FXML
    private TextField tfProxyPort;
    @FXML
    private ComboBox cbProxyType;

    @FXML
    private CheckBox btTruncateNodes;
    @FXML
    private ComboBox cbViewMode;
    @FXML
    private TextField tfMessageNodeFormat;
    @FXML
    private TextField tfTruncateLength;
    @FXML
    private Label lbMessageNodeSample;

    @FXML
    private TextField tfTextViewer;
    @FXML
    private TextField tfHexViewer;
    @FXML
    private Button btTextViewer;
    @FXML
    private Button btHexViewer;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        initView();

        loadGeneral();
        loadTreeView();
        loadExternalViewers();
    }

    private void loadGeneral() {
        StartupProperties p = StartupProperties.getInstance();

        btOneInstance.setSelected(p.isOneInstance());

        switch(p.getAutoUpdatePeriod()) {
            case 0: {
                cbCheckPeriod.setValue(cbCheckPeriod.getItems().get(1));
                break;
            }
            case 1: {
                cbCheckPeriod.setValue(cbCheckPeriod.getItems().get(2));
                break;
            }
            case 7: {
                cbCheckPeriod.setValue(cbCheckPeriod.getItems().get(3));
                break;
            }
            case 14: {
                cbCheckPeriod.setValue(cbCheckPeriod.getItems().get(4));
                break;
            }
            case 30: {
                cbCheckPeriod.setValue(cbCheckPeriod.getItems().get(5));
                break;
            }
            default: {
                cbCheckPeriod.setValue(cbCheckPeriod.getItems().get(0));
                break;
            }
        }

        switch(p.getProxyMode()) {
            case 0: {
                cbProxyType.setValue(cbProxyType.getItems().get(0));
                break;
            }
            case 1: {
                cbProxyType.setValue(cbProxyType.getItems().get(1));
                break;
            }
            case 2: {
                cbProxyType.setValue(cbProxyType.getItems().get(2));
                break;
            }
            default: {
                cbProxyType.setValue(cbProxyType.getItems().get(0));
                break;
            }
        }

        tfProxyHost.setText(p.getProxyHost());
        tfProxyPort.setText(Integer.toString(p.getProxyPort()));
    }

    private void loadTreeView() {
        StartupProperties p = StartupProperties.getInstance();

        cbViewMode.setValue(cbViewMode.getItems().get(p.getTreeViewMode()));
        btTruncateNodes.setSelected(p.getTreeNodeLength() != 0);
        tfMessageNodeFormat.setText(StartupProperties.getTreeMessageNodeFormat());
        tfTruncateLength.setText(Integer.toString(p.getTreeNodeLength() == 0 ? 200 : p.getTreeNodeLength()));
    }

    private void loadExternalViewers() {
        StartupProperties p = StartupProperties.getInstance();
        tfTextViewer.setText(p.getExternalFileViewer() == null ? "" : p.getExternalFileViewer().getAbsolutePath());
        tfHexViewer.setText(p.getExternalHexViewer() == null ? "" : p.getExternalHexViewer().getAbsolutePath());
    }

    private void initView() {

        // Tab General 
        ObservableList<String> checkOptions = FXCollections.observableArrayList("Never", "Every Startup", "Every Day", "Every Week", "Every Two Week", "Every Month");
        cbCheckPeriod.setItems(checkOptions);
        ObservableList<String> proxyOptions = FXCollections.observableArrayList("No Proxy", "Use System Proxy Settings", "HTTP Proxy");
        cbProxyType.setItems(proxyOptions);

        // Tab Tree
        ObservableList<String> viewOptions = FXCollections.observableArrayList("Plain HL7 message in root message node", "Compact HL7 message information in root message node");
        cbViewMode.setItems(viewOptions);

        // Tab External viewers
        btTextViewer.setOnAction((ActionEvent event)->{
            Path file = UITool.chooseFile("Choose external viewer", null);
            tfTextViewer.setText(file == null ? "" : file.toString());
        });

        btHexViewer.setOnAction((ActionEvent event)->{
            Path file = UITool.chooseFile("Choose external viewer", null);
            tfHexViewer.setText(file == null ? "" : file.toString());
        });
    }

    public void write() throws Exception {

        StartupProperties p = StartupProperties.getInstance();

        //
        p.setOneInstance(btOneInstance.isSelected());
        p.setProxyMode(cbProxyType.getSelectionModel().getSelectedIndex());

        p.setProxyHost(tfProxyHost.getText());
        try {
            p.setProxyPort(Integer.parseInt(tfProxyPort.getText()));
        } catch(Exception e) {
            p.setProxyPort(0);
        }

        switch(cbCheckPeriod.getSelectionModel().getSelectedIndex()) {
            case 1: {
                p.setAutoUpdatePeriod(0);
                break;
            }
            case 2: {
                p.setAutoUpdatePeriod(1);
                break;
            }
            case 3: {
                p.setAutoUpdatePeriod(7);
                break;
            }
            case 4: {
                p.setAutoUpdatePeriod(14);
                break;
            }
            case 5: {
                p.setAutoUpdatePeriod(30);
                break;
            }
            default: {
                p.setAutoUpdatePeriod(-1);
                break;
            }
        }
        p.setAutoUpdateAsk(cbCheckPeriod.getSelectionModel().getSelectedIndex() != 0);

        //
        p.setTreeViewMode(cbViewMode.getSelectionModel().getSelectedIndex());
        p.setTreeNodeLength(btTruncateNodes.isSelected() ? Integer.parseInt(tfTruncateLength.getText()) : 0);
        StartupProperties.setTreeMessageNodeFormat(tfMessageNodeFormat.getText());

        // Third tab
        p.setExternalFileViewer(tfTextViewer.getText().isEmpty() ? null : new File(tfTextViewer.getText()));
        p.setExternalHexViewer(tfHexViewer.getText().isEmpty() ? null : new File(tfHexViewer.getText()));
    }

}
