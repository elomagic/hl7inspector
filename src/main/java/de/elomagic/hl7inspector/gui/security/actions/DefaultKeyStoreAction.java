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
package de.elomagic.hl7inspector.gui.security.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JList;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.gui.SimpleDialog;

/**
 *
 * @author rambow
 */
public class DefaultKeyStoreAction extends AbstractAction {
    private JList list;

    /**
     * Creates a new instance of FileOpenAction.
     */
    public DefaultKeyStoreAction(JList list) {
        super("Set private", null);//ResourceLoader.loadImageIcon("edit_add.png"));

        this.list = list;

        putValue(SHORT_DESCRIPTION, "Set selected keystore as default private keystore");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(list.getSelectedValue() != null) {
                File file = (File)list.getSelectedValue();

                StartupProperties.getInstance().setProperty(StartupProperties.DEFAULT_PRIVATE_KEYSTORE, file.getAbsolutePath());
                list.repaint();
            } else {
                SimpleDialog.error("No keystore selected!");
            }
        } catch(Exception ee) {
            Logger.getLogger(getClass()).error(ee.getMessage(), ee);
        }
    }
}
