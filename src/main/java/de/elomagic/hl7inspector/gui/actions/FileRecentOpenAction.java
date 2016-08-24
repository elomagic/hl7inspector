/*
 * Copyright 2016 Carsten Rambow
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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.filechooser.FileSystemView;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.ImportOptionBean;
import de.elomagic.hl7inspector.gui.ImportOptionsDialog;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.ReaderProgessDialog;

/**
 *
 * @author Carsten Rambow
 */
public class FileRecentOpenAction extends BasicAction {

    private final File file;

    /**
     * Creates a new instance of FileRecentOpenAction.
     *
     * @param file
     */
    public FileRecentOpenAction(File file) {
        super();

        this.file = file;

        putValue(NAME, file.getAbsolutePath());
        putValue(SMALL_ICON, FileSystemView.getFileSystemView().getSystemIcon(file));
        putValue(SHORT_DESCRIPTION, "Size: " + file.length() + " Bytes.");
        putValue(MNEMONIC_KEY, KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        List<File> files = StartupProperties.getInstance().getRecentFiles();

        if(files.indexOf(file) != -1) {
            files.remove(file);
        }

        while(files.size() > 8) {
            files.remove(files.size() - 1);
        }

        files.add(0, file);

        StartupProperties.getInstance().setRecentFiles(files);

        ImportOptionBean options = new ImportOptionBean();
        options.setSource(file.getAbsolutePath());
        options.setFileSize(file.length());

        ImportOptionsDialog dlg = new ImportOptionsDialog();
        if(dlg.execute(options)) {
            options = dlg.getImportOptions();

            try {
                InputStream fin;
                try {
                    // Check if compressed file
                    ZipFile zipFile = new ZipFile(file);

                    Enumeration enu = zipFile.entries();

                    if(!enu.hasMoreElements()) {
                        throw new Exception("No file found inside compressed file.");
                    }

                    ZipEntry entry = (ZipEntry)enu.nextElement();
                    options.setFileSize(entry.getSize());
                    options.setSourceLabel("Zip Archive");
                    //options.setSource(entry.getName());

                    // FEATURE "ChooseDialog" must popup when using archive with more then one file.
                    if(enu.hasMoreElements()) {
                        Notification.info("The file includes more then one compressed file.\nRead file " + entry.getName() + ".");
                    }

                    fin = zipFile.getInputStream(entry);
                } catch(Exception ee) {
                    // No zip archive
                    fin = new FileInputStream(file);
                }

                try {
                    try (BufferedInputStream bin = new BufferedInputStream(fin)) {
                        ReaderProgessDialog readerDlg = new ReaderProgessDialog();
                        readerDlg.read(bin, options);
                    }
                } finally {
                    fin.close();
                }
            } catch(Exception ee) {
                Notification.error(ee, "Error during reading file.");
            }
        }
    }
}
