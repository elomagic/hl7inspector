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

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.actions.*;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import java.io.File;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author rambow
 */
public class MainMenuBar extends JMenuBar {
    
    /** Creates a new instance of MainMenu */
    public MainMenuBar() { init(); }
    
    protected void init() {
        JMenu miFile = new JMenu("File");
        miFile.add(new JMenuItem(new FileNewAction()));
        miFile.add(new JMenuItem(new FileOpenAction()));
        miFile.add(miOpenRecentFiles);
        miFile.add(new JMenuItem(new FileSaveAsAction()));
        /* FEATURE Print message support needed
        menuItem.addSeparator();
        menuItem.add(new JMenuItem(new PrintAction())); */
        miFile.addSeparator();
        miFile.add(new JMenuItem(new ExitAction()));
        miFile.addChangeListener(new RecentFileMenuListener());
        add(miFile);
        
/*        menuItem = new JMenu("Edit");
        menuItem.add(new JMenuItem(new DeleteMessageAction()));
        add(menuItem);*/
        
        JMenu menuItem = new JMenu("Search");
        menuItem.add(new JMenuItem(new FindWindowAction()));
        add(menuItem);        
        
        viewMenu.addChangeListener(new ViewMenuListener());
        viewMenu.add(miCompactView);
        viewMenu.addSeparator();
        viewMenu.add(miNodeDescription);
        viewMenu.add(miNodeDetails);
        viewMenu.addSeparator();
        viewMenu.add(new JMenuItem(new ValidateMessageAction()));
        add(viewMenu);
                
        menuItem = new JMenu("Tools");
        menuItem.add(new JMenuItem(new ViewTextFile()));
        menuItem.add(new JMenuItem(new ViewHexFile()));
        menuItem.addSeparator();
        menuItem.add(miParseWindow);
        menuItem.add(miReceiveWindow);
        menuItem.add(miSendWindow);
        menuItem.addSeparator();
        menuItem.add(new JMenuItem(new ProfileManagerAction()));
        menuItem.addSeparator();
        menuItem.add(new JMenuItem(new OptionsAction()));
        add(menuItem);
        
        /*menuItem = new JMenu("Window");
        menuItem.add(new JMenuItem(new DetailWindowAction()));
        add(menuItem);*/
        
        menuItem = new JMenu("Help");
        menuItem.add(new JMenuItem(new CheckUpdateAction()));
        menuItem.addSeparator();
        menuItem.add(new JMenuItem(new AboutAction()));
        add(menuItem);
    }
    
    private void createRecentFilesMenu() {
        miOpenRecentFiles.removeAll();
        
        Vector v = StartupProperties.getInstance().getRecentFiles();
        for (int i=0;i<v.size();i++) {
            JMenuItem mi = new JMenuItem(new FileRecentOpenAction(((File)v.get(i))));
            miOpenRecentFiles.add(mi);
        }
        
        miOpenRecentFiles.setEnabled(v.size() != 0);
    }
    
    private JMenu               miOpenRecentFiles   = new JMenu("Open recent files");
    private JMenu               miFile;
    private JMenu               viewMenu            = new JMenu("View");
    private JCheckBoxMenuItem   miCompactView       = new JCheckBoxMenuItem(new ViewCompressedAction());
    private JCheckBoxMenuItem   miNodeDescription   = new JCheckBoxMenuItem(new ViewNodeDescriptionAction());
    private JCheckBoxMenuItem   miNodeDetails       = new JCheckBoxMenuItem(new ViewNodeDetailsAction());
    private JCheckBoxMenuItem   miParseWindow       = new JCheckBoxMenuItem(new ShowParserWindowAction());
    private JCheckBoxMenuItem   miReceiveWindow     = new JCheckBoxMenuItem(new ReceiveMessageAction());
    private JCheckBoxMenuItem   miSendWindow        = new JCheckBoxMenuItem(new SendMessageAction());
    
    class RecentFileMenuListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            if (((JMenuItem)e.getSource()).isSelected()) {
                createRecentFilesMenu();                
            }
        }
    }
    
    class ViewMenuListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            if (((JMenuItem)e.getSource()).isSelected()) {
                Hl7TreeModel model = (Hl7TreeModel)Desktop.getInstance().getTree().getModel();
                               
                miCompactView.setSelected(model.isCompactView());
                miNodeDescription.setSelected(model.isViewDescription());
                miNodeDetails.setSelected(Desktop.getInstance().getDetailsWindow().isVisible());                
                miParseWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getInputTraceWindow()) != -1);
                miReceiveWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getReceiveWindow()) != -1);
                miSendWindow.setSelected(Desktop.getInstance().getTabbedBottomPanel().indexOfComponent(Desktop.getInstance().getSendWindow()) != -1);
            }
        }
    }    
}
