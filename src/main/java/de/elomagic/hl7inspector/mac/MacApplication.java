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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author carstenrambow
 */
public class MacApplication {

    private Object app;
    private Class appc;
    private final static MacApplication instance = new MacApplication();

    public static MacApplication getApplication() {
        return instance;
    }

    private void createInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (isMacOS()) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");

            appc = Class.forName("com.apple.eawt.Application");
            app = appc.newInstance();


            Class lc = Class.forName("com.apple.eawt.ApplicationListener");
            Object listener = Proxy.newProxyInstance(lc.getClassLoader(), new Class[]{lc}, new InvocationHandler() {

                @Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                    Object event = args[0];
                    MacApplicationEvent macEvent = wrapEvent(event);
                    boolean handled = false;

                    if (method.getName().equals("handleQuit")) {
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handleQuit(macEvent);
                            handled = handled || macEvent.isHandled();
                        }
                    }

                    if (method.getName().equals("handlePreferences")) {
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handlePreferences(macEvent);
                            handled = handled || macEvent.isHandled();
                        }
                    }

                    if (method.getName().equals("handleAbout")) {
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handleAbout(macEvent);
                            handled = handled || macEvent.isHandled();
                        }
                    }

                    if (method.getName().equals("handleOpenApplication")) {
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handleOpenApplication(macEvent);
                            handled = handled || macEvent.isHandled();
                        }
                    }

                    if (method.getName().equals("handleOpenFile")) {
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handleOpenFile(macEvent);
                            handled = handled || macEvent.isHandled();
                        }
                    }

                    if (method.getName().equals("handlePrintFile")) {
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handlePrintFile(macEvent);
                            handled = handled || macEvent.isHandled();
                        }
                    }

                    if (method.getName().equals("handleReOpenApplication")) {
                        for (int i = 0; i < listeners.size(); i++) {
                            listeners.get(i).handleReOpenApplication(macEvent);
                            handled = handled || macEvent.isHandled();
                        }
                    }

                    try {
                        Method ms = event.getClass().getMethod("setHandled", boolean.class);
                        ms.invoke(event, handled);
                    } catch (Exception ex) {
                        log.fatal(ex.getMessage(), ex);
                    }

                    return null;
                }

            });

            try {
                Method m = appc.getMethod("addApplicationListener", lc);
                m.invoke(app, listener);
            } catch (Exception ex) {
                log.fatal(ex.getMessage(), ex);
            }
        }
    }

    private MacApplicationEvent wrapEvent(Object event) {
        Object source;
        String filename;
        try {
            Method ms = event.getClass().getMethod("getSource");
            source = ms.invoke(event);

            Method mf = event.getClass().getMethod("getFilename");
            Object mfr = mf.invoke(event);
            filename = mfr != null ? mfr.toString() : null;
        } catch (Exception ex) {
            log.fatal(ex.getMessage(), ex);
            source = null;
            filename = null;
        }

        MacApplicationEvent macEvent = new MacApplicationEvent(source, filename);
        return macEvent;
    }

    private MacApplication() {
        try {
            createInstance();
        } catch (Exception ex) {
            log.warn(ex.getMessage(), ex);
        }
    }

    private static List<MacApplicationListener> listeners = new ArrayList<MacApplicationListener>();

    public void addApplicationListener(MacApplicationListener listener) {
        listeners.add(listener);
    }

    public void removeApplicationListener(MacApplicationListener listener) {
        listeners.remove(listener);
    }

    public void setEnabledAboutMenu(boolean value) {
        if (isMacOS()) {
            try {
                Method m = appc.getMethod("setEnabledAboutMenu", boolean.class);
                m.invoke(app, Boolean.valueOf(value));
            } catch (Exception ex) {
                log.fatal(ex.getMessage(), ex);
            }
        }
    }

    public void setEnabledPreferencesMenu(boolean value) {
        if (isMacOS()) {
            try {
                Method m = appc.getMethod("setEnabledPreferencesMenu", boolean.class);
                m.invoke(app, Boolean.valueOf(value));
            } catch (Exception ex) {
                log.fatal(ex.getMessage(), ex);
            }
        }
    }

    public void setTitle(String title) {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", title);
    }

    public static boolean isMacOS() {
        String os = System.getProperty("os.name").toLowerCase();

        return os.toLowerCase().startsWith("mac");
    }

    private Logger log = Logger.getLogger(this.getClass());
}
