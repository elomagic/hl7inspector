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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.io.MessageImportEvent;
import de.elomagic.hl7inspector.io.MessageImportListener;
import de.elomagic.hl7inspector.io.MessageImportThread;
import de.elomagic.hl7inspector.profile.ProfileIO;
import de.elomagic.hl7inspector.validate.Validator;

/**
 *
 * @author Carsten Rambow
 */
public class ReaderProgessDialog extends JDialog implements MessageImportListener, ActionListener {

    private static final long serialVersionUID = 4229125522794967128L;
    private long bytesRead;
    private JLabel lblSource = new JLabel();
    private JLabel lblSize = new JLabel("?");
    private JLabel lblMessages = new JLabel("0");
    private JLabel lblBytes = new JLabel("0");
    private JPanel buttonPanel = new JPanel(new FlowLayout());
    private ImportOptionBean options = null;
    private MessageImportThread thread = null;
    private int messagesAppend;
    private boolean userAbort = false;
    private JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL);
    private JButton btAbort = new JButton("Abort");
    private DoRun doRun = new DoRun();
    private DesktopIntf d = Desktop.getInstance();

    /**
     * Creates a new instance of ReaderProgessDialog.
     */
    public ReaderProgessDialog() {
        super(Desktop.getInstance().getMainFrame());

        init();
    }

    private void init() {
        setTitle("File Reading Progress");
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        lblSource.getFont().deriveFont(Font.BOLD);
        bar.setIndeterminate(true);

        btAbort.addActionListener(this);

        buttonPanel.add(btAbort);

        FormLayout layout = new FormLayout(
                "8dlu, p, 4dlu, p:grow, p, 2dlu, p, p, p, p:grow",
                //            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 7dlu, p");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.add(new GradientLabel("Source"), cc.xyw(1, 1, 10));

        // 2nd row
        builder.addLabel("Name:", cc.xy(2, 3));      // Ok
        builder.add(lblSource, cc.xyw(4, 3, 7));

        // 3rd row
        builder.addLabel("Size:", cc.xy(2, 5));        // Ok
        builder.add(lblSize, cc.xyw(4, 5, 7));        // Ok

        // 4th row
        builder.add(new GradientLabel("Progress"), cc.xyw(1, 7, 10));

        // 5th row
        builder.addLabel("Message:", cc.xy(2, 9));
        builder.add(lblMessages, cc.xyw(4, 9, 2));
        builder.addLabel("Size:", cc.xy(7, 9));
        builder.add(lblBytes, cc.xyw(9, 9, 2));

        // 6th row
        builder.add(bar, cc.xyw(1, 11, 10));

        // 7th row
        builder.add(buttonPanel, cc.xyw(1, 13, 10));

        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);

        // Button pan
        pack();

        setSize(300, getPreferredSize() == null ? 230 : getPreferredSize().height);

        setLocationRelativeTo(getOwner());
    }

    public void read(final InputStream fin, final ImportOptionBean readOptions) throws IOException {
        setModal(true);
        options = readOptions;

        // TODO Do we need this really ???
        //Desktop.getInstance().getScrollPane().getVerticalScrollBar().setValue(Desktop.getInstance().getScrollPane().getVerticalScrollBar().getMaximum());
        if(readOptions.isClearBuffer()) {
            Desktop.getInstance().clearMessages();
        }

        messagesAppend = 0;
        d.setLockCounter(true);
        thread = new MessageImportThread(fin, readOptions);
        thread.addListener(this);
        thread.start();

        setVisible(true);
    }

    @Override
    public void messageRead(final MessageImportEvent event) {
        Desktop.getInstance().getInputTraceWindow().addLine("Catch message.");
        boolean ignore = false;
        bytesRead = event.getBytesRead();
        // Now filtering
        if(!options.getPhrase().isEmpty()) {
            String m = options.isCaseSensitive() ? event.getMessage().toString() : event.getMessage().toString().toUpperCase();
            String phrase = options.isCaseSensitive() ? options.getPhrase() : options.getPhrase().toUpperCase();

            if(!options.isUseRegExpr()) {
                boolean found = (m.contains(phrase));
                ignore = ((!found && !options.isNegReg())
                          || found && options.isNegReg());
            } else {
                boolean found = m.matches(phrase);

                ignore = ((!found && !options.isNegReg())
                          || found && options.isNegReg());
            }
        }

        if(ignore) {
            d.getInputTraceWindow().addLine("Ignore message. (Filter is active)");
        } else {
            Message msg = event.getMessage();
            try {
                File file = new File(options.getSource());
                if(file.exists()) {
                    msg.setFile(file);
                }
            } catch(Exception e) {
                Logger.getLogger(getClass()).error(e.getMessage(), e);
            }

            d.addMessages(Collections.singletonList(msg),
                    options.getBufferSize(),
                    options.isReadBottom());

            if(d.getSelectedMessages().size() < options.getBufferSize()) {
                messagesAppend++;
            } else if(!options.isReadBottom()) {
                thread.terminate = true;
            }

            if(options.isValidate()) {
                try {
                    Validator val = new Validator(ProfileIO.getDefault());
                    val.validate(msg);
                } catch(Exception ee) {
                    Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                }
            }
        }

        SwingUtilities.invokeLater(doRun);
    }

    @Override
    public void charRead(final char c) {
        d.getInputTraceWindow().addChar(c);
    }

    @Override
    public void importDone(final MessageImportEvent event) {
        d.getInputTraceWindow().addLine("Import done.");
        //Desktop.getInstance().setModel(model);
        d.setLockCounter(false);
        setVisible(false);
        if(event.getSource().terminate && userAbort) {
            Notification.info("Import abort by user.");
        }
    }

    class DoRun implements Runnable {

        @Override
        public void run() {
            lblSource.setText(options.getSource());
            lblSize.setText(Long.toString(options.getFileSize()));
            lblBytes.setText(Long.toString(bytesRead));
            lblMessages.setText(Integer.toString(messagesAppend));
            bar.setValue(messagesAppend);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if(thread != null) {
            userAbort = true;
            thread.terminate = true;
        }
    }
}
