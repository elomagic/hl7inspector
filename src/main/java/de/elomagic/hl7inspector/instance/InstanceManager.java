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
package de.elomagic.hl7inspector.instance;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.actions.FileRecentOpenAction;

/**
 *
 * @author rambow
 */
public class InstanceManager extends Thread {
    private final static InstanceManager instance = new InstanceManager();
    private final static String APP_GUID = "1ddf4bc0-4977-11da-8cd6-0800200c9a66";
    private final static int APP_INSTANCE_PORT = 49153;
    private boolean terminating = false;

    /** Creates a new instance of InstanceManager */
    private InstanceManager() {
    }

    public static void startOneInstance() {
        instance.start();
    }

    public static boolean lookupInstance(String[] args) {
        boolean result = false;
        try {
            try {
                try (Socket s = new Socket("localhost", APP_INSTANCE_PORT)) {
                    s.setSoTimeout(1000);
                    try (OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
                            InputStreamReader in = new InputStreamReader(s.getInputStream());
                            BufferedReader bin = new BufferedReader(in)) {
                        out.write(APP_GUID.concat("\n"));
                        out.flush();

                        String guid = bin.readLine();

                        if(APP_GUID.equals(guid)) {
                            out.write(args.length != 0 ? 1 : 0);

                            if(args.length != 0) {
                                out.write(args[0].concat("\n"));
                            }

                            out.flush();

                            System.exit(0);
                        }
                    }
                }
            } catch(ConnectException e) {
                //
            }
        } catch(Exception e) {
            Logger.getLogger(instance.getClass()).error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(APP_INSTANCE_PORT);

            while(!terminating) {
                try {
                    try (Socket s = ss.accept()) {
                        s.setSoTimeout(1000);
                        try (OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
                                InputStreamReader in = new InputStreamReader(s.getInputStream());
                                BufferedReader bin = new BufferedReader(in)) {
                            String guid = bin.readLine();

                            if(APP_GUID.equals(guid)) {
                                out.write(APP_GUID.concat("\n"));
                                out.flush();

                                //Toolkit.getDefaultToolkit().
                                Desktop.getInstance().toFront();
                                /* int paramtCount = */
                                bin.read();

                                String fn = bin.readLine();

                                File file = new File(fn);
                                if(file.exists()) {
                                    new FileRecentOpenAction(file).actionPerformed(null);
                                }
                            }
                        }
                    }
                } catch(Exception e) {
                    Logger.getLogger(getClass()).error(e.getMessage(), e);
                }
            }
        } catch(Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
    }

    public void terminate() {
        terminating = true;
    }
}
