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
package de.elomagic.hl7inspector.gui.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;

import de.elomagic.hl7inspector.gui.Notification;

/**
 *
 * @author Carsten Rambow
 */
public class VisitWebSiteAction extends AbstractAction {

    public VisitWebSiteAction(final String caption, final String uri) {
        super(caption, null);//icon);

        this.uri = uri;

        putValue(SHORT_DESCRIPTION, caption);
    }
    private final String uri;

    @Override
    public void actionPerformed(final ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(uri));
        } catch(URISyntaxException | IOException ex) {
            Notification.error(ex);
        }
    }
}
