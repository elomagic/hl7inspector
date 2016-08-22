/*
 * Copyright 2016 Carsten Rambow
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
package de.elomagic.hl7inspector.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Carsten Rambow
 */
public class XmlDateAdapter extends XmlAdapter<String, Date> {

    private static final String PATTERN = "yyyy.MM.dd";

    @Override
    public Date unmarshal(final String value) throws Exception {
        return new SimpleDateFormat(PATTERN).parse(value);
    }

    @Override
    public String marshal(final Date date) throws Exception {
        return new SimpleDateFormat(PATTERN).format(date);
    }

}
