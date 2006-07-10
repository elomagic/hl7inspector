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
import com.l2fprod.common.swing.BaseDialog;
import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.hl7.parser.MessageEncoding;
import de.elomagic.hl7inspector.io.StreamFormat;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author rambow
 */
public class ImportOptionsDialog extends BaseDialog {
    
    /** Creates a new instance of ImportOptionsDialog */
    public ImportOptionsDialog() {
        super(Desktop.getInstance());
        
        init();
    }
    
    public boolean execute(ImportOptionBean defaultOptions) {
        if (defaultOptions == null) {
            defaultOptions = new ImportOptionBean();
        }
        
        ((SpinnerNumberModel)spBuffer.getModel()).setValue(new Integer(defaultOptions.getBufferSize()));
        cbCaseSense.setSelected(defaultOptions.isCaseSensitive());
        cbClearBuffer.setSelected(defaultOptions.isClearBuffer());
        rbModeAuto.setSelected(defaultOptions.getImportMode() == StreamFormat.AUTO_DETECT);
        rbModeStream.setSelected(defaultOptions.getImportMode() == StreamFormat.FRAMED);
        rbModeParse.setSelected(defaultOptions.getImportMode() == StreamFormat.TEXT_LINE);
        cbNegateReg.setSelected(defaultOptions.isNegReg());
        cbReadBottom.setSelected(defaultOptions.isReadBottom());
        cbbStartChar.setSelectedIndex((defaultOptions.getStartChar()==null)?16:(int)defaultOptions.getStartChar().charValue());
        cbbStopChar1.setSelectedIndex((defaultOptions.getStopChar1()==null)?16:(int)defaultOptions.getStopChar1().charValue());
        cbbStopChar2.setSelectedIndex((defaultOptions.getStopChar2()==null)?16:(int)defaultOptions.getStopChar2().charValue());
        cbValidate.setSelected(defaultOptions.isValidate());
        cbMessageEnc.setSelectedIndex((defaultOptions.getMessageEncoding()==MessageEncoding.HL7_FORMAT)?0:1);
        cbCharEnc.setSelectedItem(defaultOptions.getEncoding());        
        cbRegExpr.setSelected(defaultOptions.isUseRegExpr());
        
        updateParseModeButtons(defaultOptions.getImportMode() != StreamFormat.TEXT_LINE);
        
        lblSource.setText(defaultOptions.getSource());
        lblSource.setToolTipText(defaultOptions.getSource());
        
        lblFileSize.setText((defaultOptions.getFileSize()==-1)?"?":""+defaultOptions.getFileSize());
        
        cbbPhrase.setSelectedItem("");
        cbbPhrase.setFocusable(true);
        cbbPhrase.requestFocusInWindow();        
        
        return ask();
    }
    
    public void ok() {
        if (cbbPhrase.getSelectedItem().toString().length() != 0) {
            StartupProperties.getInstance().putPhrase(cbbPhrase.getSelectedItem().toString());
        }
        
        super.ok();
    }
    
