/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.elomagic.hl7inspector.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JLabel;

/**
 *
 * @author carsten.rambow
 */
public class LinkLabel extends JLabel implements MouseListener {

    public LinkLabel(String text, String uri) throws URISyntaxException {
        super();
        init(text, uri);
    }

    private void init(String text, String uri) throws URISyntaxException {
        if (text.indexOf("<html>") == -1) {
            text = "<html><u><font color=blue>" + text + "</font></u></html>";
        }
        
        setText(text);       
        
        this.uri = new URI(uri);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(uri);

        addMouseListener(this);

    }
    private URI uri;

    public void mouseClicked(MouseEvent e) {
        try {
            if (uri.toString().indexOf('@') == -1) {
                java.awt.Desktop.getDesktop().browse(uri);
            } else {
                java.awt.Desktop.getDesktop().mail(uri);
            }
        } catch (Exception ex) {
            SimpleDialog.error(ex);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
