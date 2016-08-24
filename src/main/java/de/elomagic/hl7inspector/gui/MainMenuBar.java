/*
 * Copyright 2016 Carsten Rambow
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
 */
package de.elomagic.hl7inspector.gui;

import java.io.File;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.actions.*;
import de.elomagic.hl7inspector.gui.dialogs.about.AboutDialog;
import de.elomagic.hl7inspector.gui.options.OptionsDialog;
import de.elomagic.hl7inspector.hl7.model.EncodingObject;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.mac.MacApplication;
import de.elomagic.hl7inspector.mac.MacApplicationAdapter;
import de.elomagic.hl7inspector.mac.MacApplicationEvent;

/**
 *
 * @author rambow
 */
public class MainMenuBar extends JMenuBar {

    private final JMenu miFile = new JMenu("File");
    private final JMenu miOpenRecentFiles = new JMenu("Open recent files");
    private final JMenu miEdit = new JMenu("Edit");
    private final JMenuItem miEditItem = new JMenuItem(new EditMessageItemAction());
    private final JMenuItem miEditAppendItem = new JMenuItem(new AddMessageItemAction());
    private final JMenuItem miEditRemoveItem = new JMenuItem(new RemoveMessageItemAction());
    private final JMenuItem miEditRemoveMessage = new JMenuItem(new RemoveMessageAction());
    private final JMenu viewMenu = new JMenu("View");
    private final JCheckBoxMenuItem miCompressedView = new JCheckBoxMenuItem(new ViewCompressedAction());
    private final JCheckBoxMenuItem miNodeDescription = new JCheckBoxMenuItem(new ViewNodeDescriptionAction());
    private final JCheckBoxMenuItem miNodeDetails = new JCheckBoxMenuItem(new ViewNodeDetailsAction());
    private final JCheckBoxMenuItem miTraceWindow = new JCheckBoxMenuItem(new ShowParserWindowAction());
    private final JCheckBoxMenuItem miReceiveWindow = new JCheckBoxMenuItem(new ShowReceiveWindowAction(true));
    private final JCheckBoxMenuItem miSendWindow = new JCheckBoxMenuItem(new ShowSendWindowAction(true));
    private final JMenu miHelp = new JMenu("Help");

    /**
     * Creates a new instance of MainMenuBar.
     */
    public MainMenuBar() {
        super();

        init();
    }

    private void init() {
        //buildRecentUsedKeystoresMenu();

        MacApplication.getApplication().addApplicationListener(new MacApplicationAdapter() {
            @Override
            public void handleAbout(MacApplicationEvent ae) {
                new AboutDialog().setVisible(true);

                ae.setHandled(true);
            }

            @Override
            public void handleQuit(MacApplicationEvent ae) {
                ae.setHandled(true);
                System.exit(0);
            }

            @Override
            public void handlePreferences(MacApplicationEvent ae) {
                OptionsDialog dlg = new OptionsDialog();
                dlg.ask();

                ae.setHandled(true);
            }
        });
        MacApplication.getApplication().setEnabledAboutMenu(true);
        MacApplication.getApplication().setEnabledPreferencesMenu(true);

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

        miEdit.addChangeListener(new EditMenuListener());
        miEdit.add(miEditItem);
        miEdit.add(miEditAppendItem);
        miEdit.add(miEditRemoveItem);
        miEdit.add(miEditRemoveMessage);
        add(miEdit);

        JMenu menuItem = new JMenu("Search");
        menuItem.add(new JMenuItem(new FindWindowAction()));
        add(menuItem);

        viewMenu.addChangeListener(new ViewMenuListener());
        viewMenu.add(miCompressedView);
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
        menuItem.add(miTraceWindow);
        menuItem.add(miReceiveWindow);
        menuItem.add(miSendWindow);
        menuItem.addSeparator();
        menuItem.add(new JMenuItem(new ProfileManagerAction()));

        if(StartupProperties.getInstance().isDebugFileOutput()) {
            menuItem.add(new JMenuItem(new KeyStoreManagerAction()));
        }

        if(!MacApplication.isMacOS()) {
            menuItem.addSeparator();
            menuItem.add(new JMenuItem(new OptionsAction()));
        }

        add(menuItem);

        /*menuItem = new JMenu("Window");
         menuItem.add(new JMenuItem(new DetailWindowAction()));
         add(menuItem);*/
        miHelp.add(new JMenuItem(new CheckUpdateAction()));
        miHelp.add(new JMenuItem(new VisitWebSiteAction("Visit HL7Inspector NEO", "http://www.hl7inspector.com")));

        if(!MacApplication.isMacOS()) {
            miHelp.addSeparator();
            miHelp.add(new JMenuItem(new AboutAction()));
        }

        add(miHelp);
    }

    private void createRecentFilesMenu() {
        miOpenRecentFiles.removeAll();

        List<File> list = StartupProperties.getInstance().getRecentFiles();
        for(File file : list) {
            JMenuItem mi = new JMenuItem(new FileRecentOpenAction(file));
            miOpenRecentFiles.add(mi);
        }

        miOpenRecentFiles.setEnabled(!list.isEmpty());
    }

    class RecentFileMenuListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if(((JMenuItem)e.getSource()).isSelected()) {
                createRecentFilesMenu();
            }
        }
    }

    class EditMenuListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if(((JMenuItem)e.getSource()).isSelected()) {

                for(int i = 0; i < miEdit.getItemCount(); i++) {
                    miEdit.getItem(i).setEnabled(false);
                }

                List<Hl7Object> selectedObjects = Desktop.getInstance().getSelectedObjects();

                if(!selectedObjects.isEmpty()) {
                    Hl7Object hl7o = selectedObjects.get(0);

                    if(!(hl7o instanceof EncodingObject)) {
                        miEditItem.setEnabled(!(hl7o instanceof Message));
                        miEditAppendItem.setEnabled(hl7o.getChildClass() != null);
                        miEditRemoveItem.setEnabled(!(hl7o instanceof Message));
                        miEditRemoveMessage.setEnabled(true);
                    }
                }
            }
        }
    }

    class ViewMenuListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if(((JMenuItem)e.getSource()).isSelected()) {
                DesktopIntf d = Desktop.getInstance();

                miCompressedView.setSelected(d.isCompressedView());
                miNodeDescription.setSelected(d.isNodeDescriptionVisible());
                miNodeDetails.setSelected(d.isNodeDetailsWindowVisible());
                miTraceWindow.setSelected(d.isInputTraceWindowVisible());
                miReceiveWindow.setSelected(d.isReceiveWindowVisible());
                miSendWindow.setSelected(d.isSendWindowVisible());
            }
        }
    }
}
