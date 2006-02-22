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

package de.elomagic.hl7inspector.gui.monitor;

import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.io.IOThreadListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author rambow
 */
public class CharacterMonitor extends JPanel implements IOThreadListener {
    
    /** Creates a new instance of CharacterMonitor */
    public CharacterMonitor() {
        setLayout(new BorderLayout());
        
        tb = new MonitorToolBar(this);        
        hp = new HighlighterPane();
        sp = new JScrollPane(hp);        
        
        add(tb, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(400, 200));        
    }
    
    public  String getTitle() { return "Input Trace"; }
    public  ImageIcon getIcon() { return ResourceLoader.loadImageIcon("trace.png"); }
    
    public void addChar(char c) { hp.addChar(c); }
    
    public void addLine(String value) { hp.addLine(value); }
    
    public void clear() { hp.clear(); }
    
    public String getText() { return hp.getText(); }
    
    public MonitorToolBar getToolBar() { return tb; }
    
    private MonitorToolBar  tb;
    private JScrollPane	    sp;
    private HighlighterPane hp;
    
    // Interface IOThreadListener
    public void threadStopped(Thread source) { }
    public void threadStarted(Thread source) { }
    public void charSend(Object source, char c) { addChar(c); }
    public void charReceived(Object source, char c) { addChar(c); }
    public void status(Thread source, String status) { addLine(status); }
}
