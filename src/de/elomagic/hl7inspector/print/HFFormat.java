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

import de.elomagic.hl7inspector.Hl7Inspector;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author rambow
 */
public class HFFormat extends PageFormat implements Printable {
    public HFFormat() {
        super();
        
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        
        setOrientation(pf.getOrientation());
        setPaper(pf.getPaper());
    }
    
    private final static HFFormat INSTANCE = new HFFormat();
    public final static HFFormat getInstance() { return INSTANCE; }
    
    private static final Font font = new Font("Serif", Font.ITALIC, 8);
    private static final float height = (float) (0.25 * 72);
    
    public double getPrintableY() { return getImageableY() + height; }
    
    public double getPrintableHeight() {
        double imageableHeight = getImageableHeight() - height - height;
        if (imageableHeight < 0) {
            imageableHeight = 0;
        }
        
        return imageableHeight;
    }
    
    private void drawString(Graphics2D g2d, String text, float alignment, boolean header) {        
        double y;
        
        if (header) {
            y = getImageableY() + height;            
        } else {
            LineMetrics metrics = font.getLineMetrics(text, g2d.getFontRenderContext());
            y = (double) (getImageableY() + getImageableHeight()- metrics.getDescent() - metrics.getLeading());
        }
        
        double x = 0;
        
        if (alignment == Label.LEFT_ALIGNMENT) {
            x = getImageableX();
        } else if (alignment == Label.RIGHT_ALIGNMENT) {
            Rectangle2D r2d = font.getStringBounds(text, g2d.getFontRenderContext());
            x = getImageableX() + getImageableWidth() - r2d.getWidth();
        }
        
        g2d.drawString(text, (float)x, (float)y);
    }
    
    // Interface Printable
    
    public int print(Graphics g, PageFormat format, int pageIndex) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setFont(font);
            
            // Draw header
            LineMetrics metrics = font.getLineMetrics("Xy", g2d.getFontRenderContext());
            int y = (int)(metrics.getDescent() + metrics.getLeading() + height + getImageableY());
            
            g2d.setPaint(Color.gray);
            g2d.drawLine(
                    (int) getImageableX(), 
                    y, 
                    (int) (getImageableX() + getImageableWidth()), 
                    y);
            
            g2d.setPaint(Color.black);
            drawString(g2d, Hl7Inspector.APPLICATION_NAME + " " + Hl7Inspector.getVersionString(), Label.LEFT_ALIGNMENT, true);
            drawString(g2d, DateFormat.getDateTimeInstance().format(new Date()), Label.RIGHT_ALIGNMENT, true);
            
            // Draw footer
            y = (int) (getImageableY() + getImageableHeight() - metrics.getHeight()/* metrics.getDescent() - metrics.getLeading() - height*/);
            
            g2d.setPaint(Color.gray);
            g2d.drawLine(
                    (int) getImageableX(), 
                    y, 
                    (int) (getImageableX() + getImageableWidth()), 
                    y);
            
            g2d.setPaint(Color.black);
            drawString(g2d, Hl7Inspector.APPLICATION_NAME + " " + Hl7Inspector.getVersionString(), Label.LEFT_ALIGNMENT, false);
            drawString(g2d, "Page #" + (pageIndex+1), Label.RIGHT_ALIGNMENT, false);
        } finally {
            g2d.dispose();
        }
        
        return Printable.PAGE_EXISTS;
    }
}
