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

import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.print.HFFormat;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.swing.AbstractAction;

/**
 *
 * @author rambow
 */
public class PrintSetupAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public PrintSetupAction() {
        super("Page Setup...", ResourceLoader.loadImageIcon("document-properties.png"));
        
        putValue(SHORT_DESCRIPTION, "Page Setup.");
    }
    
    public void actionPerformed(ActionEvent e) {
        PrinterJob job = PrinterJob.getPrinterJob();
        
        if (job.getPrintService() != null) {
            PageFormat defFormat = HFFormat.getInstance();
            PageFormat newFormat = job.pageDialog(defFormat);
            
            if (defFormat != newFormat) {
                HFFormat.getInstance().setPaper(newFormat.getPaper());
                HFFormat.getInstance().setOrientation(newFormat.getOrientation());
            }
        } else {
            SimpleDialog.error("No printer available.");
        }
    }
}
