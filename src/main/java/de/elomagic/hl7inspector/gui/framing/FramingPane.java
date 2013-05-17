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
package de.elomagic.hl7inspector.gui.framing;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.io.Frame;

import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author rambow
 */
public class FramingPane extends JPanel {
    private JComboBox cbbStartChar = new JComboBox();
    private JComboBox cbbStopChar1 = new JComboBox();
    private JComboBox cbbStopChar2 = new JComboBox();

    /**
     * Creates a new instance of FramingPanel.
     */
    public FramingPane() {
        init();
    }

    protected final void init() {
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

        setLayout(new BorderLayout());

        FormLayout layout = new FormLayout(
                "p, 4dlu, p, p:grow",
                "p, 4dlu, p, 4dlu, p, 4dlu, p , p:grow, p");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        // 1st row
        builder.addLabel("Start char:", cc.xy(1, 1));
        builder.add(cbbStartChar, cc.xy(3, 1));

        builder.addLabel("1. Stop char:", cc.xy(1, 3));
        builder.add(cbbStopChar1, cc.xy(3, 3));

        builder.addLabel("2. Stop char:", cc.xy(1, 5));
        builder.add(cbbStopChar2, cc.xy(3, 5));

        //    builder.add(btRemove,                 cc.xy (3,   3));

        //    builder.add(btImport,                 cc.xy (3,   5));

        //    builder.add(btRemove,                 cc.xy (3,   7));

        add(builder.getPanel(), BorderLayout.CENTER);

        //    getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        //    add(builder.getPanel());

        //    lstProfiles.setModel(new DefaultLVectorListModel(StartupProperties.getInstance().getProfiles()));
        //    lstProfiles.setCellRenderer(new ProfileCellRenderer());

        //setSize(400, 300);

        StartupProperties prop = StartupProperties.getInstance();

        int c = Integer.parseInt(prop.getProperty(StartupProperties.DEFAULT_FRAME_START, "11"));
        cbbStartChar.setSelectedIndex(c);

        c = Integer.parseInt(prop.getProperty(StartupProperties.DEFAULT_FRAME_STOP1, "28"));
        cbbStopChar1.setSelectedIndex(c != -1 ? c : 32);

        c = Integer.parseInt(prop.getProperty(StartupProperties.DEFAULT_FRAME_STOP2, "13"));
        cbbStopChar2.setSelectedIndex(c != -1 ? c : 32);

    }

    public Frame getMessageFrame() {
        Character startChar = (cbbStartChar.getSelectedIndex() < 32) ? new Character((char)cbbStartChar.getSelectedIndex()) : null;
        Character stopChar1 = (cbbStopChar1.getSelectedIndex() < 32) ? new Character((char)cbbStopChar1.getSelectedIndex()) : null;
        Character stopChar2 = (cbbStopChar2.getSelectedIndex() < 32) ? new Character((char)cbbStopChar2.getSelectedIndex()) : null;

        return new Frame(startChar, stopChar1, stopChar2);
    }

    public void setMessageFrame(Frame value) {
        cbbStartChar.setSelectedIndex(value.getStartFrame());
        cbbStopChar1.setSelectedIndex(value.getStopFrameLength() == 0 ? 32 : value.getStopFrame()[0]);
        cbbStopChar2.setSelectedIndex(value.getStopFrameLength() < 2 ? 32 : value.getStopFrame()[1]);
    }
}
