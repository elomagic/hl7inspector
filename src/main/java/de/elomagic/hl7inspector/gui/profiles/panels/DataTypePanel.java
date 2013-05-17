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
package de.elomagic.hl7inspector.gui.profiles.panels;

import java.util.ArrayList;
import java.util.List;

import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.gui.profiles.model.DataTypeModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.DataTypeItem;
import de.elomagic.hl7inspector.profile.Profile;

/**
 *
 * @author rambow
 */
public class DataTypePanel extends ProfileTablePanel {
    private static final long serialVersionUID = -1867620600189483742L;

    /**
     * Creates a new instance of DataTypePanel.
     */
    public DataTypePanel(PanelDialog d) {
        super(d);
    }

    @Override
    protected void init() {
        model = new DataTypeModel();

        super.init();
    }

    @Override
    public void write(Profile profile) {
        DataTypeModel m = (DataTypeModel)getModel();

        List<DataTypeItem> list = new ArrayList<>();

        for(int i = 0; i < m.getRowCount(); i++) {
            DataTypeItem item = m.getDataTypeItem(i);
            list.add(item);
        }

        profile.setDataTypeList(list);
    }

    @Override
    public void read(Profile profile) {
        ((DataTypeModel)model).setModel(profile.getDataTypeList());
    }

    @Override
    public String getTitle() {
        return "Data Types";
    }

    @Override
    public javax.swing.Icon getIcon() {
        return ResourceLoader.loadImageIcon("x-profile-data-editor.png", ResourceLoader.LARGE_IMAGE);
    }

    @Override
    public String getDescription() {
        return "";
    }
}
