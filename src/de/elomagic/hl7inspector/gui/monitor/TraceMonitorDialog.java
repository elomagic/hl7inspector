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

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.ToolKit;
import java.awt.BorderLayout;
import javax.swing.JDialog;

/**
 *
 * @author rambow
 */
public abstract class TraceMonitorDialog extends JDialog {

    /** Creates a new instance of TestMonitorDialog */
    public TraceMonitorDialog(String title) {
        super(Desktop.getInstance(), title, false);
        setLayout(new BorderLayout());
    
        init();

        setAlwaysOnTop(true);
                
        pack();
        
        setSize(600, 200);    
    }

    protected abstract void init();
    
    public void setVisible(boolean v) {
        if ((v) && (!isVisible())) {
            setBounds(ToolKit.centerFrame(this, Desktop.getInstance()));
        } 
        
        super.setVisible(v);
    }            
}
