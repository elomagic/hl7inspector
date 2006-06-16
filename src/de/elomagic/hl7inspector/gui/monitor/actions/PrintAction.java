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

package de.elomagic.hl7inspector.gui.monitor.actions;

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.print.MessageRenderer;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import javax.swing.AbstractAction;

/**
 *
 * @author rambow
 */
public class PrintAction extends AbstractAction {
    
    /** Creates a new instance of FileNewAction */
    public PrintAction(CharacterMonitor d) {
        super("", ResourceLoader.loadImageIcon("document-print.png"));//icon);
        
        dlg = d;
        
        putValue(SHORT_DESCRIPTION, "Print trace log");
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
//            String s = StringEscapeUtils.unescapeHtml(dlg.getText()).trim();
            
//            if (s.length() != 0) {
            PrinterJob job = PrinterJob.getPrinterJob();
            
            if (job.printDialog()) {
                job.setJobName(Hl7Inspector.APPLICATION_NAME);
                job.setPageable(new MessageRenderer(dlg.getTextPane()));
                job.print();
            }
//            } else {
//                SimpleDialog.error("No log to print.");
//            }
        } catch (Exception ex) {
            SimpleDialog.error(ex);
        }
    }
    
    private CharacterMonitor dlg;
}
