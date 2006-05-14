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

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.actions.ExitAction;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.gui.receive.ReceivePanel;
import de.elomagic.hl7inspector.gui.sender.SendPanel;
import de.elomagic.hl7inspector.hl7.model.Component;
import de.elomagic.hl7inspector.hl7.model.Field;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.hl7.model.RepetitionField;
import de.elomagic.hl7inspector.hl7.model.Segment;
import de.elomagic.hl7inspector.hl7.model.Subcomponent;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.Hl7Tree;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import de.elomagic.hl7inspector.profile.DataElement;
import de.elomagic.hl7inspector.profile.DataTypeItem;
import de.elomagic.hl7inspector.profile.MessageDescriptor;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.profile.SegmentItem;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class Desktop extends JFrame implements TreeSelectionListener, ComponentListener {
    
    /** Creates a new instance of Desktop */
    
    private Desktop() { init(null); }
    
    public final static Desktop getInstance() { return desk; }
    
    //public FindBar getFindWindow() { return findWindow; }
    public Hl7Tree getTree() { return treePane.getTree(); }
    
    public JScrollPane getScrollPane() { return treePane; }
    
    public TreeModel getModel() { return treePane.getModel(); }
    public void setModel(TreeModel model) { treePane.setModel(model); }
    
    private void init(Hl7TreeModel model) {
//    setDefaultCloseOperation(EXIT_ON_CLOSE);
        String s = Hl7Inspector.APPLICATION_NAME +
                " " +                 
                Hl7Inspector.getVersionString() +
                " (" + System.getProperty("os.arch")+"; "+System.getProperty("os.name") + ") - An Open Source project from Carsten Rambow";

        setTitle(s);
        
        StartupProperties prop = StartupProperties.getInstance();
        
        mainToolBar = new MainToolBar();
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(
                new java.awt.event.WindowAdapter() {
            public void windowActivated(WindowEvent e) { }
            public void windowClosing(WindowEvent e)   { new ExitAction().actionPerformed(null);     }
            public void windowIconified(WindowEvent e) { }
        }
        );
        
        setJMenuBar(new MainMenuBar());
        
        getContentPane().setLayout(new BorderLayout());
        
        JPanel deskToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        deskToolbar.add(mainToolBar);//, FlowLayout.LEADING);
        
        getContentPane().add(deskToolbar, BorderLayout.NORTH);        
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);               
        
        if (model != null)
            treePane = new Hl7TreePane(model);
        else
            treePane = new Hl7TreePane();
        
        treePane.getTree().getSelectionModel().addTreeSelectionListener(this);
        
        detailsPanel = new ScrollableEditorPane();
        detailsPanel.getCaption().setTitle("Node details");
        detailsPanel.addComponentListener(this);
        
        middlePanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, detailsPanel);
        middlePanel.setResizeWeight(1);
                      
        inputTrace.addComponentListener(this);        
        rp.addComponentListener(this);                
        sp.addComponentListener(this);                
        
        tabPanel = new JTabbedPane();
