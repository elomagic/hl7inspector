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
package de.elomagic.hl7inspector.gui.sender;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.monitor.CharacterMonitor;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.io.SendThread;

/**
 *
 * @author rambow
 */
public class SendPanel extends CharacterMonitor implements ActionListener {
    private static final long serialVersionUID = 2164439922833152117L;

    /**
     * Creates a new instance of SendPanel.
     */
    public SendPanel() {
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
        getToolBar().add(btOptions);
    }
    private SendThread thread;
    private AbstractButton btStart = createButton(JToggleButton.class, "start_service.png", "Send selected messages", "START");
    private AbstractButton btStop = createButton(JToggleButton.class, "stop_service.png", "Cancel sending message", "STOP");
    private AbstractButton btOptions = createButton(JButton.class, "preferences-desktop.png", "Setup sender options", "OPTIONS");
    private AbstractButton btSeqAuth = createButton(JToggleButton.class, "kgpg_sign.png", "Server authentication", "AUTH");
    private AbstractButton btSeqCrypt = createButton(JToggleButton.class, "encrypt.png", "Encrypt communication", "CRYPT");

    private void initThread() {
        SendThread t = new SendThread();

//        t.setMessages(Desktop.getInstance().getTree().getSelectedMessages());

        if(thread != null) {
            thread.terminateRequest();
            thread.removeListener(this);

            t.setOptions(thread.getOptions());
        }

        t.addListener(this);

        thread = t;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "START":
                if(Desktop.getInstance().getTree().getSelectedMessages().isEmpty()) {
                    SimpleDialog.error("No message(s) selected. Please select the message(s) you want to send.");
                } else {
                    thread.setMessages(Desktop.getInstance().getTree().getSelectedMessages());
                    thread.start();
                }
                break;
            case "STOP":
                if(thread != null) {
                    thread.terminateRequest();
                }
                break;
            case "OPTIONS":
                //            setAlwaysOnTop(false);
                SendOptionsDialog dialog = new SendOptionsDialog();
                dialog.setOptions(thread.getOptions());
                if(dialog.ask()) {
                    thread.setOptions(dialog.getOptions());
                }
                break;
            case "AUTH":
                thread.getOptions().setAuthentication(btSeqAuth.isSelected());
                break;
            case "CRYPT":
                thread.getOptions().setEncryption(btSeqCrypt.isSelected());
                break;
            default:
                Logger.getLogger(getClass()).error("Unknown ActionCommand '" + e.getActionCommand() + "'.");
                break;
        }
    }

    private AbstractButton createButton(Class c, String imageName, String text, String cmd) {
        AbstractButton result = null;
        try {
            result = (AbstractButton)c.newInstance();

            result.setIcon(ResourceLoader.loadImageIcon(imageName));
            result.setToolTipText(text);
            result.setActionCommand(cmd);
            result.addActionListener(this);
        } catch(InstantiationException | IllegalAccessException e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }

        return result;
    }

    // Interface IOThreadListener
    @Override
    public void threadStarted(Thread source) {
        btStart.setEnabled(false);
        btStop.setEnabled(true);
        btSeqCrypt.setEnabled(false);
        btSeqAuth.setEnabled(false);
        btOptions.setEnabled(false);
    }

    @Override
    public void threadStopped(Thread source) {
        btStart.setEnabled(true);
        btStop.setEnabled(false);
        btStop.setSelected(true);
        btSeqCrypt.setEnabled(true);
        btSeqAuth.setEnabled(true);
        btOptions.setEnabled(true);

        initThread();
    }

    @Override
    public String getTitle() {
        return "Message Sender";
    }

    @Override
    public ImageIcon getIcon() {
        return ResourceLoader.loadImageIcon("send.png");
    }
}
