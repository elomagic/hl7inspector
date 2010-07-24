/*
 * Copyright 2008 Carsten Rambow
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
package de.elomagic.hl7inspector.autoupdate;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author carsten.rambow
 */
@Root(name = "elomagic", strict = false)
public class VersionBean {

    @Element(name = "hl7_inspector2", required = false)
    private VersionItemBean hl7Inspector = new VersionItemBean();
    public VersionItemBean getHl7Inspector2() {
        return hl7Inspector;
    }

    public void setHl7Inspector2(VersionItemBean hl7Inspector) {
        this.hl7Inspector = hl7Inspector;
    }

}
