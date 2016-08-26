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
package de.elomagic.hl7inspector.gui.dialogs;

import java.awt.BorderLayout;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import com.airhacks.afterburner.views.FXMLView;
import com.l2fprod.common.swing.BaseDialog;

import de.elomagic.hl7inspector.gui.Desktop;

/**
 * Abstract wrapper class to use JavaFX in Swing context.
 *
 * @author Carsten Rambow
 */
public abstract class AbstractDialog extends BaseDialog {

    public AbstractDialog(final String title) {
        super(Desktop.getInstance().getMainFrame(), title, true);

        getBanner().setVisible(false);

        setDialogMode(BaseDialog.CLOSE_DIALOG);
        setResizable(true);
        setLayout(new BorderLayout());

        JFXPanel fxPanel = new JFXPanel();
        getContentPane().add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(()->{
            FXMLView content = getContent();

            Scene scene = new Scene(content.getView());
            fxPanel.setScene(scene);
        });

        setSize(400, 300);
        setLocationRelativeTo(getOwner());

    }

    protected abstract FXMLView getContent();

}
