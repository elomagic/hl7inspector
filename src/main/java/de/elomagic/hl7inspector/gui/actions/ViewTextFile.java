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
 */
package de.elomagic.hl7inspector.gui.actions;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.images.ResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ViewTextFile extends AbstractAction {
    /**
     * Creates a new instance of FileNewAction.
     */
    public ViewTextFile() {
        super();

        putValue(NAME, "View with text editor");
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("accessories-text-editor.png"));
        putValue(SHORT_DESCRIPTION, "View source file in text editor.");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(StartupProperties.getInstance().getExternalFileViewer() == null) {
            SimpleDialog.info("No external file viewer/editor set. Please check your configuration.");
        } else {
            List<Hl7Object> selectedObjects = Desktop.getInstance().getSelectedObjects();
            if(selectedObjects.isEmpty()) {
                SimpleDialog.error("One message which imported from a file must be selected.");
            } else if(selectedObjects.size() > 1) {
                SimpleDialog.error("Please select only one message.");
            } else {
                Hl7Object node = selectedObjects.get(0);

                while(!(node instanceof Message)) {
                    node = node.getHl7Parent();
                }

                Message message = (Message)node;

                File file;
                try {
                    file = new File(new URI(message.getSource()));
                } catch(Exception ee) {
                    file = null;
                }

                if(file == null) {
                    SimpleDialog.error("Only messages from a file can be open.");
                } else {
                    try {
                        Runtime rt = Runtime.getRuntime();
                        String[] cmd = {StartupProperties.getInstance().getExternalFileViewer().toString(), file.getAbsolutePath()};
                        rt.exec(cmd);
                    } catch(Exception ee) {
                        Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                        SimpleDialog.error(ee, "Can't start external file viewer/editor application.");
                    }
                }
            }
        }
    }
}
