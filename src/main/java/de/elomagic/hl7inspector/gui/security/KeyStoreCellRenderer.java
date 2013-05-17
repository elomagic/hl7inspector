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
package de.elomagic.hl7inspector.gui.security;

import java.awt.Component;
import java.awt.SystemColor;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.images.ResourceLoader;

/**
 *
 * @author rambow
 */
public class KeyStoreCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {
    private static final long serialVersionUID = -2700735747239354278L;

    /** Creates a new instance of ProfileCellRenderer */
    public KeyStoreCellRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        File file = (File)value;

        setToolTipText("File: " + file.toString());
        if(!file.exists()) {
            setIcon(ResourceLoader.loadImageIcon("warning.png"));
            setToolTipText("Profile " + file.toString() + " not found!");
        } else if(file.toString().equals(StartupProperties.getInstance().getProperty(StartupProperties.DEFAULT_PRIVATE_KEYSTORE, ""))) {
            setIcon(ResourceLoader.loadImageIcon("ok.png"));
            setToolTipText("");
        } else {
            setIcon(ResourceLoader.loadImageIcon("clear.png"));
            setToolTipText("");
        }

        setText(file.getAbsolutePath());

        setBackground(isSelected ? SystemColor.textHighlight : SystemColor.text);
        setForeground(isSelected ? SystemColor.textHighlightText : SystemColor.textText);

        return this;
    }
}
