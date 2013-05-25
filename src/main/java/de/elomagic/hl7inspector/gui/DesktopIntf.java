/*
 * Copyright 2013 Carsten Rambow
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

import de.elomagic.hl7inspector.hl7.model.Hl7Object;
import de.elomagic.hl7inspector.hl7.model.Message;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.ProfileFile;

import java.awt.Frame;
import java.util.List;

/**
 *
 * @author rambow
 */
public interface DesktopIntf {
    /**
     * Returns main window.
     *
     * @return The window
     */
    Frame getMainFrame();

    Profile setProfileFile(final ProfileFile file);

    /**
     * Clears message tree.
     */
    void clearMessages();

    /**
     * Adds message to the view.
     *
     * @param messages List if messages
     * @param maxMessageInView Maximum message in the view
     * @param readBottom When true message will be removed from the top
     */
    void addMessages(List<Message> messages, int maxMessageInView, boolean readBottom);

    /**
     * Returns a list of selected messages.
     *
     * @return List of messages
     */
    List<Message> getSelectedMessages();

    /**
     * Returns all HL7 messages frpm the view.
     *
     * @return List of messages
     */
    List<Message> getMessages();

    /**
     * Removes messages from the view.
     *
     * @param messages List of HL7 messages
     */
    void removeMessages(List<Message> messages);

    /**
     * Append hl7 object child to the given parent object.
     * <p/>
     * In case of a {@link Message} then use method {@link #addMessages(java.util.List, int, boolean) } instead
     *
     * @param parent HL7 parent object
     * @return The new child or null if an error occured
     */
    Hl7Object appendHl7Object(Hl7Object parent);

    void removeHL7Object(Hl7Object o);

    void setHL7ObjectValue(Hl7Object o, String value);
    /**
     * Returns a list of selected HL7 Object nodes of the tree.
     *
     * @return List of Hl7Objects
     */
    List<Hl7Object> getSelectedObjects();

    /**
     * Returns the trace window interface.
     *
     * @return The interface
     */
    TraceWindowIntf getInputTraceWindow();

    boolean isCompressedView();

    void setCompressedView(boolean value);

    boolean isReceiveWindowVisible();

    /**
     * Shows the receive window.
     */
    void setReceiveWindowVisible(boolean visible);

    boolean isSendWindowVisible();

    /**
     * Shows the send window.
     */
    void setSendWindowVisible(boolean visible);

    boolean isInputTraceWindowVisible();

    /**
     * Shows the trace window.
     */
    void setInputTraceWindowVisible(boolean visible);

    void setNodeDescriptionWindowVisible(boolean visible);

    boolean isNodeDetailsWindowVisible();

    void setNodeDetailsWindowVisible(boolean visible);

    boolean isNodeDescriptionVisible();

    void setNodeDescriptionVisible(boolean visible);

    void refreshHighlightPhrases();

    void findNextPhrase(String phrase, Hl7Object startFrom, boolean caseSensitive);

    void setLockCounter(boolean increase);
}