//        tabPanel.addTab(inputTrace.getTitle(), inputTrace.getIcon(), inputTrace);                
//        tabPanel.addTab(rp.getTitle(), rp.getIcon(), rp);
//        tabPanel.addTab(sp.getTitle(), sp.getIcon(), sp);
        tabPanel.addComponentListener(this);
        
        mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, middlePanel, tabPanel);
        mainPanel.setResizeWeight(1);        
        getContentPane().add(mainPanel, BorderLayout.CENTER);        
                
        setIconImage(ResourceLoader.loadImageIcon("hl7-icon.png").getImage());
        
        pack();                
                        
        int x = Integer.parseInt(prop.getProperty(prop.DESKTOP_X, "0"));
        int y = Integer.parseInt(prop.getProperty(prop.DESKTOP_Y, "0"));
        int w = Integer.parseInt(prop.getProperty(prop.DESKTOP_W, "800"));
        int h = Integer.parseInt(prop.getProperty(prop.DESKTOP_H, "600"));
        
        setBounds(x,y, w, h);
        
        validate();
        
        middlePanel.setDividerLocation(w-200);
               
        mainPanel.setResizeWeight(0.5);        
        mainPanel.setDividerLocation(0.75);        
        
        tabPanel.setVisible(false);       
        
        ProfileFile file = new ProfileFile(prop.getProperty(StartupProperties.DEFAULT_PROFILE, ""));
        setProfileFile(file);
        
        getDetailsWindow().setVisible(prop.isDetailsWindowVisible());
        getToolBar().getDetailsButton().setSelected(prop.isDetailsWindowVisible());
    }
    
    public void setProfileFile(ProfileFile file) {
        if (file.exists()) {
            try {
                Profile profile = Profile.getDefault();
                
                FileInputStream fin = new FileInputStream(file);
                try {
                    profile.loadFromStream(fin);
                } finally {
                    fin.close();
                }
                
                bottomPanel.setProfileText(profile.getName());
                bottomPanel.setProfileTooltTip(profile.getDescription());                
                
                if (getModel()instanceof Hl7TreeModel) {
                    if (((Hl7TreeModel)getModel()).isViewDescription()) {
                        getTree().updateUI();
                    }
                }
            } catch (Exception e) {
                bottomPanel.setProfileText("Unable to load default profile.");
                bottomPanel.setProfileTooltTip(e.getMessage());                

                Logger.getLogger(getClass()).error(e.getMessage(), e);
                SimpleDialog.error(e, "Unable to load default profile.");
            }
        } else {
            bottomPanel.setProfileText("Profile not found!");            
            bottomPanel.setProfileTooltTip("");
        }
    }        
    
    public MainToolBar getToolBar() { return mainToolBar; }
            
    private MainToolBar             mainToolBar;
    
    private BottomPanel             bottomPanel = new BottomPanel();
    
    public void setSelectedTabIndex(int index) { tabPanel.setSelectedIndex(index); }    
    
    public CharacterMonitor getInputTraceWindow() { return inputTrace; }
    private CharacterMonitor        inputTrace = new CharacterMonitor();

    public ReceivePanel getReceiveWindow() { return rp; }
    private ReceivePanel rp = new ReceivePanel();
    public SendPanel getSendWindow() { return sp; }
    private SendPanel sp = new SendPanel();    
    
    private final static Desktop    desk = new Desktop();
    
    private JSplitPane              middlePanel;
    private JSplitPane              mainPanel;
    private Hl7TreePane             treePane;
    private ScrollableEditorPane    detailsPanel;
    private JTabbedPane             tabPanel;
    
    public ScrollableEditorPane getDetailsWindow() { return detailsPanel; }
    public JTabbedPane getTabbedBottomPanel() { return tabPanel; }

    /**
     * 
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
        if (e.getNewLeadSelectionPath() != null) {
            if (e.getNewLeadSelectionPath().getPathCount() > 1) {
                if (e.getNewLeadSelectionPath().getPathComponent(1) instanceof Hl7Object) {
                    Hl7Object o = (Hl7Object)e.getNewLeadSelectionPath().getPathComponent(1);
                    if (o instanceof Message) {
                        Message m = (Message)o;

                        bottomPanel.setStatusText("Source: ".concat(m.getSource().length()==0?"Unknown":m.getSource()));
                    }
                }
                
                if (e.getNewLeadSelectionPath().getLastPathComponent() instanceof Hl7Object) {
                    showHl7ObjectDetails((Hl7Object)e.getNewLeadSelectionPath().getLastPathComponent());
                }                
            }
        }
    }
    
    private void showHl7ObjectDetails(Hl7Object o) {
        String s = "";
        
        
        if (detailsPanel.isVisible()) {            
            String NO_DESCRIPTION_FOUND = "No description in profile found.";                
    
            MessageDescriptor md = new MessageDescriptor(Profile.getDefault());
            
            s = md.getDescription(o, true);
            
            if (s.length() == 0) {
                s = NO_DESCRIPTION_FOUND;
            }
        }
        
        detailsPanel.getEditorPane().setText(s);        
    }
    
    public void setTabVisible(JComponent o) {
        int i = getTabbedBottomPanel().indexOfComponent(o);
        
        if (i == -1) {
            if (o instanceof CharacterMonitor) {
                CharacterMonitor cm = (CharacterMonitor)o;
                getTabbedBottomPanel().addTab(cm.getTitle(), cm.getIcon(), cm);
                
                getTabbedBottomPanel().setSelectedComponent(o);
            }
        } else {
            getTabbedBottomPanel().remove(o);
        }        
        
        getTabbedBottomPanel().setVisible(getTabbedBottomPanel().getTabCount() != 0);
    }

    // Interface ComponentListener
    public void componentShown(ComponentEvent e) {
        if (e.getSource().equals(getDetailsWindow())) {
            StartupProperties.getInstance().setDetailsWindowVisible(getDetailsWindow().isVisible());
            middlePanel.setDividerLocation(getSize().width-200);
            middlePanel.setDividerSize(4);
            getToolBar().getDetailsButton().setSelected(true);
                                                
            if (getTree().getSelectionCount() == 1) {
                showHl7ObjectDetails((Hl7Object)getTree().getSelectionPath().getLastPathComponent());                
            }
        } else if (e.getSource().equals(getTabbedBottomPanel())) {
            mainPanel.setDividerSize(4);
            mainPanel.setDividerLocation(0.75);        
//            getToolBar().getDetailsButton().setSelected(true);
        }        
    }

    public void componentResized(ComponentEvent e) { }

    public void componentMoved(ComponentEvent e) { }

    public void componentHidden(ComponentEvent e) {
        if (e.getSource().equals(getDetailsWindow())) {
            StartupProperties.getInstance().setDetailsWindowVisible(getDetailsWindow().isVisible());
            getToolBar().getDetailsButton().setSelected(false);            
            if (middlePanel.isVisible()) {
                middlePanel.setDividerSize(0);
            }
        } else if (e.getSource().equals(getTabbedBottomPanel())) {
            if (mainPanel.isVisible()) {
                mainPanel.setDividerSize(0);
            }
        }
    }  
}
