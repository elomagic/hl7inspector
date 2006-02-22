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

package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.profile.ProfileFile;
import java.util.Vector;
import javax.swing.AbstractListModel;

/**
 *
 * @author rambow
 */
public class VectorListModel extends AbstractListModel {
  
  /** Creates a new instance of ProfileManagerModel */
  public VectorListModel(Vector<ProfileFile> v) { 
    super(); 
    vector = v; 
  }
  
  public void add(ProfileFile object) {
    vector.add(object);
    fireIntervalAdded(this, vector.size(), vector.size()-1);
  }
  
  public void remove(Object object) {
    int index0 = vector.size();
    int index1 = vector.size()-1;
    vector.remove(object);
    fireIntervalRemoved(this, index0, index1);
  }
  
  public int indexOf(Object o) { return vector.indexOf(o); }

  public Object getElementAt(int index) { return vector.get(index); }    

  public int getSize() { return vector.size(); }
  
  private Vector<ProfileFile> vector;
  
}
