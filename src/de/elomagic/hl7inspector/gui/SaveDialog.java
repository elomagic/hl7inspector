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
import com.l2fprod.common.swing.JDirectoryChooser;
import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.file.filters.Hl7XmlFileFilter;
import de.elomagic.hl7inspector.gui.framing.FramingSetupDialog;
import de.elomagic.hl7inspector.hl7.parser.MessageEncoding;
import de.elomagic.hl7inspector.io.Frame;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
/**
 *
 * @author rambow
 */
public class SaveDialog extends BaseDialog {
    
    /** Creates a new instance of SaveDialog */
    public SaveDialog() {
        super(Desktop.getInstance(), "Save messages", true);
        
        init();
    }
    
    public MessageWriterBean getOptions() {
        MessageWriterBean options = new MessageWriterBean();
        options.setDataFileExtension((editDataExt.getSelectedItem() != null)?editDataExt.getSelectedItem().toString():"");
        options.setDataFilePrefix(editPrefix.getText());
        options.setDestinationFolder(new File(editDestFolder.getText()));
        options.setFrame(frame);
        options.setGenerateSempahore(btGenSema.isSelected());
        options.setManyFiles(btManyFiles.isSelected());
        options.setOnlySelectedFiles(btSelected.isSelected());
        options.setSemaphoreExtension((editSemaExt.getSelectedItem() != null)?editSemaExt.getSelectedItem().toString():"");
        options.setSingleFileName((editFilename.getText().length()!=0)?new File(editFilename.getText()):null);
        options.setCharEncoding((cbCharEnc.getSelectedItem()!= null)?cbCharEnc.getSelectedItem().toString():"");
        options.setMessageEncoding(("XML".equals(cbCharEnc.getSelectedItem()))?MessageEncoding.XML_FORMAT:MessageEncoding.HL7_FORMAT);
        
        return options;
    }
    
    public void ok() {
        MessageWriterBean options = getOptions();
        
        try {        
            if (options.isManyFiles()) {
                if (options.getDestinationFolder() == null) {
                    throw new Exception("Destination folder not set. Please check");
                }
                
                if (!options.getDestinationFolder().exists()) {
                    throw new Exception("Destination folder does not exists. Please check");
                }
                
                if (options.getDataFileExtension().length() == 0) {
                    throw new Exception("Invalid extension for data file. Please check");
                }
                
                if (options.isGenerateSempahore() && (options.getSemaphoreExtension().length() == 0)) {
                    throw new Exception("Invalid extension for semaphore file. Please check");
                }
                
                
                // Validation ok
                StartupProperties.getInstance().setLastSaveFolder(options.getDestinationFolder());
            } else {
                if (options.getSingleFileName() == null) {
                    throw new Exception("Invalid file name. Please check");
                }
                
                if (options.getSingleFileName().isDirectory()) {
                    throw new Exception("Invalid file name. Please check");                
                }
                
                // Validation ok
                String s = options.getSingleFileName().getPath();
                s = s.substring(0, s.lastIndexOf(File.separator));
                                            
                StartupProperties.getInstance().setLastSaveFolder(new File(s));
            }            
                        
            super.ok();
        } catch (Exception e) {
            SimpleDialog.error(e.getMessage());            
        }
    }
    
