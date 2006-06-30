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
import de.elomagic.hl7inspector.file.filters.Hl7FileFilter;
import de.elomagic.hl7inspector.file.filters.TextFileFilter;
import de.elomagic.hl7inspector.file.filters.XmlFileFilter;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

/**
 *
 * @author rambow
 */
public class FileOpenAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public FileOpenAction() {
        super("File Open...", ResourceLoader.loadImageIcon("document-open.png"));
        
        putValue(SHORT_DESCRIPTION, "Open and imports hl7 message(s)");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
    }
    
    public void actionPerformed(ActionEvent e) {
        StartupProperties prop = StartupProperties.getInstance();
        File path = new File(prop.getProperty("de.elomagic.hl7inspector.import.path", System.getProperty("user.dir")));
        if (!path.exists()) {
            path = new File(System.getProperty("user.dir"));
        }
        
        JFileChooser fc = new JFileChooser(path);
        fc.addChoosableFileFilter(new TextFileFilter());
        fc.addChoosableFileFilter(new XmlFileFilter());
        fc.addChoosableFileFilter(new Hl7FileFilter());        
        
        fc.setDialogTitle("Choose file");
        if (fc.showOpenDialog(Desktop.getInstance()) == JFileChooser.APPROVE_OPTION) {
            fc.setVisible(false);
            
            prop.setProperty("de.elomagic.hl7inspector.import.path", fc.getCurrentDirectory().getPath());
            
            new FileRecentOpenAction(fc.getSelectedFile()).actionPerformed(null);
        }
    }
}
