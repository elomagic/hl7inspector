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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.simpleframework.xml.Element;

/**
 *
 * @author carsten.rambow
 */
public class VersionItemBean {

    @Element(name = "date", required = false)
    private String date = "";
    public Date getDate() throws ParseException {
        return ("".equals(date)) ? null : new SimpleDateFormat("yyyy.MM.dd").parse(date);
    }

    public void setDate(Date date) {
        this.date = (date == null) ? "" : new SimpleDateFormat("yyyy.MM.dd").format(date);
    }

    @Element(name = "version", required = false)
    private String version = "";
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Element(name = "homepage", required = false)
    private String homepage = "";
    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

}
