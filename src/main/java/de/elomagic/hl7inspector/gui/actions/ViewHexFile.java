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
 *
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
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ViewHexFile extends AbstractAction {
    
    /** Creates a new instance of FileNewAction */
    public ViewHexFile() {
        super("View with hex viewer", ResourceLoader.loadImageIcon("hexedit.png"));//icon);
        
        putValue(SHORT_DESCRIPTION, "View source file in hex editor.");
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.SHIFT_DOWN_MASK));

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (StartupProperties.getInstance().getExternalHexViewer() == null)
            SimpleDialog.info("No external hex viewer/editor set. Please check your configuration.");
        else {
            TreePath[] paths = Desktop.getInstance().getTree().getSelectionPaths();
            
            if (paths != null) {
                if (paths.length == 0)
                    SimpleDialog.error("One message which imported from a file must be selected.");
                else if (paths.length > 1)
                    SimpleDialog.error("Please select only one message.");
                else {
                    TreePath path = paths[0];
                    
                    Hl7Object node = (Hl7Object)path.getLastPathComponent();
                    
                    while (!(node instanceof Message))
                        node = node.getHl7Parent();
                    
                    Message message = (Message)node;
                                       
                    File file;
                    
                    try {
                        file = new File(new URI(message.getSource()));
                    } catch (Exception ee) {
                        file = null;
                    }
                    
                    if (file == null)
                        SimpleDialog.error("Only messages from a file can be open.");
                    else {
                        try {
                            Runtime rt = Runtime.getRuntime();
                            String[] cmd = { StartupProperties.getInstance().getExternalHexViewer().toString() , file.getAbsolutePath() };
                            rt.exec(cmd);
                        } catch (Exception ee) {
                            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                            SimpleDialog.error(ee, "Can't start external hex viewer/editor application.");
                        }
                    }
                }
            }
        }
    }
}

