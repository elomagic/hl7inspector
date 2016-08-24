/*
 * Copyright 2016 Carsten Rambow
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
package de.elomagic.hl7inspector.gui.security;

import java.security.KeyStore;
import java.util.List;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.PanelDialog;

/**
 *
 * @author Carsten Rambow
 */
public class KeyStoreDialog extends PanelDialog {

    private final KeyStore keyStore;
    private CommonPanel pnlCom;

    /**
     * Creates a new instance of ProfileDefinitionDialog.
     *
     * @param keyStore
     * @throws java.lang.Exception
     */
    public KeyStoreDialog(final KeyStore keyStore) throws Exception {
        super(Desktop.getInstance().getMainFrame(), "Keystore Dialog", true);

        this.keyStore = keyStore;
    }

    @Override
    protected void read() {
        List list = getPanelList();
        for(int i = 0; i < list.size(); i++) {
            ((KeyStorePanel)list.get(i)).read(keyStore);
        }
    }

    @Override
    protected void init() {
        try {
            pnlCom = new CommonPanel(this);

            getPanelList().add(pnlCom);
//            getPanelList().add(new ValidatePanel(this));
//            getPanelList().add(new SegmentPanel(this));
//            getPanelList().add(new DataElementPanel(this));
//            getPanelList().add(new DataTypePanel(this));
//            getPanelList().add(new TablePanel(this));

            super.init();

            setSize(750, 500);

            setLocationRelativeTo(Desktop.getInstance().getMainFrame());
        } catch(Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            Notification.error(e, e.getMessage());
        }
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }
}
