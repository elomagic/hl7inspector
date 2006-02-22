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

package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.gui.actions.*;
import de.elomagic.hl7inspector.utils.StringEscapeUtils;
import java.awt.BorderLayout;
import javax.swing.*;

/**
 *
 * @author rambow
 */
public class FindBar extends JPanel {
    
    /** Creates a new instance of FindWindow */
    private FindBar() {
        super(new BorderLayout());
        
        JToolBar bar = new JToolBar();
        
        editPhrase.getDocument().addDocumentListener(new SearchPhraseChangedAction());
        editPhrase.addKeyListener(new FindCloseWindowAction());
        
        btNext = new JButton(new FindNextAction());

        bar.setRollover(true);
        bar.setFloatable(true);
        
        
        btnClose.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        bar.add(btnClose);
        bar.add(editPhrase);
        bar.add(btNext);
        bar.add(btHighlight);
        bar.add(cbCaseSensitive);
        
        add(bar, BorderLayout.WEST);
        
        setVisible(false);
    }
    
    public final static FindBar getInstance() { return instance; }
     
    private String escapedPhrase = "";
    private String phrase = "";
    
    public String getEscapedPhrase() { 
        if (!phrase.equals(editPhrase.getText())) {
            escapedPhrase = StringEscapeUtils.escapeHtml(editPhrase.getText());
        }
                
        return escapedPhrase;
    }
    
    public boolean isCaseSensitive() { return cbCaseSensitive.isSelected(); }
    public boolean isHighlight() { return btHighlight.isSelected(); }
    public void requestFocus() { editPhrase.requestFocus(); }
    
    public void setVisible(boolean value) {
        super.setVisible(value);
        
        if (value) {
            btNext.getRootPane().setDefaultButton(btNext);
        }
    }
    
    private final static FindBar instance = new FindBar();
    
    private JTextField      editPhrase      = new JTextField(10);
    private JButton         btnClose        = new JButton(new FindCloseWindowAction());
    private JButton         btNext;
    private JCheckBox       cbCaseSensitive = new JCheckBox(new FindCaseSensitiveAction());
    private JToggleButton   btHighlight     = new JToggleButton(new FindHightlightAction());
}
