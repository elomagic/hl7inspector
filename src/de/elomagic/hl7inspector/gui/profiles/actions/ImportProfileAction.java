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

package de.elomagic.hl7inspector.gui.profiles.actions;

import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.profiles.input.FileImportDialog;
import de.elomagic.hl7inspector.gui.profiles.model.ProfileModel;
import de.elomagic.hl7inspector.gui.profiles.model.SortedTableModel;
import de.elomagic.hl7inspector.utils.StringVector;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ImportProfileAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public ImportProfileAction(JTable t) {
//    public ImportProfileAction(ProfileModel m) {    
        super("Import", null);//ResourceLoader.loadImageIcon("edit.png"));
        
        table = t;
        //model = m;
        
        putValue(SHORT_DESCRIPTION, "Import data from CSV file.");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            ProfileModel model = (ProfileModel)((SortedTableModel)table.getModel()).getTableModel();
            
            FileImportDialog dlg = new FileImportDialog(model);
            if (dlg.ask()) {
                Vector<String> mapList = dlg.getMappingList();                
                
                model.lock();
                try {
                    model.clear();
                    
                    FileReader rin = new FileReader(dlg.getFile());
                    try {
                        LineNumberReader lin = new LineNumberReader(rin);
                        try {
                            int l=0;
                            String line = lin.readLine();

                            while ((line != null)) { // && (l< 10)) {
                                l++; 
                                try {
                                    if (l >= dlg.getBeginFromLine()) {
                                        StringVector lineStack = new StringVector(line, dlg.getSeparatorChar());
                                        while (lineStack.size() < mapList.size())
                                            lineStack.add(""); // Add dummies

                                        int r = model.addRowObject();                                        

                                        for (int q=0; q<mapList.size(); q++) {
                                            if (!mapList.get(q).equals("-")) {
                                                int c = model.findColumn(mapList.get(q));

                                                model.setValueAt(lineStack.get(q), r, c);                                        
                                            }
                                        }                                
                                    }
                                    
                                    line = lin.readLine();
                                } catch (Exception ee) {                                    
                                    String s = "An error occur during import line #" + l + " !"; 
                                    Logger.getLogger(getClass()).warn(s, ee);                                    
                                    if (SimpleDialog.confirmYesNo(s.concat(" ! Continue?")) == SimpleDialog.NO_OPTION) {
                                        throw ee;
                                    }
                                }
                            }
                        } finally {
                            lin.close();
                        }
                    } finally {
                        rin.close();
                    }
                } finally {
                    model.unlock();
                }
            }
        } catch (Exception ee) {
            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
            SimpleDialog.error(ee);
        }
    }
    
    private JTable table;
    //private ProfileModel model;
}
