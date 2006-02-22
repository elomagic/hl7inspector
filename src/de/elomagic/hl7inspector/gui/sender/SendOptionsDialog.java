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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.GradientLabel;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.gui.ToolKit;
import de.elomagic.hl7inspector.io.Frame;
import de.elomagic.hl7inspector.io.SendOptionsBean;
import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author rambow
 */
public class SendOptionsDialog extends BaseDialog {
    
    /** Creates a new instance of SendOptionsDialog */
    public SendOptionsDialog() {
        super(Desktop.getInstance());
        
        init();        
    }
    
    private void init() {
        getBanner().setVisible(false);
                
        setTitle("Send Options");
        //setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        
        cbStartChar     = new JComboBox();
        cbStopChar1     = new JComboBox();
        cbStopChar2     = new JComboBox();
        edDestHost      = new JTextField();    
        cbDestPort      = new JComboBox(new String[] { "2100", "2200", "2300", "5555", "5556" } );
        cbDestPort.setEditable(true);
        cbEncoding      = new JComboBox(new String[] { "ISO-8859-1", "US-ASCII", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16" });
        cbReuse         = new JCheckBox();
        cbReuse.setToolTipText("Reuse socket for next the message.");
        
//        btGrpMode.add(rbModeAuto);
//        btGrpMode.add(rbModeStream);
//        btGrpMode.add(rbModeParse);
        
        String[] model = {
            "0x00 - NUL",
            "0x01 - SOH",
            "0x02 - STX",
            "0x03 - ETX",
            "0x04 - EOT",
            "0x05 - ENQ",
            "0x06 - ACK",
            "0x07 - BEL",
            "0x08 - BS",
            "0x09 - HT",
            "0x0a - LF",
            "0x0b - VT",
            "0x0c - FF",
            "0x0d - CR",
            "0x0e - SO",
            "0x0f - SI",
            "0x10 - DLE",
            "0x11 - DC1",
            "0x12 - DC2",
            "0x13 - DC3",
            "0x14 - DC4",
            "0x15 - NAK",
            "0x16 - SYN",
            "0x17 - ETB",
            "0x18 - CAN",
            "0x19 - EM",
            "0x1a - SUB",
            "0x1b - ESC",
            "0x1c - FS",
            "0x1d - GS",
            "0x1e - RS",
            "0x1f - US",
            "-"
        };
        cbStartChar.setModel(new DefaultComboBoxModel(model));
        cbStopChar1.setModel(new DefaultComboBoxModel(model));
        cbStopChar2.setModel(new DefaultComboBoxModel(model));
        
        FormLayout layout = new FormLayout(
        "0dlu, p, 4dlu, 50dlu, 4dlu, p, 2dlu, 50dlu, 4dlu, p, 2dlu, 50dlu, p:grow",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
        "p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
        builder.add(new GradientLabel("Destination"),        cc.xyw(1,   1,  13));
        
        // 2nd row
        builder.addLabel("Host:",             cc.xy(2,   3));      // Ok
        builder.add(edDestHost,                cc.xyw(4,   3,  10));
        
        // 3rd row
        builder.addLabel("Port:",             cc.xy(2,   5));        // Ok
        builder.add(cbDestPort,              cc.xy(4,   5));
        
        // 4th row
        builder.add(new GradientLabel("Options"),       cc.xyw(1,   7,  13));
        
        // 5th row
        builder.addLabel("Encoding:",       cc.xyw(2,   9,  3));
        builder.add(cbEncoding,           cc.xyw(4,   9,  2));
        
        // 6th row
        builder.addLabel("Reuse:",          cc.xyw(2,   11,  3));
        builder.add(cbReuse,                cc.xyw(4,   11,  2));
        
        // 8th row
        builder.add(new GradientLabel("Message Frame:"),         cc.xyw(1, 15, 13));
                
        // 12th row
        builder.addLabel("Start char:",       cc.xy(2, 17));       // Ok
        builder.add(cbStartChar,             cc.xy(4, 17));
        
        builder.addLabel("1. Stop char :",    cc.xy(6, 17));
        builder.add(cbStopChar1,             cc.xy(8, 17));
        
        builder.addLabel("2. Stop char:",     cc.xy(10, 17));
        builder.add(cbStopChar2,             cc.xy(12, 17));        
        
        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        
        pack();
        
        setSize(530, getPreferredSize().height);
        
        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }
    
    public void setOptions(SendOptionsBean bean) {        
        cbStartChar.setSelectedIndex(bean.getFrame().getStartFrame());
        cbStopChar1.setSelectedIndex(bean.getFrame().getStopFrame()[0]);
        cbStopChar2.setSelectedIndex((bean.getFrame().getStopFrameLength() < 2)?cbStopChar2.getItemCount()-1:bean.getFrame().getStopFrame()[1]);
        edDestHost.setText(bean.getHost());
        cbDestPort.setSelectedItem(Integer.toString(bean.getPort()));
        cbEncoding.setSelectedItem(bean.getEncoding());
        cbReuse.setSelected(bean.isReuseSocket());
    }
    
    public void ok() {
        try {
            try {
                Integer.parseInt(cbDestPort.getSelectedItem().toString());
            } catch (Exception ee) {
                throw new Exception("Destination port must an integer value!");
            }
            
            if (edDestHost.getText().length() == 0) {
                throw new Exception("Destination host missing!");
            }
            
            super.ok();            
        } catch (Exception e) {
            SimpleDialog.error(e.getMessage());            
        }        
    }    
    
    public SendOptionsBean getOptions() {
        Frame frame = new Frame();
        frame.setStartChar((char)cbStartChar.getSelectedIndex());
        int stops = (cbStopChar2.getSelectedIndex() < cbStopChar2.getItemCount()-1)?2:1;
        if (stops == 1) {
            frame.setStopChars( new char[] { (char)cbStopChar1.getSelectedIndex() } );
        } else {
            frame.setStopChars( new char[] { (char)cbStopChar1.getSelectedIndex(), (char)cbStopChar2.getSelectedIndex() } );            
        }

        SendOptionsBean bean = new SendOptionsBean();
        bean.setFrame(frame);
        bean.setHost(edDestHost.getText());
        bean.setPort(Integer.parseInt(cbDestPort.getSelectedItem().toString()));
        bean.setEncoding(cbEncoding.getSelectedItem().toString());
        bean.setReuseSocket(cbReuse.isSelected());
        
        return bean;
    }
    
    private JComboBox       cbStartChar;
    private JComboBox       cbStopChar1;
    private JComboBox       cbStopChar2;
    private JTextField      edDestHost;
    private JComboBox       cbDestPort;
    private JComboBox       cbEncoding;
    private JCheckBox       cbReuse;
}