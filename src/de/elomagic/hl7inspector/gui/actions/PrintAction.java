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

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
//import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import de.elomagic.hl7inspector.print.MessageRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterJob;
//import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

/**
 *
 * @author rambow
 */
public class PrintAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public PrintAction() {
        super("Print messages...", ResourceLoader.loadImageIcon("document-print.png"));
        
        putValue(SHORT_DESCRIPTION, "Print selected message(s).");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            Desktop desktop = Desktop.getInstance();
            
            if (((Hl7TreeModel) desktop.getTree().getModel()).getMessages().size() != 0) {
//                Vector<Message> messages = desktop.getTree().getSelectedMessages();
//
//                if (messages.size() == 0) {
//                    if (SimpleDialog.confirmYesNo("No message(s) selected. Print all messages?") == SimpleDialog.YES_OPTION) {
//                        messages.addAll(((Hl7TreeModel) desktop.getTree().getModel()).getMessages());
//                    }
//                }
//
//                if (messages.size() != 0) {
                PrinterJob job = PrinterJob.getPrinterJob();
                
                if (job.printDialog()) {
                    job.setJobName(Hl7Inspector.APPLICATION_NAME);
                    job.setPageable(new MessageRenderer());
                    job.print();
                }
//                }
            } else {
                SimpleDialog.error("No messages found to print.");
            }
        } catch (Exception ex) {
            SimpleDialog.error(ex);
        }
    }
}