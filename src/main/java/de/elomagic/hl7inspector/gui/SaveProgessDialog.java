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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.io.MessageWriterListener;
import de.elomagic.hl7inspector.io.MessageWriterThread;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author rambow
 */
public class SaveProgessDialog extends JDialog implements MessageWriterListener, ActionListener {
    private JLabel lblDest = new JLabel();
    private JLabel lblMessageIndex = new JLabel("0");
    private JLabel lblCount = new JLabel("0");
    private JPanel buttonPanel = new JPanel(new FlowLayout());
    private JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL);
    private JButton btAbort = new JButton("Abort");
    private MessageWriterBean bean;
    private List<Message> messages;
    private MessageWriterThread thread = null;
    private DoRun doRun = new DoRun();
    private int messageIndex = 0;
    private File messageFile = null;

    /**
     * Creates a new instance of SaveProgessDialog.
     */
    public SaveProgessDialog(List<Message> messageList, MessageWriterBean options) {
        super(Desktop.getInstance());

        messages = messageList;
        bean = options;

        init();
    }

    @Override
    public void setVisible(boolean v) {
        if(v) {
            thread = new MessageWriterThread(messages, bean);
            thread.addListener(this);
            thread.start();
        }

        super.setVisible(v);
    }

    private void init() {
//        getBanner().setVisible(false);
//        getButtonPane().setVisible(false);

        setModal(true);
        setTitle("Save messages progress");
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        lblDest.getFont().deriveFont(Font.BOLD);
        lblDest.setText("Unknown");

        bar.setIndeterminate(false);

        btAbort.addActionListener(this);

        buttonPanel.add(btAbort);

        FormLayout layout = new FormLayout(
                "8dlu, p, 4dlu, p:grow, p, 2dlu, p, p, p, p:grow",
                //            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 7dlu, p");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.add(new GradientLabel("Destination"), cc.xyw(1, 1, 10));

        // 2nd row
        builder.addLabel("File:", cc.xy(2, 3));      // Ok
        builder.add(lblDest, cc.xyw(4, 3, 7));

        // 4th row
        builder.add(new GradientLabel("Progress"), cc.xyw(1, 5, 10));

        // 5th row
        builder.addLabel("Message:", cc.xy(2, 7));
        builder.add(lblMessageIndex, cc.xyw(4, 7, 2));
        builder.addLabel("Total:", cc.xy(7, 7));
        builder.add(lblCount, cc.xyw(9, 7, 2));

        // 6th row
        builder.add(bar, cc.xyw(1, 9, 10));

        // 7th row
        builder.add(buttonPanel, cc.xyw(1, 11, 10));

        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);

        // Button pan

        pack();

        setSize(300, getPreferredSize() != null ? getPreferredSize().height : 230);

        setLocationRelativeTo(getOwner());
    }

    @Override
    public void actionPerformed(ActionEvent ee) {
        if(thread != null) {
            thread.terminate = true;
        }
    }

    // Interface MessageWriterListener
    @Override
    public void messageSaved(MessageWriterThread source, File file, int count) {
        messageIndex++;
        messageFile = file;

        SwingUtilities.invokeLater(doRun);
    }

    @Override
    public void writerDone(MessageWriterThread source, int count) {
        setVisible(false);
        if(source.terminate) {
            SimpleDialog.info("Saving message abort by user.");
        } else {
            SimpleDialog.info("Saving message successfull done.");
        }
    }

    class DoRun implements Runnable {
        @Override
        public void run() {
            if(messageFile != null) {
                lblDest.setText(messageFile.getAbsolutePath());
            }

            lblMessageIndex.setText(Integer.toString(messageIndex));
            lblCount.setText(Integer.toString(messages.size()));

            bar.setMaximum(messages.size());
            bar.setValue(messageIndex);
        }
    }
}
