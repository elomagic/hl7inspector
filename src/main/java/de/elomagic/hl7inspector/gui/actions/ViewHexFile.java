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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.util.List;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ViewHexFile extends BasicAction {
    /**
     * Creates a new instance of ViewHexFile.
     */
    public ViewHexFile() {
        super();

        putValue(NAME, "View with hex viewer");
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("hexedit.png"));
        putValue(SHORT_DESCRIPTION, "View source file in hex editor.");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.SHIFT_DOWN_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(StartupProperties.getInstance().getExternalHexViewer() == null) {
            SimpleDialog.info("No external hex viewer/editor set. Please check your configuration.");
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
                        String[] cmd = {StartupProperties.getInstance().getExternalHexViewer().toString(), file.getAbsolutePath()};
                        rt.exec(cmd);
                    } catch(Exception ee) {
                        Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                        SimpleDialog.error(ee, "Can't start external hex viewer/editor application.");
                    }
                }
            }
        }
    }
}
