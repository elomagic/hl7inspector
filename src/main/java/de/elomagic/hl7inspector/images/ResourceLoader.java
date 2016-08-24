/*
 * Copyright 2016 Carsten Rambow
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

import javafx.scene.image.Image;

import org.apache.log4j.Logger;

/**
 *
 * @author Carsten Rambow
 */
public class ResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(ResourceLoader.class);
    private static final String IMAGE_SOURCE_PATH = "de/elomagic/hl7inspector/themes/";
    private static final String SMALL_IMAGE = "";
    private static final String THEME = "classic/";

    public static final String LARGE_IMAGE = "large/";

    public static ImageIcon loadImageIcon(final String imageName) {
        return loadImageIcon(imageName, SMALL_IMAGE);
    }

    public static ImageIcon loadImageIcon(final String imageName, final String imageSize) {
        ImageIcon icon = null;

        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            icon = new ImageIcon(loader.getResource(IMAGE_SOURCE_PATH + THEME + imageSize + imageName));
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return icon;
    }

    public static BufferedImage loadBufferedImage(final String imageName) {
        BufferedImage image = null;

        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            image = ImageIO.read(loader.getResource(IMAGE_SOURCE_PATH + THEME + imageName));
        } catch(Exception ex) {
            LOGGER.error("Resource " + imageName + ": " + ex.getMessage(), ex);
        }

        return image;
    }

    public static Image loadImage(final String imageName) {
        Image image = null;

        ClassLoader loader = ClassLoader.getSystemClassLoader();

        try {
            image = new Image(loader.getResourceAsStream(IMAGE_SOURCE_PATH + THEME + SMALL_IMAGE + imageName));
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return image;
    }
}
