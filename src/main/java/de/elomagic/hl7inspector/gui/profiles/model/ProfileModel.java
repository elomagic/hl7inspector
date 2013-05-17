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
package de.elomagic.hl7inspector.gui.profiles.model;

import de.elomagic.hl7inspector.gui.ArrayListModel;

/**
 *
 * @author rambow
 */
public abstract class ProfileModel<E extends Object> extends ArrayListModel<E> {
    private static final long serialVersionUID = -5817455151118652785L;

    /**
     * Creates a new instance of DataTypeModel
     */
    public ProfileModel() {
        super();
    }

    /**
     * Creates a default class object an add it to the model
     */
    public int addRowObject() throws InstantiationException, IllegalAccessException {
        E o = getDefaultRowClass().newInstance();
        return addRow(o);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
//        boolean retValue;

//        retValue = (rowIndex > 0);

        //retValue = super.isCellEditable(rowIndex, columnIndex);
        return true;
    }

    public abstract Class<E> getDefaultRowClass();
}
