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
package de.elomagic.hl7inspector.print;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
//import java.awt.print.PrinterJob;

/**
 *
 * @author rambow
 */
public class MessageRenderer implements Printable {
    /** Creates a new instance of MessageRenderer */
    public MessageRenderer() {
//        PrinterJob pjob = PrinterJob.getPrinterJob();
//        PageFormat pf = pjob.defaultPage();
//        pjob.setPrintable(this);
//        try {
//            if (pjob.printDialog()) {
//                pjob.print();
//            }
//        } catch (PrinterException e) {
//        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}
