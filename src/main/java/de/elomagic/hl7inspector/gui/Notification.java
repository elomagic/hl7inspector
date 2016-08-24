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
package de.elomagic.hl7inspector.gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 *
 * @author Carsten Rambow
 */
public final class Notification {

    private Notification() {
    }

    /**
     *
     * @param text
     * @return
     */
    public static Optional<ButtonType> confirmOkCancel(final String text) {

        NotificationRunnable runnable = new NotificationRunnable() {
            @Override
            public void run() {
                Alert alert = new Alert(AlertType.CONFIRMATION, text);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);

                result = alert.showAndWait();
                handled = true;
            }
        };

        showAndWait(runnable);

        return runnable.result;

    }

    public static void info(final String text) {

        NotificationRunnable runnable = new NotificationRunnable() {
            @Override
            public void run() {
                Alert alert = new Alert(AlertType.INFORMATION, text);
                alert.setTitle("Information");
                alert.setHeaderText(null);

                result = alert.showAndWait();
                handled = true;
            }
        };

        showAndWait(runnable);
    }

    public static void error(final Exception exception) {
        error(exception, null);
    }

    public static void error(final Exception exception, final String text) {

        NotificationRunnable runnable = new NotificationRunnable() {
            @Override
            public void run() {
                Alert alert = new Alert(AlertType.ERROR, text);
                alert.setTitle("Error");
                alert.setHeaderText("An error occur.");

                // Create expandable Exception.
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exception.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label("The exception stacktrace was:");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alert.getDialogPane().setExpandableContent(expContent);

                result = alert.showAndWait();
                handled = true;
            }
        };

        showAndWait(runnable);
    }

    private static void showAndWait(final NotificationRunnable runnable) {
        Platform.runLater(runnable);

        try {
            while(!runnable.handled) {
                Thread.sleep(100);
            }
        } catch(Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    static class NotificationHelper implements NotificationResultHandler {

        boolean handled = false;
        Optional<ButtonType> result;

        @Override
        public void getResultButtonType(final Optional<ButtonType> type) {
            result = type;
        }
    }

    static abstract class NotificationRunnable implements Runnable {

        boolean handled = false;
        Optional<ButtonType> result;

    }

}
