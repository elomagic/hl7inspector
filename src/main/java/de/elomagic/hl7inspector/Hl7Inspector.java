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
package de.elomagic.hl7inspector;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.jgoodies.looks.LookUtils;
import com.sun.javafx.application.PlatformImpl;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.SimpleLayout;

import de.elomagic.hl7inspector.gui.Desktop;
import de.elomagic.hl7inspector.gui.Notification;
import de.elomagic.hl7inspector.gui.UpdateCheckDialog;
import de.elomagic.hl7inspector.gui.actions.FileRecentOpenAction;
import de.elomagic.hl7inspector.instance.InstanceManager;
import de.elomagic.hl7inspector.mac.MacApplication;

/**
 *
 * @author rambow
 */
public class Hl7Inspector {

    public static final String APPLICATION_NAME = "HL7 Inspector";
    public static final String VERSION_LABEL = "";

    /**
     * Creates a new instance of Main.
     */
    private Hl7Inspector() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        new Hl7Inspector().start(args);
    }

    private void start(final String[] args) {

        // Just start only a JavaFX environmental. No need to do anything here
        PlatformImpl.startup(()->{
            PlatformImpl.addListener(new PlatformImpl.FinishListener() {
                @Override
                public void idle(boolean implicitExit) {
                    // Do nothing
                }

                @Override
                public void exitCalled() {
                    // Do nothing
                }
            });

        });

        MacApplication.setScreenMenuBar(Hl7Inspector.APPLICATION_NAME, true);
        Logger.getRootLogger().addAppender(new ConsoleAppender(new SimpleLayout(), ConsoleAppender.SYSTEM_OUT));

        if(StartupProperties.getInstance().isDebugFileOutput()) {
            try {
                RollingFileAppender app = new RollingFileAppender(new SimpleLayout(), StartupProperties.getUserHomePath(true).concat("hl7inspector2.0-debug.log"), true);
                app.setEncoding("UTF-8");
                app.setMaxBackupIndex(1);
                app.setMaxFileSize("512KB");

                Logger.getRootLogger().addAppender(app);
            } catch(Exception e) {
                Logger.getLogger(Hl7Inspector.class).fatal(e.getMessage(), e);
            }
        }

        try {
            if(StartupProperties.getInstance().isOneInstance()) {
                if(InstanceManager.lookupInstance(args)) {
                    System.exit(0);
                } else {
                    InstanceManager.startOneInstance();
                }
            }

            UIManager.installLookAndFeel("JGoodies Plastic", "com.jgoodies.looks.plastic.PlasticLookAndFeel");
            UIManager.installLookAndFeel("JGoodies Plastic3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
            UIManager.installLookAndFeel("JGoodies PlasticXP", "com.jgoodies.looks.plastic.PlasticXPLookAndFeel");

            if(LookUtils.IS_OS_WINDOWS_MODERN) {
                UIManager.installLookAndFeel("JGoodies Windows", "com.jgoodies.looks.windows.WindowsLookAndFeel");
            }

            JFrame.setDefaultLookAndFeelDecorated(true);

            Desktop.getInstance().clearMessages();
            Desktop.getInstance().getMainFrame().setVisible(true);

            UpdateCheckDialog.check(true);

            if(args.length != 0) {
                File file = new File(args[0]);

                if(file.exists()) {
                    new FileRecentOpenAction(file).actionPerformed(null);
                }
            }
        } catch(Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            Notification.error(e, e.getMessage());
        }
    }

    public static String getVersion() {
        String version;

        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            try (InputStream in = loader.getResourceAsStream("application.properties")) {
                Properties props = new Properties();
                props.load(in);
                version = props.getProperty("application.version", "?.?.?.?");
            }
        } catch(Exception e) {
            version = "?.?.?.?";
        }

        return version;
    }

    public static String getVersionString() {
        return getVersion().concat(" ").concat(VERSION_LABEL);
    }
}
