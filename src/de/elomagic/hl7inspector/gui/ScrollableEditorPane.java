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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author rambow
 */
public class ScrollableEditorPane extends JPanel {
    
    /** Creates a new instance of ScrollableEditorPane */
    public ScrollableEditorPane() {
        super(new BorderLayout());

        editorPane = new JEditorPane();        
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setFont(Font.getFont("Arial"));
        
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(editorPane);
        
        captionPane = new WindowCaptionPanel();
        
        add(captionPane, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JEditorPane         editorPane;
    private JScrollPane         scrollPane;
    private WindowCaptionPanel  captionPane;
                    
    public WindowCaptionPanel getCaption() { return captionPane; }
    public JEditorPane getEditorPane() { return editorPane; }             
}
