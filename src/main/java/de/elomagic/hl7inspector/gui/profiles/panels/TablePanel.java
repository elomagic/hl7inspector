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
import de.elomagic.hl7inspector.gui.profiles.model.TableModel;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.TableItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rambow
 */
public class TablePanel extends ProfileTablePanel {
    private static final long serialVersionUID = -3661005431150970703L;

    /**
     * Creates a new instance of TablePanel.
     */
    public TablePanel(PanelDialog d) {
        super(d);
    }

    @Override
    protected void init() {
        model = new TableModel();

        super.init();
    }

    @Override
    public void write(Profile profile) {
        TableModel m = (TableModel)getModel();

        List<TableItem> list = new ArrayList<>();

        for(int i = 0; i < m.getRowCount(); i++) {
            list.add(m.getTableItem(i));
        }

        profile.setTableDataList(list);
    }

    @Override
    public void read(Profile profile) {
        ((TableModel)model).setModel(profile.getTableDataList());
    }

    @Override
    public String getTitle() {
        return "Tables";
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