    private void init() {         
        // TODO Char encoding support required
        
        getBanner().setVisible(false);
        
        editDestFolder  = new JTextField();
        editDestFolder.setEditable(false);
        editDestFolder.setText(StartupProperties.getInstance().getLastSaveFolder().getAbsolutePath());
        btChooseFolder  = new JButton("...");
        btChooseFolder.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) { selectFolder(); }
        } );
        btSelected      = new JCheckBox("Write only selected messages");
        btManyFiles     = new JRadioButton("Many files (One file per message)");
        btManyFiles.setSelected(true);
        btOneFile       = new JRadioButton("One file (Framed message stream)");
        cbMessageEnc    = new JComboBox(new String[] { "HL7", "XML", "XML Expanded" });
        cbMessageEnc.setSelectedIndex(0);
        cbMessageEnc.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) { 
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().toString().startsWith("XML")) {
                        editDataExt.setSelectedItem("xml");
                    } else {
                        editDataExt.setSelectedItem("hl7");
                    }
                }
            }            
        });
        
        cbCharEnc      = new JComboBox(new String[] { "ISO-8859-1", "US-ASCII", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16" });
        editPrefix      = new JTextField();
        editDataExt     = new JComboBox(new String[] { "hl7", "xml", "txt", "dat" });
        editDataExt.setEditable(true);
        editSemaExt     = new JComboBox(new String[] { "sem", "frg" });
        editSemaExt.setEditable(true);
        btGenSema       = new JCheckBox();
        btGenSema.setSelected(true);
        editFilename    = new JTextField();
        editFilename.setEditable(false);
        editFilename.setText(StartupProperties.getInstance().getLastSaveFolder().getAbsolutePath().concat("\\hl7messages.hl7"));
        btFilename      = new JButton("...");
        btFilename.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) { selectFilename(); }
        } );
        editFrame       = new JEditorPane();
        editFrame.setContentType("text/html");
        editFrame.setEditable(false);
        editFrame.setBorder(LineBorder.createGrayLineBorder());
        btFrame         = new JButton("...");
        btFrame.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                FramingSetupDialog dlg = new FramingSetupDialog();
                dlg.setMessageFrame(frame);
                
                if (dlg.ask()) {
                    frame = dlg.getMessageFrame();
                    
                    updateFramePreview();
                }
            }
        }
        );
        
        updateFramePreview();
        
        //btSave          = new JButton("Save options dialog");
        
        ButtonGroup btnGrp  = new ButtonGroup();
        btnGrp.add(btManyFiles);
        btnGrp.add(btOneFile);
        
        FormLayout layout = new FormLayout(
        "8dlu, 8dlu, p, 4dlu, 50dlu, 4dlu, p, 4dlu, 50dlu, p:grow, 4dlu, 30dlu",
//            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, " +
                "p, 7dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 5dlu, p, 3dlu, p, 3dlu, " + 
                "p, 3dlu, p, 3dlu, p, 3dlu, p");   // rows
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        builder.add(new GradientLabel("Save options"),  cc.xyw(1,  1, 12));
        
        builder.addLabel("Message encoding",            cc.xyw(1,  3, 3));      // Ok
        builder.add(cbMessageEnc,                       cc.xyw(5,  3, 4));        

        builder.addLabel("Char encoding",               cc.xyw(1,  5, 3));      // Ok
        builder.add(cbCharEnc,                          cc.xyw(5,  5, 2));        
        
        builder.addLabel("Only selected",               cc.xyw(1,  7, 3));      // Ok
        builder.add(btSelected,                         cc.xyw(5,  7, 6));        

        builder.add(btManyFiles,                        cc.xyw(1,  9, 7));        // Ok        

        builder.addLabel("Folder:",                     cc.xy(3,    11));      // Ok
        builder.add(editDestFolder,                     cc.xyw(5,   11, 6));
        builder.add(btChooseFolder,                     cc.xy(12,   11));

        builder.addLabel("Prefix:",                     cc.xy(3,   13));        // Ok
        builder.add(editPrefix,                         cc.xy(5,   13));        // Ok
        builder.addLabel("Extension:",                  cc.xy(7,   13));        // Ok
        builder.add(editDataExt,                        cc.xy(9,   13));        // Ok

        builder.addLabel("Semaphor file",               cc.xy(3,   15));        // Ok        
        builder.add(btGenSema,                          cc.xy(5,   15));        // Ok        
        builder.addLabel("Extension:",                  cc.xy(7,   15));        // Ok
        builder.add(editSemaExt,                        cc.xy(9,   15));        // Ok
        
        builder.add(btOneFile,                          cc.xyw(1,  17, 7));        // Ok
        
        builder.addLabel("Filename",                    cc.xy(3,   19));        // Ok       
        builder.add(editFilename,                       cc.xyw(5,  19, 6));
        builder.add(btFilename,                         cc.xy(12,  19));
        
        builder.addLabel("Frame:",                      cc.xy(3,   21));
        builder.add(editFrame,                          cc.xyw(5,  21, 6));
        builder.add(btFrame,                            cc.xy(12,  21));
        
        add(builder.getPanel(), BorderLayout.CENTER);
        
        pack();
        
        setSize(getPreferredSize().width, getPreferredSize().height);
        
        setBounds(ToolKit.centerFrame(this, Desktop.getInstance()));                
    }
    
    private void selectFolder() {
        JDirectoryChooser dc = new JDirectoryChooser(editDestFolder.getText());
        dc.setDialogTitle("Choose filename");

        if(dc.showOpenDialog(this) == JDirectoryChooser.APPROVE_OPTION) {
            editDestFolder.setText(dc.getSelectedFile().getAbsolutePath());
        }
    }
    
    private void selectFilename() {
        JFileChooser fc = new JFileChooser(new File(editFilename.getText()).getParent());
        fc.addChoosableFileFilter(new Hl7XmlFileFilter());

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            editFilename.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }
        
    private void updateFramePreview() {
        String s = "<body><html><font face=\"Arial, Tahoma\" size=\"3\">";
        
        s = s.concat("<font color=\"fuchsia\">&lt;0x" + ((frame.getStartFrame() < 16)?"0":"") + Integer.toHexString(frame.getStartFrame())).concat("&gt;</font>");
        
        s = s.concat("HL7 Message");
        
        s = s.concat("<font color=\"fuchsia\">");
        for (int i=0; i<frame.getStopFrame().length; i++)
            s = s.concat("&lt;0x".concat(((frame.getStopFrame()[i] < 16)?"0":"") + Integer.toHexString(frame.getStopFrame()[i]))).concat("&gt;");        
        s = s.concat("</font>");
        
        s = s.concat("</font></body></html>");
        
        editFrame.setText(s);
    }
    
    private Frame           frame = new Frame();
    
    private JTextField      editDestFolder;
    private JButton         btChooseFolder;
    private JCheckBox       btSelected;
    private JRadioButton    btManyFiles;
    private JRadioButton    btOneFile;
    private JComboBox       cbMessageEnc;
    private JComboBox       cbCharEnc;
    private JTextField      editPrefix;
    private JComboBox       editDataExt;
    private JComboBox       editSemaExt;
    private JCheckBox       btGenSema;
    private JTextField      editFilename;
    private JButton         btFilename;
    private JEditorPane     editFrame;
    private JButton         btFrame;
    
}
