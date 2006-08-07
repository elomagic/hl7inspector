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
import de.elomagic.hl7inspector.gui.ArchiveFileSelectDialog;
import de.elomagic.hl7inspector.gui.ImportOptionBean;
import de.elomagic.hl7inspector.gui.ImportOptionsDialog;
import de.elomagic.hl7inspector.gui.ReaderProgessDialog;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.hl7.parser.MessageEncoding;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException; 
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.AbstractAction;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author rambow
 */
public class FileRecentOpenAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public FileRecentOpenAction(File _file) {
        super(_file.getAbsolutePath(), FileSystemView.getFileSystemView().getSystemIcon(_file));
        
        file = _file;
        
        putValue(SHORT_DESCRIPTION, "Size: " + _file.length() + " Bytes.");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    
    public void actionPerformed(ActionEvent e) {
        Vector<File> files = StartupProperties.getInstance().getRecentFiles();
        
        if (files.indexOf(file) != -1) {
            files.remove(file);
        }
        
        while (files.size() > 8)
            files.remove(files.size()-1);
        
        files.insertElementAt(file, 0);
        
        StartupProperties.getInstance().setRecentFiles(files);
        
        ImportOptionBean options = new ImportOptionBean();
        options.setSource(file.getAbsolutePath());
        options.setFileSize(file.length());
        options.setMessageEncoding((file.toString().toLowerCase().endsWith("xml"))?MessageEncoding.XML_FORMAT:MessageEncoding.HL7_FORMAT);
        
        try {
            InputStream fin = null;
            try {
                // Check if compressed file
                ZipFile zipFile = new ZipFile(file, ZipFile.OPEN_READ);
                
                Enumeration enu = zipFile.entries();
                
                if (!enu.hasMoreElements()) {
                    throw new FileNotFoundException("No file found inside compressed file.");
                }
                
                ZipEntry entry = (ZipEntry) enu.nextElement();
                
                if (enu.hasMoreElements()) {
                    entry = null;
                    ArchiveFileSelectDialog dialog = new ArchiveFileSelectDialog(zipFile);
                    
                    if (dialog.ask()) {
                        entry = dialog.getSelectedEntry();                        
                    }
                }
                                
                if (entry != null) {
                    options.setFileSize(entry.getSize());
                    options.setSourceLabel("Zip Archive");
                    options.setSource(entry.getName() + " (" + options.getSource() + ")");                
                    
                    fin = zipFile.getInputStream(entry);
                }
            } catch (IOException ee) {
                // No zip archive
                fin = new FileInputStream(file);
            }
            
            if (fin != null) {
                ImportOptionsDialog dlg = new ImportOptionsDialog();
                if (dlg.execute(options)) {
                    options = dlg.getImportOptions();
                    
                    try {
                        try {
                            BufferedInputStream bin = new BufferedInputStream(fin);
                            try {
                                ReaderProgessDialog readerDlg = new ReaderProgessDialog();
                                readerDlg.read(bin, options);
                            } finally {
                                bin.close();
                            }
                        } finally {
                            fin.close();
                        }
                    } catch (Exception ee) {
                        SimpleDialog.error(ee, "Error during reading file.");
                    }
                }
            }
        } catch (FileNotFoundException ee) {
            SimpleDialog.error(ee, "Error during reading file.");
        }
    }
    
    private File file;
}
