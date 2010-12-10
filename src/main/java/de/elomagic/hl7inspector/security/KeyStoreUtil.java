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
package de.elomagic.hl7inspector.security;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 *
 * @author rambow
 */
public class KeyStoreUtil {

    /** Creates a new instance of KeyStoreUtil */
//    public KeyStoreUtil() {
//    }
    public static KeyStore loadPublicKeyStore(File file) throws Exception {
        String publicPath = file.getPath() + "/trusted/publicstore";
        KeyStore result = KeyStore.getInstance("JKS");
        FileInputStream fin = new FileInputStream(publicPath);
        try {
            result.load(fin, "elomagic".toCharArray());
        } finally {
            fin.close();
        }

        return result;
    }

    public static KeyStore loadPrivateKeyStore(File file, String password) throws Exception {
        String publicPath = file.getPath();
        KeyStore result = KeyStore.getInstance("JKS");
        FileInputStream fin = new FileInputStream(file);
        try {
            result.load(fin, password.toCharArray());
        } finally {
            fin.close();
        }

        return result;
    }

}
