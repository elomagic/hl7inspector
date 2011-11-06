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

import de.elomagic.hl7inspector.gui.PanelDialog;
import de.elomagic.hl7inspector.gui.profiles.model.DataElementModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.DataElement;
import de.elomagic.hl7inspector.profile.Profile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rambow
 */
public class DataElementPanel extends ProfileTablePanel {

    private static final long serialVersionUID = -3583520327885944253L;

    /** Creates a new instance of FileIdPane */
    public DataElementPanel(PanelDialog d) {
        super(d);
    }

    @Override
    protected void init() {
        model = new DataElementModel();

        super.init();
    }

    @Override
    public void write(Profile profile) {
        DataElementModel m = (DataElementModel) getModel();

        List<DataElement> list = new ArrayList<DataElement>();

        for (int i = 0; i < m.getRowCount(); i++) {
            list.add(m.getDataElement(i));
        }

        profile.setDataElementList(list);
    }

    @Override
    public void read(Profile profile) {
        ((DataElementModel) model).setModel(profile.getDataElementList());
    }

    @Override
    public String getTitle() {
        return "Data Elements";
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
