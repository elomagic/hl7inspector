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
 */
package de.elomagic.hl7inspector.file.filters;

import java.util.Collections;
import java.util.List;

/**
 * Text file filter.
 */
public class TextFileFilter extends AbstractFileFilter {
    @Override
    protected List<String> getFileExtentions() {
        return Collections.singletonList("txt");
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     *
     * @see FileView#getName
     */
    @Override
    public String getDescription() {
        return "Text files";
    }
}
