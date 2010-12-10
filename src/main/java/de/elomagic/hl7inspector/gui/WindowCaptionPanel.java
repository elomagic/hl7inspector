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

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author rambow
 */
public class WindowCaptionPanel extends JPanel {
    
    /** Creates a new instance of WindowCaptionPanel */
    public WindowCaptionPanel() {
        init();
    }
    
    public WindowCaptionPanel(String title) {
        init();
                
        setTitle(title);
    }        
    
    public final void init() {
        setLayout(new BorderLayout());
        
        lbCaption = new JLabel();
        lbCaption.setBorder(new BevelBorder(BevelBorder.RAISED));
        btClose = new JButton();
        btClose.setVisible(false);
        
        add(lbCaption, BorderLayout.CENTER);
        add(btClose, BorderLayout.EAST);        
    }
    
    private JLabel  lbCaption;
    private JButton btClose;
    
    public final void setTitle(String title) { lbCaption.setText(title); }
    public void setCloseButton(boolean visible) { btClose.setVisible(visible); }
}
