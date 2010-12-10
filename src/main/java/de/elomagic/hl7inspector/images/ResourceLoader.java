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
    
    /** Creates a new instance of ResourceLoader */
    private ResourceLoader() {
    }
    
    public static ImageIcon loadImageIcon(String imageName) {
        ImageIcon icon = null;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
            icon = new ImageIcon(loader.getResource(IMAGE_SOURCE_PATH.concat(SMALL_IMAGE).concat(imageName)));
        } catch(Exception ex) {
            Logger.getLogger(ResourceLoader.class.getName()).error(ex.getMessage(), ex);
        }
        
        return icon;
    }
    
    public static ImageIcon loadImageIcon(String imageName, String imageSize) {
        ImageIcon icon = null;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
            icon = new ImageIcon(loader.getResource(IMAGE_SOURCE_PATH.concat(imageSize).concat(imageName)));
        } catch(Exception ex) {
            Logger.getLogger(ResourceLoader.class.getName()).error(ex.getMessage(), ex);
        }
        
        return icon;
    }    
    
    public static BufferedImage loadBufferedImage(String imageName) {
        BufferedImage image = null;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
            image = ImageIO.read(loader.getResource(IMAGE_SOURCE_PATH.concat(imageName)));
        } catch(Exception ex) {
            Logger.getLogger(ResourceLoader.class.getName()).error(ex.getMessage(), ex);
        }
        
        return image;
    }
    
    public final static String IMAGE_SOURCE_PATH = "de/elomagic/hl7inspector/resources/";
    
    public final static String SMALL_IMAGE = "";
    public final static String LARGE_IMAGE = "32x32/";
}