    public ImportOptionBean getImportOptions() {
        ImportOptionBean result = new ImportOptionBean();
        
        result.setBufferSize(Integer.parseInt(spBuffer.getValue().toString()));
        result.setCaseSensitive(cbCaseSense.isSelected());
        result.setClearBuffer(cbClearBuffer.isSelected());
        try {
            result.setFileSize(Integer.parseInt(lblFileSize.getText()));
        } catch (Exception e) {
            result.setFileSize(-1);
        }
        result.setImportMode(rbModeParse.isSelected()?StreamFormat.TEXT_LINE:(rbModeStream.isSelected()?StreamFormat.FRAMED:StreamFormat.AUTO_DETECT));
        result.setNegReg(cbNegateReg.isSelected());
        result.setPhrase((cbbPhrase.getSelectedItem() == null)?"":cbbPhrase.getSelectedItem().toString());
        result.setReadBottom(cbReadBottom.isSelected());
        result.setSource(lblSource.getText());
        result.setStartChar((cbbStartChar.getSelectedIndex() > 31)?null:new Character((char)cbbStartChar.getSelectedIndex()));
        result.setStopChar1((cbbStopChar1.getSelectedIndex() > 31)?null:new Character((char)cbbStopChar1.getSelectedIndex()));
        result.setStopChar2((cbbStopChar2.getSelectedIndex() > 31)?null:new Character((char)cbbStopChar2.getSelectedIndex()));
        result.setValidate(cbValidate.isSelected());
        result.setEncoding(cbCharEnc.getSelectedItem().toString());
        result.setMessageEncoding((cbMessageEnc.getSelectedIndex()==0)?MessageEncoding.HL7_FORMAT:MessageEncoding.XML_FORMAT);
        result.setUseRegExpr(cbRegExpr.isSelected());
        
        return result;
    }
    
