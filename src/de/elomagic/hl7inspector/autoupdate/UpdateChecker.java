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

package de.elomagic.hl7inspector.autoupdate;

import de.elomagic.hl7inspector.StartupProperties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import nanoxml.XMLElement;

/**
 *
 * @author rambow
 */
public class UpdateChecker extends Thread {
    
    /** Creates a new instance of UpdateChecker */
    public UpdateChecker() {
        resultAvailable = false;
        terminated = false;
    }
    
    public void terminate() {
        terminated = true;
        
    }    
    
    private boolean terminated = false;
    
    @Override
    public void run() {
        resultAvailable = false;
        terminated = false;
        try {
            try {
                result = checkUpdates();                
                resultAvailable = false;
            } catch (Exception ex) {
                e = ex;
            }            
        } finally {
            terminated = true;
        }        
    }
    
    private boolean resultAvailable = false;
    private boolean result = false;
    private Exception e = null;
    
    public final static boolean checkForUpdates() throws Exception /*IOException, MalformedURLException */ {
        UpdateChecker uc = new UpdateChecker();
        
        uc.start();
        
        while ((!uc.resultAvailable) && (!uc.terminated)) {
            sleep(100);            
        }
        
        if (uc.e != null) {
            throw uc.e;
        }
        
        return uc.result;
    }
    
    private boolean checkUpdates() throws Exception /*IOException, MalformedURLException */ {
        UpdateChecker uc = new UpdateChecker();
        
        String f = uc.getVersionFile();
        
        XMLElement xml = new XMLElement();        
        xml.parseString(f);
        
        String version = "";
        
        Enumeration enu = xml.enumerateChildren();
        while (enu.hasMoreElements()) {
            XMLElement child = (XMLElement)enu.nextElement();
            if (child.getName().equalsIgnoreCase("hl7_inspector2")) {
                Enumeration enu2 = child.enumerateChildren();
                while (enu2.hasMoreElements()) {
                    XMLElement child2 = (XMLElement)enu2.nextElement();
                    if (child2.getName().equalsIgnoreCase("version")) {
                        version = child2.getContent();
                    }            
                }
            }            
        }
        
        return de.elomagic.hl7inspector.Hl7Inspector.getVersion().compareTo(version) < 0;
    }
    
    public String getVersionFile() throws Exception /* IOException, MalformedURLException */ {
        String result = "";
        
        StartupProperties p = StartupProperties.getInstance();
        
        URL url = new URL("http", "www.elomagic.de", "//file_versions.xml");      
        
        HttpURLConnection uc = null;
        
        switch (p.getProxyMode()) {
            case 1: { 
                ProxySelector ps = ProxySelector.getDefault();
                List<Proxy> l = ps.select(url.toURI());                
                
                if (l.size() != 0) {
                    Proxy proxy = l.get(0);
                    uc = (HttpURLConnection)url.openConnection(proxy); 
                } else {
                    uc = (HttpURLConnection)url.openConnection(); 
                }
                
                break; 
            }
            case 2: { 
                InetSocketAddress isa = new InetSocketAddress(p.getProxyHost(), p.getProxyPort());
                uc = (HttpURLConnection)url.openConnection(new Proxy(Proxy.Type.HTTP, isa)); 
                break; 
            }
            default: { 
                uc = (HttpURLConnection)url.openConnection(); 
                break; 
            }
        }
        
        uc.setFollowRedirects(true);       
        uc.setConnectTimeout(10000);
        uc.setReadTimeout(10000);
        
        InputStreamReader in = new InputStreamReader(uc.getInputStream());
        try {
            BufferedReader bin = new BufferedReader(in);
            try{
                String s;
                do {
                    s = bin.readLine();
                    if (s != null) {
                        result = result.concat(s);
                    }
                } while (s != null);                                
            } finally {
                bin.close();
            } 
        } finally {
            in.close();
        }
        
        return result;        
    }    
}
