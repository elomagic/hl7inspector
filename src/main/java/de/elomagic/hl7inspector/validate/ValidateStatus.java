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
 *
 */
package de.elomagic.hl7inspector.validate;

import de.elomagic.hl7inspector.images.ResourceLoader;
import javax.swing.ImageIcon;

/**
 *
 * @author rambow
 */
public class ValidateStatus {

    /** Creates a new instance of ValidateStatus */
    public ValidateStatus(int status) {
        this.status = status;
    }

    public ValidateStatus(int status, String text) {
        this.status = status;
        this.text = text;
    }

    public final static int OK = 0;
    public final static int INFO = 1;
    public final static int WARN = 2;
    public final static int ERROR = 3;

    public ImageIcon getIcon() {
        ImageIcon icon;

        switch (status) {
            case OK:
                icon = ResourceLoader.loadImageIcon("ok.png");
                break;
            case INFO:
                icon = ResourceLoader.loadImageIcon("dialog-information.png");
                break;
            case WARN:
                icon = ResourceLoader.loadImageIcon("warning.png");
                break;
            case ERROR:
                icon = ResourceLoader.loadImageIcon("dialog-error.png");
                break;
            default:
                icon = ResourceLoader.loadImageIcon("hole_white.gif");
                break;
        }

        return icon;
    }

    public int compareTo(ValidateStatus vs) {
        return status - vs.status;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        String s;

        switch (status) {
            case OK:
                s = "Ok";
                break;
            case INFO:
                s = "Info";
                break;
            case WARN:
                s = "Warning";
                break;
            case ERROR:
                s = "Error";
                break;
            default:
                s = "Ok";
                break;
        }

        return s;
    }

    private int status = 1;
    private String text = "";
}
