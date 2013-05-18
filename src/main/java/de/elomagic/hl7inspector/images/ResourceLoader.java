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
package de.elomagic.hl7inspector.images;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class ResourceLoader {
    private static Logger log = Logger.getLogger(ResourceLoader.class);
    private final static String IMAGE_SOURCE_PATH = "de/elomagic/hl7inspector/themes/";
    private final static String SMALL_IMAGE = "";
    private final static String THEME = "classic/";
    public final static String LARGE_IMAGE = "large/";

    public static ImageIcon loadImageIcon(String imageName) {
        return loadImageIcon(imageName, SMALL_IMAGE);
    }

    public static ImageIcon loadImageIcon(String imageName, String imageSize) {
        ImageIcon icon = null;

        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            icon = new ImageIcon(loader.getResource(IMAGE_SOURCE_PATH + THEME + imageSize + imageName));
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return icon;
    }

    public static BufferedImage loadBufferedImage(String imageName) {
        BufferedImage image = null;

        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            image = ImageIO.read(loader.getResource(IMAGE_SOURCE_PATH + THEME + imageName));
        } catch(Exception ex) {
            log.error("Resource " + imageName + ": " + ex.getMessage(), ex);
        }

        return image;
    }
}
