/*
 * Copyright 2016 Carsten Rambow
 * 
 * Licensed under the GNU Public License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.gnu.org/licenses/gpl.txt
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.elomagic.hl7inspector.file.filters;

import java.util.Arrays;
import java.util.Collection;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author Carsten Rambow
 */
public final class ExtensionFilters {

    private ExtensionFilters() {
    }

    public static Collection<? extends ExtensionFilter> getProfileExtensionFilters() {

        return Arrays.asList(
                new FileChooser.ExtensionFilter("HL7 definitions profile (*.hip)", "*.hip"),
                getAllFilesExtensionFilter()
        );

    }

    public static Collection<? extends ExtensionFilter> getExecutableExtensionFilters() {

        return Arrays.asList(
                new ExtensionFilter("Executeable Files (*.exe)", "*.exe"),
                getAllFilesExtensionFilter()
        );

    }

    public static ExtensionFilter getAllFilesExtensionFilter() {
        return new ExtensionFilter("All Files (*.*)", "*.*");
    }

    public static ExtensionFilter getXmlFileExtensionFilter() {
        return new ExtensionFilter("XML Files (*.xml)", "*.xml");
    }

    public static ExtensionFilter getHL7FileExtensionFilter() {
        return new ExtensionFilter("HL7 Files (*.hl7)", "*.hl7");
    }

}
