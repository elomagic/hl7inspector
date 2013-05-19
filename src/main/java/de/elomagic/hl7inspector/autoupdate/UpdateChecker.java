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
 */
package de.elomagic.hl7inspector.autoupdate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXB;

import de.elomagic.hl7inspector.Hl7Inspector;
import de.elomagic.hl7inspector.StartupProperties;

/**
 *
 * @author rambow
 */
public class UpdateChecker extends Thread {
    private boolean terminated = false;
    private boolean resultAvailable = false;
    private boolean result = false;
    private UpdateCheckException e = null;

    /**
     * Creates a new instance of UpdateChecker.
     */
    public UpdateChecker() {
        resultAvailable = false;
        terminated = false;
    }

    public void terminate() {
        terminated = true;
    }

    @Override
    public void run() {
        resultAvailable = false;
        terminated = false;
        try {
            try {
                String versionFile = readVersionFile();
                String currentVersion = Hl7Inspector.getVersion();
                result = checkUpdates(versionFile, currentVersion);
                resultAvailable = false;
            } catch(IOException | URISyntaxException ex) {
                e = new UpdateCheckException(ex);
            }
        } finally {
            terminated = true;
        }
    }

    public static boolean checkForUpdates() throws InterruptedException, UpdateCheckException {
        UpdateChecker uc = new UpdateChecker();

        uc.start();

        while(!uc.resultAvailable && !uc.terminated) {
            sleep(100);
        }

        if(uc.e != null) {
            throw uc.e;
        }

        return uc.result;
    }

    /**
     *
     * @param xml
     * @param currentVersion
     * @return true when currentVersion higher then version inside xml
     */
    public boolean checkUpdates(String xml, String currentVersion) {
        VersionBean versionBean = JAXB.unmarshal(new StringReader(xml), VersionBean.class);

        String version = versionBean.getHl7Inspector2().getVersion();
        return currentVersion.compareTo(version) < 0;
    }

    private String readVersionFile() throws IOException, URISyntaxException {
        String versionFile = "";

        StartupProperties p = StartupProperties.getInstance();

        URL url = new URL("http", "www.elomagic.de", "//file_versions.xml");

        HttpURLConnection uc;
        HttpURLConnection.setFollowRedirects(true);

        switch(p.getProxyMode()) {
            case 1: {
                ProxySelector ps = ProxySelector.getDefault();
                List<Proxy> l = ps.select(url.toURI());

                if(!l.isEmpty()) {
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

        uc.setConnectTimeout(10000);
        uc.setReadTimeout(10000);
        try (InputStreamReader in = new InputStreamReader(uc.getInputStream()); BufferedReader bin = new BufferedReader(in)) {
            String s;
            do {
                s = bin.readLine();
                if(s != null) {
                    versionFile = versionFile.concat(s);
                }
            } while(s != null);
        }

        return versionFile;
    }
}
