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
package de.elomagic.hl7inspector.io;

/**
 *
 * @author rambow
 */
public class SendOptionsBean {
    private Frame frame = new Frame();
    private String host = "localhost";
    private int port = 2100;
    private String encoding = "ISO-8859-1";
    private boolean reuseSocket = true;
    private boolean encryption = false;
    private boolean authentication = false;

    /** Creates a new instance of SendOptionsBean */
    public SendOptionsBean() {
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public boolean isReuseSocket() {
        return reuseSocket;
    }

    public void setReuseSocket(boolean reuseSocket) {
        this.reuseSocket = reuseSocket;
    }

    public boolean isEncryption() {
        return encryption;
    }

    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }
}
