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
package de.elomagic.hl7inspector.gui.security;

import de.elomagic.hl7inspector.gui.*;
import java.security.KeyStore;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class KeyStoreDialog extends PanelDialog {

    /** Creates a new instance of ProfileDefinitionDialog */
    public KeyStoreDialog(KeyStore keyStore) throws Exception {
        super(Desktop.getInstance(), "Keystore Dialog", true);

        this.keyStore = keyStore;
    }

    @Override
    public boolean ask() {
        boolean result = false;

//        profile = new Profile();
//
//        try {
//            FileInputStream fin = new FileInputStream(file);
//            try {
//                profile.loadFromStream(fin);
//            } finally {
//                fin.close();
//            }
//            
//            if (profile.getSchemaVersion().compareTo(Hl7Inspector.getVersion()) > 0) {
//                SimpleDialog.warn("Unable to handle profile format. Please update HL7 Inspector.");
//            } else {
//                result = super.ask();
//            }
//        } catch (Exception e) {
//            Logger.getLogger(getClass()).error(e.getMessage(), e);
//            SimpleDialog.error(e, "Unable to read profile");
//        }

        return super.ask();
    }

    @Override
    protected void read() {
        List list = getPanelList();
        for (int i = 0; i < list.size(); i++) {
            ((KeyStorePanel) list.get(i)).read(keyStore);
        }
    }

    private KeyStore keyStore;
//    private Profile     profile;
    private CommonPanel pnlCom;
//    public CommonPanel getCommonPanel() { return pnlCom; }

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

            setLocationRelativeTo(Desktop.getInstance());
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            SimpleDialog.error(e, e.getMessage());
        }
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

}
