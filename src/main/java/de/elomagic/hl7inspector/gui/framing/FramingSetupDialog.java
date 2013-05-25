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
package de.elomagic.hl7inspector.gui.framing;

import com.l2fprod.common.swing.BaseDialog;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.io.Frame;

import java.awt.BorderLayout;

/**
 *
 * @author rambow
 */
public class FramingSetupDialog extends BaseDialog {
    private FramingPane fp = new FramingPane();

    /**
     * Creates a new instance of FramingSetupDialog.
     */
    public FramingSetupDialog() {
        super(Desktop.getInstance().getMainFrame(), "Message Framing Setup Dialog", true);

        init();
    }

    public Frame getMessageFrame() {
        return fp.getMessageFrame();
    }

    public void setMessageFrame(Frame value) {
        fp.setMessageFrame(value);
    }

    private void init() {
        getBanner().setTitle("Setup framing of messages");

        getContentPane().add(fp, BorderLayout.CENTER);

        pack();

        setSize(getPreferredSize());
        setLocationRelativeTo(getOwner());
    }
}
