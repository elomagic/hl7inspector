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
package de.elomagic.hl7inspector.profile;

import java.io.File;

/**
 *
 * @author rambow
 */
public class ProfileFile extends File {
    private static final long serialVersionUID = -8484823376425619787L;
    private String desc = "";

    /** Creates a new instance of ProfileFile */
    public ProfileFile(String pathname) {
        super(pathname);
    }

    public ProfileFile(File file) {
        super(file.getAbsolutePath());
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String value) {
        desc = value;
    }
}
