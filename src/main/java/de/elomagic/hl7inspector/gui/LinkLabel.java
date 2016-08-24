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
package de.elomagic.hl7inspector.gui;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

/**
 *
 * @author Carsten Rambow
 */
public class LinkLabel extends JLabel implements MouseListener {

    private URI uri;

    public LinkLabel(final String text, final String uri) throws URISyntaxException {
        super();
        init(text, uri);
    }

    private void init(final String text, final String uri) throws URISyntaxException {
        String t = text;
        if(!t.contains("<html>")) {
            t = "<html><u><font color=blue>" + t + "</font></u></html>";
        }

        setText(t);

        this.uri = new URI(uri);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(uri);

        addMouseListener(this);

    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        try {
            if(uri.toString().indexOf('@') == -1) {
                Desktop.getDesktop().browse(uri);
            } else {
                Desktop.getDesktop().mail(uri);
            }
        } catch(Exception ex) {
            Notification.error(ex);
        }
    }

    @Override
    public void mousePressed(final MouseEvent event) {
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
    }

    @Override
    public void mouseEntered(final MouseEvent event) {
    }

    @Override
    public void mouseExited(final MouseEvent event) {
    }
}
