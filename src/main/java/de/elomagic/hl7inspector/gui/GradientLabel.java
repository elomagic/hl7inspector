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
package de.elomagic.hl7inspector.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;

/**
 *
 * @author rambow
 */
public class GradientLabel extends JLabel {

    /** Creates a new instance of GradientLabel */
    public GradientLabel(String text) {
        super(text);
        init();
    }

    private void init() {
        color1 = UIManager.getColor("inactiveCaptionBorder");
        color2 = UIManager.getColor("activeCaptionBorder");
        setForeground(UIManager.getColor("activeCaptionText"));

        setFont(getFont().deriveFont(Font.BOLD));

        setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    private Color color1;

    private Color color2;

    private GradientPaint paint;
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if ((color1 != null && color2 != null)) {

            if (paint == null) {
                int h = getHeight();

                paint = new GradientPaint(new Point(0, h / 2), color2, new Point(getWidth(), h / 2), color1, false);
            }

            g2.setPaint(paint);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        super.paintComponent(g);
    }

}
