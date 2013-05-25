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
 */
package de.elomagic.hl7inspector.io.receive;

import de.elomagic.hl7inspector.io.IOThreadListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MessageReceiveController {
    private final static MessageReceiveController CONTROLLER = new MessageReceiveController();
    private ReceiveThread thread;
    private List<IOThreadListener> listener = new ArrayList<>();

    public static void start() {
    }

    /**
     * Stops receive thread (Synchron mode).
     *
     * @throws InterruptedException
     */
    public static void stop() throws InterruptedException {
        CONTROLLER.stopImpl();
    }

    private void stopImpl() throws InterruptedException {
        if(thread == null) {
            return;
        }

        thread.terminateRequest();
        thread.wait(2000);
    }

    public static void addListener(IOThreadListener value) {
        CONTROLLER.listener.add(value);
    }

    public static void removeListener(IOThreadListener value) {
        CONTROLLER.listener.remove(value);
    }
}
