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

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SaveDialog;
import de.elomagic.hl7inspector.gui.SaveProgessDialog;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 *
 * @author rambow
 */
public class FileSaveAsAction extends AbstractAction {
    
    /** Creates a new instance of FileSaveAsAction */
    public FileSaveAsAction() {
        super("Save As...", ResourceLoader.loadImageIcon("document-save.png"));
        
        putValue(SHORT_DESCRIPTION, "Save message(s)");
//        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
    }
    
    public void actionPerformed(ActionEvent e) {
        SaveDialog dlg = new SaveDialog();
        if (dlg.ask()) {
            Vector<Message> messages = (dlg.getOptions().isOnlySelectedFiles())?Desktop.getInstance().getTree().getSelectedMessages():((Hl7TreeModel)Desktop.getInstance().getModel()).getMessages();
                        
            SaveProgessDialog dlgSave = new SaveProgessDialog(messages, dlg.getOptions());
            dlgSave.setVisible(true);
        }
    }
}