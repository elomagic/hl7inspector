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

/**
 *
 * @author carstenrambow
 */
public interface MacApplicationListener {
    void handleAbout(MacApplicationEvent ae);

    void handleOpenApplication(MacApplicationEvent ae);

    void handleOpenFile(MacApplicationEvent ae);

    void handlePreferences(MacApplicationEvent ae);

    void handlePrintFile(MacApplicationEvent ae);

    void handleQuit(MacApplicationEvent ae);

    void handleReOpenApplication(MacApplicationEvent ae);
}
