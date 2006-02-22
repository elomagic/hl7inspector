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

import com.l2fprod.common.swing.BannerPanel;
import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRootPane;
/**
 *
 * @author rambow
 */
public abstract class AbstractPanel extends JPanel implements ActionListener {
    
    /** Creates a new instance of AbstractOptionsPane */
    public AbstractPanel(PanelDialog d) { super(new BorderLayout()); dialog = d; _init(); }
    
    public JButton getBarButton() { 
        if (button == null) {
            button = new JButton(new AbstractAction() {               
                public void actionPerformed(ActionEvent e) { select(); }                
            });
                        
            button.setText(getTitle());
            button.setIcon(getIcon());
            button.setToolTipText(getDescription());
        }
                              
        return button; 
    }    
    
    public abstract Icon getIcon();
    
    public abstract String getTitle();
    
    public abstract String getDescription();
    
    public abstract void read();

    public abstract void write();
    
    
    public void select() { dialog.setSelected(this); }
    
    public void actionPerformed(ActionEvent e) { select(); }        

    protected abstract void init();    
    
    protected void _init() {        
        add(banner, BorderLayout.NORTH);
        banner.setBackground(SystemColor.textHighlight);                
        banner.setTitle(getTitle());
        banner.setTitleColor(SystemColor.textHighlightText);
        banner.setIcon(getIcon());
        banner.setVisible(true);             
        
        add(new JRootPane(), BorderLayout.CENTER);
        
        init();
    }
        
    protected PanelDialog getDialog() { return dialog; }
    
    private PanelDialog dialog;

    private BannerPanel banner = new BannerPanel();    
    
    private JButton button;
}
