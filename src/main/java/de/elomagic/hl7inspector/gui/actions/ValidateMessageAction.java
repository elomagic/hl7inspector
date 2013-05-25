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
package de.elomagic.hl7inspector.gui.actions;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.DesktopIntf;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.profile.ProfileIO;
import de.elomagic.hl7inspector.validate.Validator;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ValidateMessageAction extends BasicAction {
    private static final long serialVersionUID = -1669814127244261309L;

    /**
     * Creates a new instance of ValidateMessageAction.
     */
    public ValidateMessageAction() {
        super();

        putValue(NAME, "Validate message(s)...");
        putValue(SMALL_ICON, ResourceLoader.loadImageIcon("spellcheck.png"));
        putValue(SHORT_DESCRIPTION, "Validate seleted message(s)");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_X));
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        DesktopIntf d = Desktop.getInstance();
        d.setLockCounter(true);
        try {
            List<Message> messages = Desktop.getInstance().getMessages();

            for(final Message msg : messages) {
                try {
                    Validator val = new Validator(ProfileIO.getDefault());
                    val.validate(msg);
                } catch(Exception ex) {
                    Logger.getLogger(getClass()).error(ex.getMessage(), ex);
                }
            }


            //d.getTree().updateUI();
            // TODO May be we need another method for this
            d.refreshHighlightPhrases();
        } finally {
            d.setLockCounter(false);
        }
    }
}
