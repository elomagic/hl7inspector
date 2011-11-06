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
import de.elomagic.hl7inspector.hl7.Hl7Encoder;
import de.elomagic.hl7inspector.hl7.model.Component;
import de.elomagic.hl7inspector.hl7.model.Delimiters;
import de.elomagic.hl7inspector.hl7.model.Field;
import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.RepetitionField;
import de.elomagic.hl7inspector.hl7.model.Subcomponent;
import de.elomagic.hl7inspector.profile.MessageDescriptor;
import de.elomagic.hl7inspector.profile.ProfileIO;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author rambow
 */
public class HL7ObjectEditor extends BaseDialog {

    private static final long serialVersionUID = 5840340196322853677L;

    /** Creates a new instance of ImportOptionsDialog */
    public HL7ObjectEditor() {
        super(Desktop.getInstance());

        init();
    }

    private void init() {
        getBanner().setVisible(false);

        setTitle("HL7 Editor");
        setModal(true);

        editType = new JTextField();
        editType.setEditable(false);

        editValue = new JTextField();
        editValue.setFont(Font.decode(StartupProperties.getInstance().getTreeFontName()));

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setFont(Font.decode("Arial"));

        scrollPane = new JScrollPane(editorPane);

        paneDesc = new JPanel(new BorderLayout());
        paneDesc.add(scrollPane, BorderLayout.CENTER);

        cbEncode = new JCheckBox();

        FormLayout layout = new FormLayout(
                "p, 4dlu, p:grow",
                //            "8dlu, left:max(40dlu;p), 75dlu, 75dlu, 7dlu, right:p, 4dlu, 75dlu",
                "p, 4dlu, p, 3dlu, p, 3dlu, p, 7dlu, p, 4dlu, top:min(40dlu;p):grow");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.add(new GradientLabel("Node value"), cc.xyw(1, 1, 3));

        builder.addLabel("Type:", cc.xy(1, 3));
        builder.add(editType, cc.xy(3, 3));

        builder.addLabel("Value:", cc.xy(1, 5));
        builder.add(editValue, cc.xy(3, 5));

        builder.addLabel("Encode:", cc.xy(1, 7));
        builder.add(cbEncode, cc.xy(3, 7));

        builder.add(new GradientLabel("Description"), cc.xyw(1, 9, 3));

        builder.add(paneDesc, cc.xyw(1, 11, 3));

        getContentPane().add(builder.getPanel(), BorderLayout.CENTER);

        pack();

        setSize(400, 400);

        setLocationRelativeTo(getOwner());
        editValue.requestFocus();
    }

    private JTextField editType;
    private JTextField editValue;
    private JPanel paneDesc;
    private JScrollPane scrollPane;
    private JEditorPane editorPane;
    private JCheckBox cbEncode;

    private boolean isEncode() {
        return cbEncode.isSelected();
    }

    public String getValue() {
        return editValue.getText();
    }

    public String getValue(Delimiters d) {
        String r = getValue();
        if (isEncode()) {
            Hl7Encoder enc = new Hl7Encoder(new Delimiters());
            r = enc.encodeString(r);
        }

        return r;
    }

    private Hl7Object o = null;

    public void setValue(Hl7Object hl7Object) {
        o = hl7Object;

        String s = o.getClass().getName();
        editType.setText(s.substring(s.lastIndexOf(".") + 1));

        String value = o.toString();
        editValue.setText(value);

        boolean b = (value.indexOf('|') != -1) || (value.indexOf('&') != -1) || (value.indexOf('~') != -1) || (value.indexOf('\\') != -1);
        cbEncode.setSelected(!b);

        MessageDescriptor md = new MessageDescriptor(ProfileIO.getDefault());
        String desc = md.getDescription(o, true);
        setDescription(desc);
    }

    private void setDescription(String value) {
        editorPane.setText(value);
//        editDesc.getScrollPane().getVerticalScrollBar().setVisibleAmount(0);        
        //editDesc.getScrollPane().scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        scrollPane.getVerticalScrollBar().setValue(0);
    }

    @Override
    public void ok() {
        boolean b = isEncode();

        if (!b) {
            if (o instanceof RepetitionField) {
                b = getValue().indexOf(Delimiters.DEFAULT_FIELD) == -1;
            } else if (o instanceof Field) {
                b = ((getValue().indexOf(Delimiters.DEFAULT_REPETITION) == -1)
                        && (getValue().indexOf(Delimiters.DEFAULT_FIELD) == -1));
            } else if (o instanceof Component) {
                b = ((getValue().indexOf(Delimiters.DEFAULT_REPETITION) == -1)
                        && (getValue().indexOf(Delimiters.DEFAULT_FIELD) == -1)
                        && (getValue().indexOf(Delimiters.DEFAULT_COMPONENT) == -1));
            } else if (o instanceof Subcomponent) {
                b = ((getValue().indexOf(Delimiters.DEFAULT_REPETITION) == -1)
                        && (getValue().indexOf(Delimiters.DEFAULT_FIELD) == -1)
                        && (getValue().indexOf(Delimiters.DEFAULT_COMPONENT) == -1)
                        && (getValue().indexOf(Delimiters.DEFAULT_SUPCOMPONENT) == -1));
            } else {
                b = true;
            }
        }

        if (b) {
            super.ok();
        } else {
            SimpleDialog.error("The value includes inhibits encoding characters for type " + editType.getText());
        }
    }

}
