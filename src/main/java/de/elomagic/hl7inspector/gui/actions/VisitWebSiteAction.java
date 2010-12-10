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
package de.elomagic.hl7inspector.gui.actions;

import de.elomagic.hl7inspector.gui.SimpleDialog;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;
import javax.swing.AbstractAction;

/**
 *
 * @author rambow
 */
public class VisitWebSiteAction extends AbstractAction {

    public VisitWebSiteAction(String caption, String uri) {
        super(caption, null);//icon);

        this.uri = uri;

        putValue(SHORT_DESCRIPTION, caption);
    //putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_L));
    }
    private String uri;

    public void actionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(uri));
        } catch (Exception ex) {
            SimpleDialog.error(ex);
        }
    }
}
