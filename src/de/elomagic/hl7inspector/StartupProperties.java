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

import de.elomagic.hl7inspector.gui.SimpleDialog;
import de.elomagic.hl7inspector.profile.ProfileFile;
import de.elomagic.hl7inspector.utils.History;
import java.awt.Color;
import java.awt.SystemColor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;
import javax.swing.UIManager;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class StartupProperties extends Properties {
    
    /** Creates a new instance of StartupProperties */
    private StartupProperties() {
        super();
        
        String wp = getUserHomePath(false);        
        try {
            
            File file = new File(wp.concat(CONFIG_FILE));
            FileInputStream fin = null;
            
            if (!file.exists()) {
                File oldFile = new File(System.getProperty("user.home").concat(File.separator).concat(".hl7inspector-2.0").concat(File.separator).concat(CONFIG_FILE));                
                
                if (oldFile.exists()) {
                    if (SimpleDialog.confirmYesNo("Import configruation from HL7 Inspector 2.0 ?") == SimpleDialog.YES_OPTION) {
                        fin = new FileInputStream(oldFile);                        
                    } else {
                        fin = new FileInputStream(wp.concat(CONFIG_FILE));
                    }
                }
            } else {
                fin = new FileInputStream(wp.concat(CONFIG_FILE));
            }
            
            try {
                loadFromXML(fin);
                //load(fin);
            } finally {
                fin.close();
            }
            
            createProfiles();
            createKeyStores();
        } catch (Exception e) {
            if (!(e instanceof FileNotFoundException)) {
                SimpleDialog.error(e) ;
            }
        }
    }
    
    public void save() {
        try {
            setProfiles();
            setKeyStores();
            
//            History history = new History(StartupProperties.SENDER_OPTIONS_DEST);
//            history.write(this);
            
            String wp = getUserHomePath(true);
            
            FileOutputStream fout = new FileOutputStream(wp.concat(CONFIG_FILE), false);
            try {
                storeToXML(fout, "Inspector properties");
                //store(fout, "Inspector comments");
                fout.flush();
                
            } finally {
                fout.close();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
            SimpleDialog.error(e, e.getMessage());
        }
    }
    
    public final static String getUserHomePath(boolean create) {
        String wp = System.getProperty("user.home").concat(File.separator).concat(".hl7inspector-2.1").concat(File.separator);
        
        if ((!(new File(wp).exists())) && (create)) {
                new File(wp).mkdir();
        }        
        
        return wp;
    }       
    
    public final static StartupProperties getInstance() { return prop; }
    
    public Vector<File> getRecentFiles() {
        Vector<File> v = new Vector<File>();
        int i = 0;
        try {            
            String s = null;
            do {
                i++;
                
                s = getProperty(RECENT_FILE.concat(".").concat(Integer.toString(i)));
                
                if (s != null) {
                    File file = new File(s);
                
                    if (file.exists()) {
                        v.add(file);
                    }
                }
            } while (s!=null);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
        
        return v;
    }
    
    public void setRecentFiles(Vector files) {
        for (int i=1; i<9; i++)
            remove(RECENT_FILE.concat(".").concat(Integer.toString(i)));
        
        for (int i=0; i<files.size(); i++)
            setProperty(RECENT_FILE.concat(".").concat(Integer.toString(i+1)), ((File)files.get(i)).getAbsolutePath());
    }
    public void setPhrases(Vector<String> v) {
        for (int i=0; i<v.size(); i++) {
            setProperty(PHRASE_HISTORY.concat(".".concat(Integer.toString(i+1))), v.get(i));
        }
    }
    
    public Vector<String> getPhrases() {
        Vector<String> v = new Vector<String>();
        
        int i = 0;
        String s = null;
        do {
            i++;
            
            s = getProperty(PHRASE_HISTORY.concat(".".concat(Integer.toString(i))));
            
            if (s != null) {
                v.add(s);
            }
        } while ((s != null) && (i < 10));
        
        return v;
    }
    
    public void putPhrase(String phrase) {
        Vector<String> v = getPhrases();
        
        if (v.indexOf(phrase) != -1) {
            v.remove(phrase);
        }
        
        while (v.size()> 9)
            v.remove(9);
        
        v.insertElementAt(phrase, 0);
        
        setPhrases(v);
    }
    
    
    public Vector<ProfileFile> getProfiles() { return profiles; }
    
    private void setProfiles() {
        int q = 0;
        String s = "";
        do {
            q++;
            s = getProperty(PROFILE_FILE.concat(".").concat(Integer.toString(q+1)), "");
            
            if (s.length() != 0) {
                remove(PROFILE_FILE.concat(".").concat(Integer.toString(q)));
                remove(PROFILE_DESCRIPTION.concat(".").concat(Integer.toString(q)));
            }
        } while (s.length() != 0);
        
        for (int i=0; i<profiles.size(); i++) {
            ProfileFile profile = profiles.get(i);
            setProperty(PROFILE_FILE.concat(".").concat(Integer.toString(i+1)), profile.getAbsolutePath());
            setProperty(PROFILE_DESCRIPTION.concat(".").concat(Integer.toString(i+1)), profile.getDescription());
        }
    }
    
    private void createProfiles() {
        profiles.clear();
        
        int i = 0;
        try {
            String s = null; 
            do {
                i++;
                
                s = getProperty(PROFILE_FILE.concat(".").concat(Integer.toString(i)));
                
                if (s != null) {
                    ProfileFile file = new ProfileFile(s);
                    file.setDescription(getProperty(PROFILE_DESCRIPTION.concat(".").concat(Integer.toString(i))));
                    profiles.add(file);
                }
            } while (s != null);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
    }
    
    public Vector<File> getKeyStores() { return keyStoreFiles; }    

    private void setKeyStores() {
        int q = 0;
        String s = "";
        do {
            q++;
            s = getProperty(KEYSTORE_FILE.concat(".").concat(Integer.toString(q+1)), "");
            
            if (s.length() != 0) {
                remove(KEYSTORE_FILE.concat(".").concat(Integer.toString(q)));
//                remove(PROFILE_DESCRIPTION.concat(".").concat(Integer.toString(q)));
            }
        } while (s.length() != 0);
        
        for (int i=0; i<keyStoreFiles.size(); i++) {
            File keyStoreFile = keyStoreFiles.get(i);
            setProperty(KEYSTORE_FILE.concat(".").concat(Integer.toString(i+1)), keyStoreFile.getAbsolutePath());
//            setProperty(PROFILE_DESCRIPTION.concat(".").concat(Integer.toString(i+1)), profile.getDescription());
        }
    }
    
    private void createKeyStores() {
        keyStoreFiles.clear();
        
        int i = 0;
        try {
            String s = null; 
            do {
                i++;
                
                s = getProperty(KEYSTORE_FILE.concat(".").concat(Integer.toString(i)));
                
                if (s != null) {
                    File file = new File(s);
//                    file.setDescription(getProperty(PROFILE_DESCRIPTION.concat(".").concat(Integer.toString(i))));
                    keyStoreFiles.add(file);
                }
            } while (s != null);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage(), e);
        }
    }    
    
    public Calendar getLastUpdateCheck() throws ParseException { 
        Calendar c = Calendar.getInstance();
        c.setTime(new SimpleDateFormat("yyyy.MM.dd").parse(getProperty(AUTOUPDATE_LAST_CHECK, "1980.01.01"))); 
        return c;
    }    
    
    public void setLastUpdateCheck(Calendar c) { setProperty(AUTOUPDATE_LAST_CHECK, new SimpleDateFormat("yyyy.MM.dd").format(c.getTime())); }
    
    public boolean isAutoUpdateAsk() { return getProperty(AUTOUPDATE_ASK, "t").equals("t"); }
    public void setAutoUpdateAsk(boolean value) { setProperty(AUTOUPDATE_ASK, value?"t":"f"); }

    public int getAutoUpdatePeriod() { return Integer.parseInt(getProperty(AUTOUPDATE_PERIOD, "30")); }
    public void setAutoUpdatePeriod(int period) { setProperty(AUTOUPDATE_PERIOD, Integer.toString(period)); }

    public boolean isDesktopImage() { return getProperty(DESKTOP_IMAGE, "t").equals("t"); }
    public void setDesktopImage(boolean value) { setProperty(DESKTOP_IMAGE, value?"t":"f"); }
    
    public String getLookAndFeel() { return getProperty(APP_LOOK_AND_FEEL, UIManager.getSystemLookAndFeelClassName()); }
    public Class getLookAndFeelClass() throws ClassNotFoundException { return ClassLoader.getSystemClassLoader().loadClass(getLookAndFeel()); }
    public void setLookAndFeel(String laf) { setProperty(APP_LOOK_AND_FEEL, laf); }
    
    public boolean isOneInstance() { return getProperty(APP_ONE_INSTANCE, "f").equals("t"); }
    public void setOneInstance(boolean value) { setProperty(APP_ONE_INSTANCE, value?"t":"f"); }
    
    public File getExternalFileViewer() { return getProperty(EXTERNAL_FILE_VIEWER, "").equals("")?null:new File(getProperty(EXTERNAL_FILE_VIEWER, "")); }
    public void setExternalFileViewer(File value) { setProperty(EXTERNAL_FILE_VIEWER, value==null?"":value.getAbsolutePath()); }
        
    public File getExternalHexViewer() { return getProperty(EXTERNAL_HEX_VIEWER, "").equals("")?null:new File(getProperty(EXTERNAL_HEX_VIEWER, "")); }
    public void setExternalHexViewer(File value) { setProperty(EXTERNAL_HEX_VIEWER, value==null?"":value.getAbsolutePath()); }
    
    public File getLastSaveFolder() { return getProperty(LAST_SAVE_FOLDER, "").equals("")?new File(""):new File(getProperty(LAST_SAVE_FOLDER, "")); }
    public void setLastSaveFolder(File value) { setProperty(LAST_SAVE_FOLDER, value==null?"":value.getAbsolutePath()); }
    
    public int getProxyMode() { return Integer.parseInt("0" + getProperty(NETWORK_PROXY_MODE, "1")); }
    public void setProxyMode(int mode) { setProperty(NETWORK_PROXY_MODE, Integer.toString(mode)); }

    public String getProxyHost() { return getProperty(NETWORK_PROXY_HOST, ""); }
    public void setProxyHost(String host) { setProperty(NETWORK_PROXY_HOST, host); }
    
    public int getProxyPort() { return Integer.parseInt("0" + getProperty(NETWORK_PROXY_PORT, "0")); }
    public void setProxyPort(int port) { setProperty(NETWORK_PROXY_PORT, Integer.toString(port)); }    
    
    public int getTreeNodeLength() { return Integer.parseInt(getProperty(TREE_NODE_LENGTH, "128")); }
    public void setTreeNodeLength(int nodeLen) { setProperty(TREE_NODE_LENGTH, Integer.toString(nodeLen)); }
    
    public int getTreeViewMode() { return Integer.parseInt(getProperty(TREE_VIEW_MODE, "0")); }
    public void setTreeViewMode(int mode) { setProperty(TREE_VIEW_MODE, Integer.toString(mode)); }
    
    public String getTreeFontName() { return getProperty(TREE_FONT_NAME, "Arial"); }
    public void setTreeFontName(String name) { setProperty(TREE_FONT_NAME, name); }    
    
    public boolean isDebugFileOutput() { return getProperty(APP_DEBUG_FILE, "f").equals("t"); }
    public void setDebuFileOutput(boolean value) { setProperty(APP_DEBUG_FILE, value?"t":"f"); }
    
    public boolean isDetailsWindowVisible() { return "t".equals(getProperty(DESKTOP_DETAILS_VISIBLE)); }
    public void setDetailsWindowVisible(boolean value) { setProperty(DESKTOP_DETAILS_VISIBLE, value?"t":"f"); }
    
    public Color getColor(String COLOR_LABEL) {
        String p = getProperty(COLOR_LABEL);
        Color c = null;
        
        try {
            c = (p == null)?null:new Color(Integer.parseInt(p, 16));            
        } catch (Exception e) {
            c = null;
        }
        
        if (c == null) {
            if (COLOR_LABEL.equals(COLOR_NODE_PREFIX)) {
                c = SystemColor.textInactiveText;
            } else if (COLOR_LABEL.equals(COLOR_NODE_TEXT)) {
                c = SystemColor.black;
            } else if (COLOR_LABEL.equals(COLOR_NODE_DESCRIPTION)) {
                c = SystemColor.textInactiveText;
            } else if (COLOR_LABEL.equals(COLOR_NODE_TRUNCATE)) {
                c = SystemColor.magenta;
            } else {
                c = null;
            }
        }
        return new Color(c.getRGB()&0xffffff);
    }
    
    public void setColor(String COLOR_LABEL, Color c) {
        setProperty(COLOR_LABEL, (c==null)?null:Integer.toHexString(c.getRGB()));
    }    
        
    private Vector<ProfileFile> profiles = new Vector<ProfileFile>();
    private Vector<File> keyStoreFiles = new Vector<File>();
    
    public final static String APP_ONE_INSTANCE         = "application-one-instance";
    public final static String APP_LOOK_AND_FEEL        = "application-look-and-feel";
    public final static String APP_DEBUG_FILE           = "application-debug-file";
    
    public final static String DEFAULT_PROFILE          = "profile-default";
    public final static String DEFAULT_PRIVATE_KEYSTORE = "security-default-private-keystore";
    public final static String DEFAULT_PUBLIC_KEYSTORE  = "security-default-public-keystore";
    
    public final static String DESKTOP_X                = "desktop.x";
    public final static String DESKTOP_Y                = "desktop.y";
    public final static String DESKTOP_W                = "desktop.w";
    public final static String DESKTOP_H                = "desktop.h";
    public final static String DESKTOP_IMAGE            = "desktop-image";
    public final static String DESKTOP_DETAILS_VISIBLE  = "desktop-details-visible";
    public final static String COLOR_NODE_PREFIX        = "tree-node-prefix-color";
    public final static String COLOR_NODE_TEXT          = "tree-node-text-color";
    public final static String COLOR_NODE_DESCRIPTION   = "tree-node-description-color";
    public final static String COLOR_NODE_TRUNCATE      = "tree-node-truncate-color";
    
    public final static String TREE_NODE_LENGTH         = "tree-node-length";
    public final static String TREE_VIEW_MODE           = "tree-view-mode";
    public final static String TREE_FONT_NAME           = "tree-font-name";
    
    public final static String EXTERNAL_FILE_VIEWER     = "external-file-viewer";
    public final static String EXTERNAL_HEX_VIEWER      = "external-hex-viewer";
    
    public final static String SENDER_OPTIONS_DEST      = "sender.options.destination";
    
    public final static String RECENT_FILE              = "recent-file";
    
    public final static String PHRASE_HISTORY           = "phrase-history";
    
    public final static String PROFILE_FILE             = "profile-file";
    public final static String PROFILE_DESCRIPTION      = "profile-description";
    
    public final static String KEYSTORE_FILE             = "keystore-file";
//    public final static String PROFILE_DESCRIPTION      = "profile-description";    
    
    public final static String DEFAULT_FRAME_START      = "default-frame-start";
    public final static String DEFAULT_FRAME_STOP1      = "default-frame-stop1";
    public final static String DEFAULT_FRAME_STOP2      = "default-frame-stop2";
    
    public final static String LAST_SAVE_FOLDER         = "last-save-folder";
    
    public final static String AUTOUPDATE_PERIOD        = "autoupdate-period";
    public final static String AUTOUPDATE_ASK           = "autoupdate-ask";
    public final static String AUTOUPDATE_LAST_CHECK    = "autoupdate-last-check";
    
    public final static String NETWORK_PROXY_MODE       = "network-proxy-mode";
    public final static String NETWORK_PROXY_HOST       = "network-proxy-host";
    public final static String NETWORK_PROXY_PORT       = "network-proxy-port";
                
    private final static StartupProperties prop = new StartupProperties();
    
    private String CONFIG_FILE = "hl7inspector.properties";
    
}
