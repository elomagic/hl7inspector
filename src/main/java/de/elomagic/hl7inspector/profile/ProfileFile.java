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
 *
 */
package de.elomagic.hl7inspector.profile;

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author Carsten Rambow
 */
public class ProfileFile {

    private final Path file;
    private String desc = "";

//    public ProfileFile(final String pathname) {
//        this(Paths.get(pathname));
//    }
    public ProfileFile(final Path file) {
        this.file = file;
    }

    public Path getFile() {
        return file;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(final String value) {
        desc = value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.file);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final ProfileFile other = (ProfileFile)obj;
        if(!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }

}
