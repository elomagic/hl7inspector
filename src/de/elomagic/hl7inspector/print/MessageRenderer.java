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

import de.elomagic.hl7inspector.gui.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import javax.swing.JTree;

/**
 *
 * @author rambow
 */
public class MessageRenderer implements Pageable {
    
    public MessageRenderer() { 
        init(Desktop.getInstance().getTree());
    }
    
    /** Creates a new instance of MessageRenderer */
    public MessageRenderer(JTree tree) { init(tree); }
    
    private void init(JTree tree) {
        setComponent(tree);
        
        //Rectangle componentBounds = component.getBounds(null);        
        //setSize(componentBounds.width, componentBounds.height);
        Dimension dim = this.tree.getPreferredSize();       
        setSize(dim.width, dim.height);
        setScale(0.7);
        //scaleToFit();        
    }
    
    private int countPagesX;
    private int countPagesY;
    private int countPages;    
    private PageFormat format = HFFormat.getInstance();
    
    protected void setSize(float width, float height) {
        countPagesX = (int) ((width + format.getImageableWidth() - 1)/ format.getImageableWidth());
        countPagesY = (int) ((height + format.getImageableHeight() - 1)/ format.getImageableHeight());
        countPages = countPagesX * countPagesY;
    }
    
    private JTree tree;
    protected void setComponent(JTree c) { this.tree = c; }
    
    private double scale;
    protected void setScale(double scale) { this.scale = scale; }
    
    public void scaleToFit() {
        Rectangle componentBounds = tree.getBounds(null);
        double scaleX = format.getImageableWidth() / componentBounds.width;
        double scaleY = format.getImageableHeight() / componentBounds.height;
        
        if (scaleX < 1 || scaleY < 1) {
            if (scaleX < scaleY) {
                scaleY = scaleX;
            } else {
                scaleX = scaleY;
            }
            
            setSize( (float) (componentBounds.width * scaleX), (float) (componentBounds.height * scaleY) );
            setScale(scaleX);
        }
    }
    
    // Interface Pageable
    
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        if (pageIndex >= countPages) {
            throw new IndexOutOfBoundsException();
        }
        
        double originX = (pageIndex % countPagesX) * format.getImageableWidth();
        double originY = (pageIndex / countPagesX) * format.getImageableHeight();
        
        Point2D.Double origin = new Point2D.Double(originX, originY);
        
        return new TilePrintable(origin, tree, scale);
    }
    
    public int getNumberOfPages() { return countPages; }
    
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        if (pageIndex >= countPages) {
            throw new IndexOutOfBoundsException();
        }
        
        return format;
    }    
}