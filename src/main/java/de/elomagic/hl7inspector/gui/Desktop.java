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
 */
package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.actions.ExitAction;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.gui.receive.ReceivePanel;
import de.elomagic.hl7inspector.gui.sender.SendPanel;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.hl7.model.RepetitionField;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.Hl7Tree;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import de.elomagic.hl7inspector.model.TreeNodeSearchEngine;
import de.elomagic.hl7inspector.profile.MessageDescriptor;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.profile.ProfileIO;
import de.elomagic.hl7inspector.utils.BundleTool;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class Desktop extends JFrame implements DesktopIntf, TreeSelectionListener, ComponentListener {
    private static final long serialVersionUID = -7355763607097590182L;
    private MainToolBar mainToolBar;
    private BottomPanel bottomPanel = new BottomPanel();
    private CharacterMonitor inputTrace = new CharacterMonitor();
    private ReceivePanel rp = new ReceivePanel();
    private SendPanel sp = new SendPanel();
    private final static Desktop desk = new Desktop();
    private JSplitPane middlePanel;
    private JSplitPane mainPanel;
    private Hl7TreePane treePane;
    private ScrollableEditorPane detailsPanel;
    private JTabbedPane tabPanel;
    private ResourceBundle bundle = BundleTool.getBundle(Desktop.class);

    /**
     * Creates a new instance of Desktop.
     */
    private Desktop() {
        init(null);
    }

    public static DesktopIntf getInstance() {
        return desk;
    }

    public Hl7Tree getTree() {
        return treePane.getTree();
    }

    public JScrollPane getScrollPane() {
        return treePane;
    }

    public Hl7TreeModel getModel() {
        return (Hl7TreeModel)treePane.getModel();
    }

    private void init(Hl7TreeModel model) {
        String s = MessageFormat.format(bundle.getString("app_title"),
                                        Hl7Inspector.APPLICATION_NAME,
                                        Hl7Inspector.getVersionString(),
                                        System.getProperty("os.arch"),
                                        System.getProperty("os.name"));

        setTitle(s);

        StartupProperties prop = StartupProperties.getInstance();

        mainToolBar = new MainToolBar();

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(
                new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                new ExitAction().actionPerformed(null);
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }
        });

        setJMenuBar(new MainMenuBar());

        getContentPane().setLayout(new BorderLayout());

        JPanel deskToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        deskToolbar.add(mainToolBar);//, FlowLayout.LEADING);

        getContentPane().add(deskToolbar, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        treePane = (model == null) ? new Hl7TreePane() : new Hl7TreePane(model);
        treePane.getTree().getSelectionModel().addTreeSelectionListener(this);

        detailsPanel = new ScrollableEditorPane();
        detailsPanel.getCaption().setTitle(bundle.getString("node_details"));
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

        int x = Integer.parseInt(prop.getProperty(StartupProperties.DESKTOP_X, "0"));
        int y = Integer.parseInt(prop.getProperty(StartupProperties.DESKTOP_Y, "0"));
        int w = Integer.parseInt(prop.getProperty(StartupProperties.DESKTOP_W, "800"));
        int h = Integer.parseInt(prop.getProperty(StartupProperties.DESKTOP_H, "600"));

        setBounds(x, y, w, h);

        validate();

        middlePanel.setDividerLocation(w - 200);

        mainPanel.setResizeWeight(0.5);
        mainPanel.setDividerLocation(0.75);

        tabPanel.setVisible(false);

        ProfileFile file = new ProfileFile(prop.getProperty(StartupProperties.DEFAULT_PROFILE, ""));
        Profile profile = setProfileFile(file);
        if(profile != null) {
            ProfileIO.setDefault(profile);
        }

        getDetailsWindow().setVisible(prop.isDetailsWindowVisible());
        getToolBar().getDetailsButton().setSelected(prop.isDetailsWindowVisible());
    }

    @Override
    public Profile setProfileFile(final ProfileFile file) {
        Profile profile = null;

        if(file.exists()) {
            try {
                try (FileInputStream fin = new FileInputStream(file)) {
                    profile = ProfileIO.load(fin);
                } catch(Exception e) {
                    profile = ProfileIO.getDefault();
                }

                bottomPanel.setProfileText(profile.getName());
                bottomPanel.setProfileTooltTip(profile.getDescription());

                if(getModel() instanceof Hl7TreeModel) {
                    if(((Hl7TreeModel)getModel()).isNodeDescriptionVisible()) {
                        getTree().updateUI();
                    }
                }
            } catch(Exception e) {
                bottomPanel.setProfileText("Unable to load default profile.");
                bottomPanel.setProfileTooltTip(e.getMessage());

                Logger.getLogger(getClass()).error(e.getMessage(), e);
                SimpleDialog.error(e, "Unable to load default profile.");
            }
        } else {
            bottomPanel.setProfileText(bundle.getString("profile_not_found"));
            bottomPanel.setProfileTooltTip("");
        }
        return profile;
    }

    public MainToolBar getToolBar() {
        return mainToolBar;
    }

    public void setSelectedTabIndex(int index) {
        tabPanel.setSelectedIndex(index);
    }

    public ReceivePanel getReceiveWindow() {
        return rp;
    }

    public SendPanel getSendWindow() {
        return sp;
    }

    public ScrollableEditorPane getDetailsWindow() {
        return detailsPanel;
    }

    public JTabbedPane getTabbedBottomPanel() {
        return tabPanel;
    }

    /**
     *
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if(e.getNewLeadSelectionPath() != null) {
            if(e.getNewLeadSelectionPath().getPathCount() > 1) {
                if(e.getNewLeadSelectionPath().getPathComponent(1) instanceof Hl7Object) {
                    Hl7Object o = (Hl7Object)e.getNewLeadSelectionPath().getPathComponent(1);
                    if(o instanceof Message) {
                        Message m = (Message)o;

                        bottomPanel.setStatusText("Source: ".concat(m.getSource().length() == 0 ? "Unknown" : m.getSource()));
                    }
                }

                if(e.getNewLeadSelectionPath().getLastPathComponent() instanceof Hl7Object) {
                    showHl7ObjectDetails((Hl7Object)e.getNewLeadSelectionPath().getLastPathComponent());
                }
            }
        }
    }

    private void showHl7ObjectDetails(Hl7Object o) {
        String s = "";


        if(detailsPanel.isVisible()) {
            String NO_DESCRIPTION_FOUND = "No description in profile found.";

            MessageDescriptor md = new MessageDescriptor(ProfileIO.getDefault());

            s = md.getDescription(o, true);

            if(s.isEmpty()) {
                s = NO_DESCRIPTION_FOUND;
            }
        }

        detailsPanel.getEditorPane().setText(s);
    }

    public void setTabVisible(JComponent o) {
        int i = getTabbedBottomPanel().indexOfComponent(o);

        if(i == -1) {
            if(o instanceof CharacterMonitor) {
                CharacterMonitor cm = (CharacterMonitor)o;
                getTabbedBottomPanel().addTab(cm.getTitle(), cm.getIcon(), cm);

                getTabbedBottomPanel().setSelectedComponent(o);
            }
        } else {
            getTabbedBottomPanel().remove(o);
        }

        getTabbedBottomPanel().setVisible(getTabbedBottomPanel().getTabCount() != 0);
    }

    @Override
    public Frame getMainFrame() {
        return this;
    }

    @Override
    public void clearMessages() {
        treePane.setModel(new Hl7TreeModel());
    }

    /**
     * Adds message to the view.
     *
     * @param messages List if messages
     * @param maxMessageInView Maximum message in the view
     * @param readBottom When true message will be removed from the top
     */
    @Override
    public void addMessages(final List<Message> messages, final int maxMessageInView, final boolean readBottom) {
        Hl7TreeModel model = getModel();
        model.locked();
        try {
            for(final Message message : messages) {
                model.addMessage(message);

                // Check buffer overflow
                while(model.getChildCount(model) > maxMessageInView) {
                    if(readBottom) {
                        model.removeChild(model, readBottom ? 0 : model.getChildCount(model) - 1);
                    } else {
                        return;
                    }
                }
            }
        } finally {
            model.unlock();
        }
    }

    @Override
    public void removeMessages(final List<Message> messages) {
        Hl7TreeModel model = getModel();

        for(final Message message : messages) {
            model.remove(message);
        }

        if(model.isCompressedView()) {
            model.fireTreeStructureChanged(model.getRoot());
        }
    }

    @Override
    public List<Message> getSelectedMessages() {
        return getTree().getSelectedMessages();
    }

    @Override
    public List<Message> getMessages() {
        return getModel().getMessages();
    }

    @Override
    public Hl7Object appendHl7Object(final Hl7Object parent) {
        try {
            String v = parent instanceof Message ? "ZZZ" : "";

            Hl7Object child = parent.add(v);

            Hl7TreeModel model = getModel();

            if(model.isCompressedView() && v.isEmpty()) {
                SimpleDialog.info("Empty items are only visible in the non compressed view.");
            } else {
                model.fireTreeNodesInsert(new Hl7Object[] {child});
            }
            return child;
        } catch(IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            SimpleDialog.error(ex, "Unable to append a new HL7 object to the message.");
            return null;
        }
    }

    @Override
    public void removeHL7Object(final Hl7Object o) {
        o.clear();
        if(o.getHl7Parent() instanceof RepetitionField && o.getHl7Parent().getChildCount() == 1) {
            o.getHl7Parent().clear();
        }

        if(Desktop.getInstance().isCompressedView()) {
            TreePath path = Hl7TreeModel.buildTreePath(o);
            getModel().fireTreeStructureChanged(path.getParentPath());
        }
    }

    @Override
    public void setHL7ObjectValue(final Hl7Object o, final String value) {
        o.parse(value);
        getModel().fireTreeStructureChanged(o);
    }

    @Override
    public List<Hl7Object> getSelectedObjects() {
        List<Hl7Object> result = new ArrayList<>();

        for(final TreePath path : getTree().getSelectionPaths()) {
            result.add((Hl7Object)path.getLastPathComponent());
        }

        return result;
    }

    @Override
    public TraceWindowIntf getInputTraceWindow() {
        return inputTrace;
    }

    @Override
    public boolean isCompressedView() {
        return getModel().isCompressedView();
    }

    @Override
    public void setCompressedView(final boolean compressed) {
        getToolBar().getCompactViewButton().setSelected(compressed);
        getModel().setCompressedView(compressed);
    }

    @Override
    public boolean isReceiveWindowVisible() {
        return getTabbedBottomPanel().indexOfComponent(getReceiveWindow()) != -1;
    }

    @Override
    public void setReceiveWindowVisible(boolean value) {
        if(value) {
            setTabVisible(getReceiveWindow());
        } else {
            // TODO Hide/Remove tab
        }
    }

    @Override
    public boolean isSendWindowVisible() {
        return getTabbedBottomPanel().indexOfComponent(getSendWindow()) != -1;
    }

    @Override
    public void setSendWindowVisible(boolean value) {
        if(value) {
            setTabVisible(getSendWindow());
        } else {
            // TODO Hide/Remove tab
        }
    }

    @Override
    public boolean isInputTraceWindowVisible() {
        return getTabbedBottomPanel().indexOfComponent(inputTrace) != -1;
    }

    @Override
    public void setInputTraceWindowVisible(boolean value) {
        setTabVisible((JComponent)getInputTraceWindow());
    }

    @Override
    public void setNodeDescriptionWindowVisible(final boolean visible) {
        getToolBar().getNodeDescriptionButton().setSelected(visible);
        getModel().setNodeDescriptionVisible(visible);
        getTree().updateUI();
    }

    @Override
    public boolean isNodeDetailsWindowVisible() {
        return getDetailsWindow().isVisible();
    }

    @Override
    public void setNodeDetailsWindowVisible(final boolean visible) {
    }

    @Override
    public boolean isNodeDescriptionVisible() {
        return getModel().isNodeDescriptionVisible();
    }

    @Override
    public void setNodeDescriptionVisible(boolean visible) {
        getModel().setNodeDescriptionVisible(visible);
    }

    @Override
    public void refreshHighlightPhrases() {
        getTree().repaint();
    }

    @Override
    public void findNextPhrase(String phrase, Hl7Object startFrom, boolean caseSensitive) {
        if(!phrase.isEmpty() && getTree().getModel().getChildCount(getTree().getModel().getRoot()) != 0) {
            TreeNode startingNode = startFrom == null ? (TreeNode)getTree().getModel().getRoot() : (TreeNode)startFrom;
            TreePath path = TreeNodeSearchEngine.findNextNode(phrase, caseSensitive, startingNode);

            if(path == null) {
                SimpleDialog.info("The end of message tree reached.");
            } else {
                getTree().expandPath(path.getParentPath());

                int row = getTree().getRowForPath(path);

                getTree().scrollRowToVisible(row);
                getTree().setSelectionRow(row);
            }
        }
    }

    @Override
    public void setLockCounter(boolean increase) {
        if(increase) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            getModel().locked();
        } else {
            getModel().unlock();

            if(getModel().getLockCount() == 0) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

    }

    // Interface ComponentListener
    @Override
    public void componentShown(ComponentEvent e) {
        if(e.getSource().equals(getDetailsWindow())) {
            StartupProperties.getInstance().setDetailsWindowVisible(getDetailsWindow().isVisible());
            middlePanel.setDividerLocation(getSize().width - 200);
            middlePanel.setDividerSize(4);
            getToolBar().getDetailsButton().setSelected(true);

            if(getTree().getSelectionCount() == 1) {
                showHl7ObjectDetails((Hl7Object)getTree().getSelectionPath().getLastPathComponent());
            }
        } else if(e.getSource().equals(getTabbedBottomPanel())) {
            mainPanel.setDividerSize(4);
            mainPanel.setDividerLocation(0.75);
//            getToolBar().getDetailsButton().setSelected(true);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        if(e.getSource().equals(getDetailsWindow())) {
            StartupProperties.getInstance().setDetailsWindowVisible(getDetailsWindow().isVisible());
            getToolBar().getDetailsButton().setSelected(false);
            if(middlePanel.isVisible()) {
                middlePanel.setDividerSize(0);
            }
        } else if(e.getSource().equals(getTabbedBottomPanel())) {
            if(mainPanel.isVisible()) {
                mainPanel.setDividerSize(0);
            }
        }
    }
}
