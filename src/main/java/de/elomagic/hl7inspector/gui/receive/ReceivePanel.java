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
package de.elomagic.hl7inspector.gui.receive;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.ImportOptionsDialog;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.io.ReceiveThread;
import de.elomagic.hl7inspector.io.SendOptionsBean;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ReceivePanel extends CharacterMonitor implements ActionListener {
    private static final long serialVersionUID = 6221303181530455773L;

    private enum Command {
        Start, Stop, Setup, Options, Auth, Crypt
    };
    private ReceiveThread thread = null;
    private AbstractButton btStart = createButton(JToggleButton.class, "start_service.png", "Start receiving message service.", Command.Start);
    private AbstractButton btStop = createButton(JToggleButton.class, "stop_service.png", "Stop receiving message service.", Command.Stop);
    private AbstractButton btPort = createButton(JButton.class, "server.png", "Setup network", Command.Setup);
    private AbstractButton btOptions = createButton(JButton.class, "preferences-desktop.png", "Setup import options", Command.Options);
    private AbstractButton btSeqAuth = createButton(JToggleButton.class, "kgpg_sign.png", "Client authentication", Command.Auth);
    private AbstractButton btSeqCrypt = createButton(JToggleButton.class, "encrypt.png", "Encrypt communication", Command.Crypt);

    /**
     * Creates a new instance of ReceivePanel
     */
    public ReceivePanel() {
        super();

        initThread();

        btStop.setSelected(true);
        ButtonGroup btGroup = new ButtonGroup();
        btGroup.add(btStart);
        btGroup.add(btStop);

        getToolBar().addSeparator();
        getToolBar().add(btStart);
        getToolBar().add(btStop);

        if(StartupProperties.getInstance().isDebugFileOutput()) {
            getToolBar().addSeparator();
            getToolBar().add(btSeqAuth);
            getToolBar().add(btSeqCrypt);
        }

        getToolBar().addSeparator();
        getToolBar().add(btPort);
        getToolBar().add(btOptions);
    }

    private void initThread() {
        ReceiveThread t = new ReceiveThread();

        if(thread != null) {
            thread.terminateRequest();
            thread.removeListener(this);

            t.setFrame(thread.getFrame());
            t.setReUseSocket(thread.isReuseSocket());
            t.setPort(thread.getPort());
            t.setOptions(thread.getOptions());
        }

        t.addListener(this);
        thread = t;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(Command.valueOf(e.getActionCommand())) {
            case Start:
                thread.start();
                break;
            case Stop:
                if(thread != null) {
                    thread.terminateRequest();
                }
                break;
            case Setup:
                //            setAlwaysOnTop(false);
                ReceiveNetworkSetupDialog dialog = new ReceiveNetworkSetupDialog();
                SendOptionsBean bean = new SendOptionsBean();
                bean.setPort(thread.getPort());
                bean.setReuseSocket(thread.isReuseSocket());
                if(dialog.ask()) {
                    thread.setPort(dialog.getOptions().getPort());
                    thread.setReUseSocket(dialog.getOptions().isReuseSocket());
                }
                break;
            case Options:
                ImportOptionsDialog dlg = new ImportOptionsDialog();
                if(dlg.execute(thread.getOptions())) {
                    thread.setOptions(dlg.getImportOptions());
                }
                break;
            case Auth:
                thread.setAuthentication(btSeqAuth.isSelected());
                break;
            case Crypt:
                thread.setEncryption(btSeqCrypt.isSelected());
                break;
            default:
                Logger.getLogger(getClass()).error("Unknown ActionCommand '" + e.getActionCommand() + "'.");
                break;
        }
    }

    private AbstractButton createButton(Class c, String imageName, String text, Command cmd) {
        AbstractButton result;
        try {
            result = (AbstractButton)c.newInstance();

            result.setIcon(ResourceLoader.loadImageIcon(imageName));
            result.setToolTipText(text);
            result.setActionCommand(cmd.name());
            result.addActionListener(this);
        } catch(InstantiationException | IllegalAccessException e) {
            result = null;
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }

        return result;
    }

    // Interface for receive thread
    @Override
    public void threadStarted(Thread source) {
        btStart.setEnabled(false);
        btStop.setEnabled(true);
        btPort.setEnabled(false);
        btSeqCrypt.setEnabled(false);
        btSeqAuth.setEnabled(false);
        btOptions.setEnabled(false);
    }

    @Override
    public void threadStopped(Thread source) {
        btStart.setEnabled(true);
        btStop.setEnabled(false);
        btStop.setSelected(true);
        btPort.setEnabled(true);
        btSeqCrypt.setEnabled(true);
        btSeqAuth.setEnabled(true);
        btOptions.setEnabled(true);

        initThread();
    }

    @Override
    public String getTitle() {
        return "Message Receiver";
    }

    @Override
    public ImageIcon getIcon() {
        return ResourceLoader.loadImageIcon("receive.png");
    }
}
