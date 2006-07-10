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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.io.MessageImportEvent;
import de.elomagic.hl7inspector.io.MessageImportListener;
import de.elomagic.hl7inspector.io.MessageImportThread;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.validate.Validator;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ReaderProgessDialog extends JDialog implements MessageImportListener, ActionListener {
    
    /** Creates a new instance of ReaderProgessDialog */
    public ReaderProgessDialog() {
        super(Desktop.getInstance());
        
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
        builder.add(new GradientLabel("Source"),        cc.xyw(1,   1,  10));
        
        // 2nd row
        builder.addLabel("Name:",             cc.xy(2,   3));      // Ok
        builder.add(lblSource,                cc.xyw(4,   3,  7));
        
        // 3rd row
        builder.addLabel("Size:",             cc.xy(2,   5));        // Ok
        builder.add(lblSize,                  cc.xyw(4,   5,  7));        // Ok
        
        // 4th row
        builder.add(new GradientLabel("Progress"),      cc.xyw(1,   7,  10));
        
        // 5th row
        builder.addLabel("Message:",          cc.xy(2,   9));
        builder.add(lblMessages,              cc.xyw(4,   9,  2));
        builder.addLabel("Size:",             cc.xy(7,   9));
        builder.add(lblBytes,                 cc.xyw(9,   9,  2));
        
        // 6th row
        builder.add(bar,                      cc.xyw(1,   11, 10));
        
        // 7th row
        builder.add(buttonPanel,              cc.xyw(1,   13, 10));
        
        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        
        // Button pan
        
        pack();
        
        setSize(300, getPreferredSize()!=null?getPreferredSize().height:230);
        
        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }
    
    public void read(InputStream fin, ImportOptionBean readOptions) throws IOException {
        setModal(true);
        options = readOptions;
        
        Desktop.getInstance().getScrollPane().getVerticalScrollBar().setValue(Desktop.getInstance().getScrollPane().getVerticalScrollBar().getMaximum());
                         
        model = (readOptions.isClearBuffer())?new Hl7TreeModel():(Hl7TreeModel)Desktop.getInstance().getModel();
        model.locked();
        thread = new MessageImportThread(fin, readOptions);
        thread.addListener(this);
        thread.start();
        
        setVisible(true);
//    } finally {
//      setVisible(false);
//    }
    }
    
    private JLabel  lblSource   = new JLabel();
    private JLabel  lblSize     = new JLabel("?");
    private JLabel  lblMessages = new JLabel("0");
    private JLabel  lblBytes    = new JLabel("0");
    private JPanel  buttonPanel = new JPanel(new FlowLayout());
    
    private Hl7TreeModel model  = null;
    private ImportOptionBean options = null;
    private MessageImportThread   thread  = null;
    
    private boolean userAbort       = false;
    
    private JProgressBar bar    = new JProgressBar(JProgressBar.HORIZONTAL);
    private JButton btAbort     = new JButton("Abort");
    private DoRun doRun         = new DoRun();
    
    public void messageRead(MessageImportEvent event) {
        Desktop.getInstance().getInputTraceWindow().addLine("Catch message.");
        boolean ignore = false;
        bytesRead = event.getBytesRead();
        // Now filtering
        if (options.getPhrase().length() != 0) {
            String m = (options.isCaseSensitive()?event.getMessage().toString():event.getMessage().toString().toUpperCase());
            String phrase = (options.isCaseSensitive()?options.getPhrase():options.getPhrase().toUpperCase());
            
            if (!options.isUseRegExpr()) {
                boolean found = (m.indexOf(phrase) != -1);
                ignore =((( !found && !options.isNegReg()) ||
                        found && options.isNegReg()));
            } else {
                boolean found = (m.matches(phrase));
                
                ignore =((( !found && !options.isNegReg()) ||
                        found && options.isNegReg()));
            }
        }
        
        Message msg = event.getMessage();
        
        if (!ignore) {
            try {
                File file = new File(options.getSource());
                if (file.exists()) {
                    msg.setFile(file);
                }
            } catch (Exception e) {
                Logger.getLogger(getClass()).error(e.getMessage(), e);
            }
            
            model.addMessage(msg);

            if (options.isValidate()) {
                try {
                    Validator val = new Validator(Profile.getDefault());
                    val.validate(msg);
                } catch (Exception ee) {
                    Logger.getLogger(getClass()).error(ee.getMessage(), ee);
                }
            }
            
            // Check buffer overflow
            while (model.getChildCount(model.getRoot()) > options.getBufferSize()) {
                if (options.isReadBottom())
                    model.removeMessage(0);
                else {
                    event.getSource().terminate = true;
                    model.removeMessage(model.getChildCount(model.getRoot())-1);
                }
            }
        } else {
            Desktop.getInstance().getInputTraceWindow().addLine("Ignore message. (Filter is active)");
        }
        
        SwingUtilities.invokeLater(doRun);
    }
    
    public void charRead(char c) {
        Desktop.getInstance().getInputTraceWindow().addChar(c);
    }
    
    private long bytesRead;
    
    public void importDone(MessageImportEvent event) {
        Desktop.getInstance().getInputTraceWindow().addLine("Import done.");
        Desktop.getInstance().setModel(model);
        model.unlock();
        setVisible(false);
        if ((event.getSource().terminate) && (userAbort)) {
            SimpleDialog.info("Import abort by user.");
        }
    }
    
    class DoRun implements Runnable {
        public void run() {
            lblSource.setText(options.getSource());
            lblSize.setText(Long.toString(options.getFileSize()));
            lblBytes.setText(Long.toString(bytesRead));
            
            if (model != null) {
                lblMessages.setText(Integer.toString(model.getChildCount(model.getRoot())));
                bar.setValue(model.getChildCount(model.getRoot()));
            }
        }
    }
    
    public void actionPerformed(ActionEvent ee) {
        if (thread != null) {
            userAbort = true;
            thread.terminate = true;
        }
    }
}