    private void init() {        
        getBanner().setVisible(false);
        
        setTitle("Import Options");
        //setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        
        lblSource.setFont(lblSource.getFont().deriveFont(Font.BOLD));
        
        btGrpMode.add(rbModeAuto);
        btGrpMode.add(rbModeStream);
        btGrpMode.add(rbModeParse);
        
        rbModeAuto.setSelected(true);
        
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
        cbbStartChar.setModel(new DefaultComboBoxModel(model));
        cbbStopChar1.setModel(new DefaultComboBoxModel(model));
        cbbStopChar2.setModel(new DefaultComboBoxModel(model));
        
        cbMessageEnc = new JComboBox(new String[] { "HL7", "XML" } );
        
        String[] modelEnc = {
            "ISO-8859-1",
            "US-ASCII",
            "UTF-8",
            "UTF-16BE",
            "UTF-16LE",
            "UTF-16"
        };
        cbCharEnc.setModel(new DefaultComboBoxModel(modelEnc));
        
        cbbPhrase = new JComboBox(StartupProperties.getInstance().getPhrases());
        cbbPhrase.setSelectedIndex(-1);
        cbbPhrase.setEditable(true);
        
        // TODO Encoding and validate support required
        FormLayout layout = new FormLayout(
                "0dlu, p, 4dlu, 50dlu, 4dlu, p, 2dlu, 50dlu, 4dlu, p, 2dlu, 50dlu, p:grow",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        // 1st row
        builder.add(new GradientLabel("Source"),        cc.xyw(1,   1,  13));
        
        // 2nd row
        builder.addLabel("Name:",             cc.xy(2,   3));      // Ok
        builder.add(lblSource,                cc.xyw(4,   3,  10));
        
        // 3rd row
        builder.addLabel("Size:",             cc.xyw(2,   5,  12));        // Ok
        builder.add(lblFileSize,              cc.xyw(4,   5,  10));
        
        // 4th row
        builder.add(new GradientLabel("Options"),       cc.xyw(1,   7,  13));
        
        // 5th row
        builder.add(cbClearBuffer,            cc.xyw(2,   9,  3));
        builder.add(cbReadBottom,             cc.xyw(8,   9,  3));
        
        // 6th row
//    builder.add(cbReadBottom,             cc.xyw(2,   11, 6));
        
        // 7th row
        builder.addLabel("Buffer size:",      cc.xy(2,   13));     // Ok
        builder.add(spBuffer,                 cc.xyw(4,   13, 2));
        
        builder.addLabel("Char Encoding:",    cc.xy(2,   15));     // Ok
        builder.add(cbCharEnc,               cc.xyw(4,   15, 2));
        builder.add(cbValidate,               cc.xyw(8,   15,  3));       

        builder.addLabel("Message Encoding:",    cc.xy(2,   17));     // Ok
        builder.add(cbMessageEnc,               cc.xyw(4,   17, 2));
        
        
        // 8th row
        builder.add(new GradientLabel("Parser Mode:"),         cc.xyw(1, 19, 13));
        
        // 9th row
        builder.add(rbModeAuto,               cc.xyw(2, 21, 12));
        
        // 11th row
        builder.add(rbModeParse,              cc.xyw(2, 23, 12));
        
        // 10th row
        builder.add(rbModeStream,             cc.xyw(2, 25, 12));
        
        // 12th row
        builder.addLabel("Start char:",       cc.xy(2, 27));       // Ok
        builder.add(cbbStartChar,             cc.xy(4, 27));
        
        builder.addLabel("1. Stop char :",    cc.xy(6, 27));
        builder.add(cbbStopChar1,             cc.xy(8, 27));
        
        builder.addLabel("2. Stop char:",     cc.xy(10, 27));
        builder.add(cbbStopChar2,             cc.xy(12, 27));
                
        // 13th row
        builder.add(new GradientLabel("Filter"),        cc.xyw(1, 29, 13));
        
        // 12th row
        builder.add(cbCaseSense,              cc.xyw(2, 31, 3));
        builder.add(cbNegateReg,              cc.xyw(6, 31, 3));
        builder.add(cbRegExpr,                cc.xyw(10, 31, 3));
        
        // 13th row
        builder.addLabel("Phrase:",           cc.xy(2, 33));       // Ok
        builder.add(cbbPhrase,                cc.xyw(4, 33, 10));
        
        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        
        pack();
        
        setSize(530, getPreferredSize().height);
        
        setBounds(ToolKit.centerFrame(this, this.getOwner()));
    }
    
    class ParseModeAction extends AbstractAction {
        public ParseModeAction(String name, String cmd) {
            super(name);
            
            putValue(AbstractAction.ACTION_COMMAND_KEY, cmd);
        }
        
        public void actionPerformed(java.awt.event.ActionEvent e) {
            boolean b = !e.getActionCommand().equals("PARSE");
            updateParseModeButtons(b);
            
        }
    }
    
    private void updateParseModeButtons(boolean disableFrame) {
        cbbStartChar.setEnabled(disableFrame);
        cbbStopChar1.setEnabled(disableFrame);
        cbbStopChar2.setEnabled(disableFrame);
    }
    
    // FEATURE Validate during import support
    // FEATURE Character encoding support
    
    private JLabel          lblSource       = new JLabel("Source can be a file or an other streamable source");
    private JLabel          lblFileSize     = new JLabel("?");
    private JCheckBox       cbClearBuffer   = new JCheckBox("Clear buffer");
    private JCheckBox       cbReadBottom    = new JCheckBox("Read from bottom");
    private JSpinner        spBuffer        = new JSpinner(new SpinnerNumberModel(1000, 1, 2000, 50));
    private JCheckBox       cbValidate      = new JCheckBox("Validate message");
    private JComboBox       cbMessageEnc;
    private JComboBox       cbCharEnc       = new JComboBox();
    private JRadioButton    rbModeAuto      = new JRadioButton(new ParseModeAction("Auto detect", "AUTO"));
    private JRadioButton    rbModeParse     = new JRadioButton(new ParseModeAction("Line stream", "PARSE"));
    private JRadioButton    rbModeStream    = new JRadioButton(new ParseModeAction("Message stream (Framed)", "FRAMED"));
    private ButtonGroup     btGrpMode       = new ButtonGroup();
    private JComboBox       cbbStartChar    = new JComboBox();
    private JComboBox       cbbStopChar1    = new JComboBox();
    private JComboBox       cbbStopChar2    = new JComboBox();
    private JCheckBox       cbRegExpr       = new JCheckBox("Use regular expression");
    private JCheckBox       cbNegateReg     = new JCheckBox("Negate result");
    private JCheckBox       cbCaseSense     = new JCheckBox("Case sensitive");
    private JComboBox       cbbPhrase;
}
