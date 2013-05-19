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

import de.elomagic.hl7inspector.gui.ImportOptionBean;
import de.elomagic.hl7inspector.gui.ImportOptionsDialog;
import de.elomagic.hl7inspector.gui.ReaderProgessDialog;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 *
 * @author rambow
 */
public class PasteTextAction extends BasicAction {
    /**
     * Creates a new instance of PasteTextAction.
     */
    public PasteTextAction() {
        super();

        putValue(NAME, "Import From Clipboard...");
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("document-open.png"));
        putValue(SHORT_DESCRIPTION, "Import HL7 messages from clipboard");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            String text = clipboard.getData(DataFlavor.stringFlavor).toString();

            importText(text);
        } catch(UnsupportedFlavorException | IOException ex) {
            SimpleDialog.error(ex, "Error during importing text");
        }
    }

    public static void importText(String text) {
        ImportOptionBean options = new ImportOptionBean();
        options.setSource("Text string");
        options.setFileSize(text.length());

        ImportOptionsDialog dlg = new ImportOptionsDialog();
        if(dlg.execute(options)) {
            options = dlg.getImportOptions();

            try {
                try (ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes(options.getEncoding())); BufferedInputStream bin = new BufferedInputStream(in)) {
                    ReaderProgessDialog readerDlg = new ReaderProgessDialog();
                    readerDlg.read(bin, options);
                }
            } catch(Exception ee) {
                SimpleDialog.error(ee, "Error during parsing text");
            }
        }
    }
}
