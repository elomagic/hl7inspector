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

package de.elomagic.hl7inspector.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.swing.JTree;


/**
 * This inner class's sole responsibility is to translate
 * the coordinate system before invoking a canvas's
 * painter. The coordinate system is translated in order
 * to get the desired portion of a canvas to line up with
 * the top of a page.
 */
public final class TilePrintable implements Printable {
    public TilePrintable(Point2D origin, JTree tree, double scale) {
        this.origin = origin;
        this.tree = tree;
        this.scale = scale;
    }
    
    private Point2D origin;
    private double scale;
    private JTree tree;
    
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        Graphics2D g2 = (Graphics2D) graphics.create();
        try {
            Rectangle componentBounds = tree.getBounds(null);
            
            g2.translate(-origin.getX(), -origin.getY());
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            //g2.translate(-componentBounds.x, -componentBounds.y);
            //g2.scale(scale, scale);
            boolean wasBuffered = tree.isDoubleBuffered();
            tree.paint(g2);
            tree.setDoubleBuffered(wasBuffered);
        } finally {
            g2.dispose();
        }
        
        if (pageFormat instanceof Printable) {
            Printable formatPainter = (Printable) pageFormat;
            formatPainter.print(graphics, pageFormat, pageIndex);
        }
        
        return PAGE_EXISTS;
    }
}