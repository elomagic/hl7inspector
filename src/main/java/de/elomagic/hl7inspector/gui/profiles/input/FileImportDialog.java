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
package de.elomagic.hl7inspector.gui.profiles.input;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BaseDialog;

import de.elomagic.hl7inspector.file.filters.CsvFileFilter;
import de.elomagic.hl7inspector.file.filters.TextFileFilter;
import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.GradientLabel;
import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.utils.StringVector;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class FileImportDialog extends BaseDialog {
    private File selectedFile = null;
    private TableModel model;
    private JTextField editFile;
    private JButton btChooseFile;
    private JComboBox cbMapper;
    private JSpinner btBeginFrom;
    private JComboBox cbSepChar;
    private JScrollPane scroller;
    private JTable tblMap;
    private JTable tblPreview;

    /**
     * Creates a new instance of FileImportDialog.
     */
    public FileImportDialog(TableModel m) {
        super(Desktop.getInstance().getMainFrame(), "File Import Dialog", true);

        model = m;

        init();
    }

    @Override
    public void ok() {
        super.ok();
    }

    public ArrayList<String> getMappingList() {
        ArrayList<String> v = new ArrayList<>();

        TableModel m = tblMap.getModel();

        for(int i = 1; i < m.getColumnCount(); i++) {
            v.add(m.getValueAt(0, i).toString());
        }

        return v;
    }

    public File getFile() {
        return selectedFile;
    }

    private void init() {
        getBanner().setVisible(false);
        setAlwaysOnTop(true);

        editFile = new JTextField();
        editFile.setEditable(false);

        btChooseFile = new JButton("...");
        btChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                selectFilename();
            }
        });

        btBeginFrom = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        btBeginFrom.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(selectedFile != null) {
                    loadFile(selectedFile);
                }
            }
        });

        cbSepChar = new JComboBox(new String[] {",", ";", "TAB", "SPACE"});
        cbSepChar.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(selectedFile != null) {
                    loadFile(selectedFile);
                }
            }
        });
        //cbSepChar.setEditable(true);

        initTables();

        cbSepChar.setSelectedIndex(0);

        FormLayout layout = new FormLayout(
                "p, 4dlu, 40dlu, p:grow, 4dlu, 30dlu",
                "p, 3dlu, p, 7dlu, p, 3dlu, p, 3dlu, p, 7dlu, p, 3dlu, p, p:grow");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.add(new GradientLabel("Filename"), cc.xyw(1, 1, 6));

        builder.addLabel("Filename:", cc.xy(1, 3));
        builder.add(editFile, cc.xyw(3, 3, 2));
        builder.add(btChooseFile, cc.xy(6, 3));

        builder.add(new GradientLabel("Options"), cc.xyw(1, 5, 6));

        builder.addLabel("Begin line", cc.xy(1, 7));
        builder.add(btBeginFrom, cc.xy(3, 7));

        builder.addLabel("Separate char", cc.xy(1, 9));
        builder.add(cbSepChar, cc.xy(3, 9));

        builder.add(new GradientLabel("Columns mapping"), cc.xyw(1, 11, 6));

        builder.add(scroller, cc.xyw(1, 13, 6));


        add(builder.getPanel(), BorderLayout.CENTER);
        pack();
        setSize((getPreferredSize().width < 640) ? 640 : getPreferredSize().width, (getPreferredSize().height < 480) ? 480 : getPreferredSize().height);
        setLocationRelativeTo(Desktop.getInstance().getMainFrame());
    }

    private void initTables() {
        tblMap = new JTable();
        tblPreview = new JTable();
        //tblPreview.setSelectionMode()

        FormLayout layout = new FormLayout(
                "p:grow",
                "p, p, p:grow");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
//        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.add(tblMap, cc.xy(1, 1));
        builder.add(tblPreview, cc.xy(1, 2));

        scroller = new JScrollPane(builder.getPanel());
    }

    private void selectFilename() {
        File path = new File(editFile.getText().isEmpty() ? System.getProperty("user.dir") : editFile.getText());
        JFileChooser fc = new JFileChooser(path);
        fc.addChoosableFileFilter(new TextFileFilter());
        fc.addChoosableFileFilter(new CsvFileFilter());
        fc.setDialogTitle("Choose CSV import file");

        if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            editFile.setText(fc.getSelectedFile().getAbsolutePath());
            selectedFile = fc.getSelectedFile();
            loadFile(fc.getSelectedFile());
        }
    }

    public char getSeparatorChar() {
        char sep;

        switch(cbSepChar.getSelectedIndex()) {
            case 0:
                sep = ',';
                break;
            case 1:
                sep = ';';
                break;
            case 2:
                sep = (char)7;
                break;
            case 3:
                sep = ' ';
                break;
            default:
                sep = ',';
                break;
        }

        return sep;
    }

    public int getBeginFromLine() {
        return Integer.parseInt(btBeginFrom.getValue().toString());
    }

    private void loadFile(File file) {
        ArrayList<Object> lines = new ArrayList<>();

        char sep = getSeparatorChar();

        try {
            try (FileReader rin = new FileReader(file); LineNumberReader lin = new LineNumberReader(rin)) {
                int l = 0;
                String line = lin.readLine();

                int begin = Integer.parseInt(btBeginFrom.getValue().toString());

                while((line != null) && (l < (10 + begin))) {
                    l++;

                    if(l >= begin) {
                        lines.add(new StringVector(line, sep));
                    }

                    line = lin.readLine();
                }
            }
        } catch(IOException | NumberFormatException e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            SimpleDialog.error(e);
        }

        updatePreview(lines);
    }

    private void updatePreview(ArrayList<Object> lines) {
        ArrayList<String> mapItems = new ArrayList<>();
        mapItems.add("-");

        for(int i = 0; i < model.getColumnCount(); i++) {
            mapItems.add(model.getColumnName(i));
        }

        ImportFileModel importModel = new ImportFileModel(lines);

        cbMapper = new JComboBox(mapItems.toArray());

        tblMap.setModel(new MapFieldModel(importModel));
        tblMap.getColumnModel().getColumn(0).setMaxWidth(40);
        tblMap.getModel().addTableModelListener(new MappingChangeAction());
        tblMap.setDefaultEditor(String.class, new DefaultCellEditor(cbMapper));
        tblMap.setDefaultRenderer(String.class, new MapFieldCellRenderer());
        tblMap.setDefaultRenderer(JLabel.class, new PreviewCellRenderer());

        tblPreview.setModel(importModel);
        tblPreview.getColumnModel().getColumn(0).setMaxWidth(40);
        tblPreview.setDefaultRenderer(Object.class, new PreviewCellRenderer());

        pack();
    }

    class MappingChangeAction implements TableModelListener {
        @Override
        public void tableChanged(javax.swing.event.TableModelEvent e) {
            if(e.getType() == TableModelEvent.UPDATE) {
                int col = e.getColumn();
                Object aValue = tblMap.getModel().getValueAt(0, col);

                for(int i = 0; i < tblMap.getModel().getColumnCount(); i++) {
                    if((i != col) && (tblMap.getModel().getValueAt(0, i).equals(aValue)) && !("-".equals(aValue))) {
                        tblMap.getModel().setValueAt("-", 0, i);
                    }
                }
            }
        }
    }
}
