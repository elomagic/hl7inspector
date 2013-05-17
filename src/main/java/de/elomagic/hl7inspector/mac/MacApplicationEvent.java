/*
 * Copyright 2010 Carsten Rambow
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
package de.elomagic.hl7inspector.mac;

import java.util.EventObject;

/**
 * The class of events sent to ApplicationListener call backs. Since these events are initiated by Apple events they provide additional functionality over the EventObjects that they inherit their
 * basic
 * characteristics from. For those events where it is appropriate, they store the file name of the item that the event corresponds to. They are also unique in that they can be flagged as, and tested
 * for, having been handled.
 *
 * @author carsten.rambow
 */
public class MacApplicationEvent extends EventObject {
    private boolean handled = false;
    private String filename;

    MacApplicationEvent(Object source) {
        super(source);
    }

    MacApplicationEvent(Object source, String filename) {
        super(source);

        this.filename = filename;
    }

    /**
     * Determines whether an ApplicationListener has acted on a particular
     * event. An event is marked as having been handled with
     * <code>setHandled(true)</code>.
     *
     * @return <code>true</code> if the event has been handled,
     * otherwise <code>false</code>
     */
    public boolean isHandled() {
        return handled;
    }

    /**
     * Sets the state of the event. After this method handles an
     * ApplicationEvent, it may be useful to specify that it has been handled.
     * This is usually used in conjunction with
     * <code>getHandled()</code>. Set
     * to true to designate that this event has been handled. By default it is
     * <code>false</code>.
     *
     * @param <code>handled - true</code> if the event has been handled,
     * otherwise <code>false</code>.
     */
    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    /**
     * Provides the filename associated with a particular AppleEvent. When the ApplicationEvent corresponds to an Apple Event that needs to act on a particular file, the ApplicationEvent carries the
     * name of the specific file with it. For example, the Print and Open events refer to specific files. For these cases, this returns the appropriate file name.
     *
     * @return the full path to the file associated with the event, if applicable, otherwise null
     */
    public String getFilename() {
        return filename;
    }
}
