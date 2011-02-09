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
package de.elomagic.hl7inspector.gui;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JLabel;

/**
 *
 * @author carsten.rambow
 */
public class LinkLabel extends JLabel implements MouseListener {

    public LinkLabel(String text, String uri) throws URISyntaxException {
        super();
        init(text, uri);
    }

    private void init(String text, String uri) throws URISyntaxException {
        if (text.indexOf("<html>") == -1) {
            text = "<html><u><font color=blue>" + text + "</font></u></html>";
        }

        setText(text);

        this.uri = new URI(uri);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(uri);

        addMouseListener(this);

    }

    private URI uri;
    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            if (uri.toString().indexOf('@') == -1) {
                java.awt.Desktop.getDesktop().browse(uri);
            } else {
                java.awt.Desktop.getDesktop().mail(uri);
            }
        } catch (Exception ex) {
            SimpleDialog.error(ex);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
