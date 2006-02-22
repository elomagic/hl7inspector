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
import de.elomagic.hl7inspector.gui.profiles.model.ProfileModel;
import de.elomagic.hl7inspector.gui.profiles.model.SortedTableModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class AddItemAction extends AbstractAction {
    /** Creates a new instance of FileOpenAction */
    public AddItemAction(JTable t) {
        super("Add", ResourceLoader.loadImageIcon("edit_add.png"));
        
        table = t;
        
        putValue(SHORT_DESCRIPTION, "Add new item at the bottom");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            if (table.getModel() instanceof SortedTableModel) {
                SortedTableModel sm = (SortedTableModel)table.getModel();
                if (sm.getTableModel() instanceof ProfileModel) {
                    int i = ((ProfileModel)sm.getTableModel()).addRowObject();
                    
                    Rectangle r = new Rectangle(0, i, 0, i);                    
                    table.scrollRectToVisible(r);                                        
                }
            }
        } catch (Exception ee) {
            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
            SimpleDialog.error(ee, ee.getMessage());
        }
    }
    
    private JTable table;
}
