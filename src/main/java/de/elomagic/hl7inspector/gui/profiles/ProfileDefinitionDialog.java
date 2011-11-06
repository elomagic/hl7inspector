/*
 * Copyright 2010 Carsten Rambow
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
package de.elomagic.hl7inspector.gui.profiles;

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.gui.*;
import de.elomagic.hl7inspector.gui.profiles.panels.*;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.profile.ProfileIO;
import java.io.FileInputStream;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ProfileDefinitionDialog extends PanelDialog {

    private static final long serialVersionUID = -6813753748983568133L;

    /** Creates a new instance of ProfileDefinitionDialog */
    public ProfileDefinitionDialog(ProfileFile _file) throws Exception {
        super(Desktop.getInstance(), "Profile Definition", true);

        file = _file;
    }

    @Override
    public boolean ask() {
        boolean result = false;

        profile = new Profile();

        try {
            FileInputStream fin = new FileInputStream(file);
            try {
                profile = ProfileIO.loadFromStream(fin);
            } finally {
                fin.close();
            }

            if (profile.getSchemaVersion().compareTo(Hl7Inspector.getVersion()) > 0) {
                SimpleDialog.warn("Unable to handle profile format. Please update HL7 Inspector.");
            } else {
                result = super.ask();
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass()).error(ex.getMessage(), ex);
            SimpleDialog.error(ex, "Unable to read profile");
        }

        return result;
    }

    @Override
    protected void read() {
        List list = getPanelList();
        for (int i = 0; i < list.size(); i++) {
            ((ProfilePanel) list.get(i)).read(profile);
        }

        pnlCom.setValidateStatus(profile.validate().size() == 0);
    }

    private ProfileFile file;
    private Profile profile;
    private CommonPanel pnlCom;

    public CommonPanel getCommonPanel() {
        return pnlCom;
    }

    @Override
    protected void init() {
        try {
            pnlCom = new CommonPanel(this);

            getPanelList().add(pnlCom);
            getPanelList().add(new ValidatePanel(this));
            getPanelList().add(new SegmentPanel(this));
            getPanelList().add(new DataElementPanel(this));
            getPanelList().add(new DataTypePanel(this));
            getPanelList().add(new TablePanel(this));

            super.init();

            setSize(750, 500);

//            for (int i = 0; i<getPanelList().size(); i++) {
//                if (getPanelList().get(0) instanceof ProfilePanel)
//                    ((ProfilePanel)getPanelList().get(0)).getTable().dsizeColumnsToFit(0);
//            }

            setLocationRelativeTo(Desktop.getInstance());
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            SimpleDialog.error(e, e.getMessage());
        }
    }

    public ProfileFile getProfileFile() {
        return file;
    }

    public Profile getProfile() {
        return profile;
    }

}
