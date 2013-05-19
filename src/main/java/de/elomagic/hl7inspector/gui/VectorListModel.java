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
 */
package de.elomagic.hl7inspector.gui;

import java.util.List;
import javax.swing.AbstractListModel;

/**
 *
 * @author rambow
 */
public class VectorListModel<E extends Object> extends AbstractListModel {
    private List<E> list;

    /**
     * Creates a new instance of VectorListModel.
     */
    public VectorListModel(List<E> v) {
        super();
        list = v;
    }

    public void add(E object) {
        list.add(object);
        fireIntervalAdded(this, list.size(), list.size() - 1);
    }

    public void remove(E object) {
        int index0 = list.size();
        int index1 = list.size() - 1;
        list.remove(object);
        fireIntervalRemoved(this, index0, index1);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public int getSize() {
        return list.size();
    }
}
