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
package de.elomagic.hl7inspector.gui;

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.images.ResourceLoader;

import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import javax.swing.JViewport;

/**
 *
 * @author rambow
 */
public class ImageBackground extends JViewport {
    private BufferedImage image = null;

    /**
     * Creates a new instance of ImageBackground.
     */
    public ImageBackground() {
        setBackground(SystemColor.window);

        if(StartupProperties.getInstance().isDesktopImage()) {
            image = ResourceLoader.loadBufferedImage("desktop.bmp");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(image != null) {
            int yl = (getHeight() / image.getHeight()) + 1;
            int xl = (getWidth() / image.getWidth()) + 2;

            for(int i = 0; i < yl; i++) {
                int y = i * image.getHeight();

                int o = 0;

                if((i % 2) == 0) {
                    o = -image.getWidth() / 2;
                }

                for(int q = 0; q < xl; q++) {
                    int x = q * image.getWidth() + o;

                    g.drawImage(image, x, y, null);
                }
            }
        }
    }
}
