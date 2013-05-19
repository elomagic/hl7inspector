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

import de.elomagic.hl7inspector.utils.BundleTool;
import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Abstract class to ease defined file filters.
 */
public abstract class AbstractFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {
    protected ResourceBundle bundle = BundleTool.getBundle(AbstractFileFilter.class);

    /**
     * Creates a new instance of AbstractFileFilter.
     */
    public AbstractFileFilter() {
        super();
    }

    /**
     * Whether the given file is accepted by this filter.
     */
    @Override
    public boolean accept(final File file) {
        boolean r = file.isDirectory();

        if(file.isDirectory()) {
            return false;
        }

        String[] parts = file.getAbsolutePath().split("\\.");

        if(parts == null || parts.length < 2) {
            return false;
        }

        String ext = parts[parts.length - 1].toLowerCase();
        return getFileExtentions().contains(ext);
    }

    /**
     * Returns a list of valid file extension.
     *
     * @return List of type string
     */
    protected abstract List<String> getFileExtentions();
}
