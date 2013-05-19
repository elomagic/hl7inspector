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
package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.hl7.Hl7Decoder;
import de.elomagic.hl7inspector.hl7.model.Delimiters;

import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author rambow
 */
public class Hl7DecoderViewPane extends ScrollableEditorPane {
    private Delimiters delimiters;
    private Color defBackColor;

    /**
     * Creates a new instance of Hl7DecoderViewPane.
     */
    public Hl7DecoderViewPane(Delimiters delimiters) {
        super();

        getCaption().setVisible(false);
        defBackColor = getEditorPane().getBackground();
        this.delimiters = delimiters;
    }

    public void setEncodedText(String value) {
        Hl7Decoder de = new Hl7Decoder(delimiters);

        StringBuilder sb = new StringBuilder();
        sb.append("<HTML>");
        sb.append(de.decodeString(value));
        sb.append("</HTML>");

        getEditorPane().setText(sb.toString());
        getEditorPane().setBackground((de.getStatus() == Hl7Decoder.Status.ERROR) ? Color.red : defBackColor);
        getEditorPane().setToolTipText(de.getStatusText());
    }

    public void setDelimiters(Delimiters d) {
        delimiters = d;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 50);
    }
}
