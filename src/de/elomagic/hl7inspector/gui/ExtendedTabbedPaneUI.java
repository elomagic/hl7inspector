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

import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author rambow
 */
public class ExtendedTabbedPaneUI extends BasicTabbedPaneUI implements MouseMotionListener, MouseListener {

    /** Creates a new instance of ExtendedTabbedPaneUI */
    public ExtendedTabbedPaneUI() {
        super();

        closeImgB = new BufferedImage(BUTTONSIZE, BUTTONSIZE, BufferedImage.TYPE_4BYTE_ABGR);
        closeImgI = new BufferedImage(BUTTONSIZE, BUTTONSIZE, BufferedImage.TYPE_4BYTE_ABGR);
        closeB = new JButton();
        closeB.setSize(BUTTONSIZE, BUTTONSIZE);
//        closeB.setOpaque(true);

        closeImgI = ResourceLoader.loadBufferedImage("close_view.gif");
        closeImgB = ResourceLoader.loadBufferedImage("close_view.gif");
    }

    protected static final int BUTTONSIZE = 16;
    //private BufferedImage closeImgB;

    private BufferedImage closeImgI;

    private BufferedImage closeImgB;

    private JButton closeB;

    private static final Border PRESSEDBORDER = new SoftBevelBorder(SoftBevelBorder.LOWERED);

    private static final Border OVERBORDER = new SoftBevelBorder(SoftBevelBorder.RAISED);
    @Override
    protected void installListeners() {
        super.installListeners();
        tabPane.addMouseMotionListener(this);
        tabPane.addMouseListener(this);
    }

    private boolean hover = false;

    private boolean mousePressed = false;
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        int h = (maxTabHeight - BUTTONSIZE) / 2;

        paintCloseIcon(g, c.getSize().width - BUTTONSIZE - h, h);
    }

    @Override
    protected int calculateTabWidth(int tabPlacement,
            int tabIndex,
            FontMetrics metrics) {


        int result = tabPane.getWidth() / tabPane.getTabCount();

        return result;
    }

    protected Rectangle getCloseButtonRectangle() {
        int h = (maxTabHeight - BUTTONSIZE) / 2;

        int w = tabPane.getSize().width;

        Rectangle result = new Rectangle(w - BUTTONSIZE - h, h, BUTTONSIZE, BUTTONSIZE);

        return result;
    }

    protected void paintCloseIcon(Graphics g, int dx, int dy) {
        paintActionButton(g, dx, dy, closeB, closeImgB);

        g.drawImage(closeImgI, dx, dy, null);
    }

    protected void paintActionButton(Graphics g, int dx, int dy, JButton button, BufferedImage image) {
        button.setBorder(null);

        if (hover) {

            if (mousePressed) {
                button.setBorder(PRESSEDBORDER);
            } else {
                button.setBorder(OVERBORDER);
            }

            button.setBackground(tabPane.getBackground());
            button.paint(image.getGraphics());
            g.drawImage(image, dx, dy, null);

        } else {
//            button.setBorder(PRESSEDBORDER);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Rectangle r = getCloseButtonRectangle();

        boolean b = hover;
        hover = r.contains(e.getX(), e.getY());

        if (b != hover) {
            tabPane.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    //
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Rectangle r = getCloseButtonRectangle();

        boolean b = mousePressed;
        mousePressed = r.contains(e.getX(), e.getY());

        if (b != mousePressed) {
            tabPane.repaint();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ((e.getClickCount() != 0) && (mousePressed)) {
            ((ExtendedTabbedPane) tabPane).fireCloseTabEvent();
        }
    }

}
