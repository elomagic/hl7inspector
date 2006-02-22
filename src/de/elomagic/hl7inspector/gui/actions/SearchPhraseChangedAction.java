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

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.FindBar;
import javax.swing.event.DocumentListener;

/**
 *
 * @author rambow
 */
public class SearchPhraseChangedAction implements DocumentListener {
    
    /** Creates a new instance of SearchPhraseChangedAction */
    public SearchPhraseChangedAction() { }
    
    public void removeUpdate(javax.swing.event.DocumentEvent e) { doActionPerforme(); }
    
    public void insertUpdate(javax.swing.event.DocumentEvent e) { doActionPerforme(); }
    
    public void changedUpdate(javax.swing.event.DocumentEvent e) { doActionPerforme(); }
    
    private void doActionPerforme() {
        if (FindBar.getInstance().isHighlight()) {
            Desktop.getInstance().getTree().repaint(50);
        }
    }
}
