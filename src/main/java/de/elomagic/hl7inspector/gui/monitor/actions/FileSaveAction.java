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

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.file.filters.TextFileFilter;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class FileSaveAction extends AbstractAction {

    /** Creates a new instance of FileSaveAsAction */
    public FileSaveAction(CharacterMonitor d) {
        super("", ResourceLoader.loadImageIcon("document-save.png"));

        dlg = d;

        putValue(SHORT_DESCRIPTION, "Save trace log...");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /** TODODialog must be top mosted. Actual it will be toped by receive/send window. */
        JFileChooser fc = new JFileChooser(StartupProperties.getInstance().getLastSaveFolder());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle("Save trace log dialog");
        fc.setSelectedFile(new File(StartupProperties.getInstance().getLastSaveFolder().getAbsolutePath().concat("\\hl7_receive_trace_log.txt")));
        fc.addChoosableFileFilter(new TextFileFilter());

        if (fc.showSaveDialog(Desktop.getInstance()) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (file.exists()) {
                if (SimpleDialog.confirmYesNo("File already exists. Overwrite?") == JOptionPane.YES_OPTION) {
                    try {
                        FileOutputStream fout = new FileOutputStream(file, false);
                        try {
                            BufferedOutputStream bout = new BufferedOutputStream(fout);
                            try {
                                bout.write(dlg.getText().getBytes());

                                bout.flush();
                            } finally {
                                bout.close();
                            }
                        } finally {
                            fout.close();
                        }
                    } catch (Exception ee) {
                        Logger.getLogger(getClass()).error("Writing trace log file failed.", ee);
                    }
                }
            }
        }

    }

    private CharacterMonitor dlg;
}
