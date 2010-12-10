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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.elomagic.hl7inspector.gui.actions.DefaultCloseWindowAction;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author rambow
 */
public class SimpleOkCancelButtonPanel extends JPanel {

    /** Creates a new instance of SimpleOkCancelButtonPanel */
    public SimpleOkCancelButtonPanel(Component parent) {
        init(new DefaultCloseWindowAction(null), new DefaultCloseWindowAction(parent));
    }

    public SimpleOkCancelButtonPanel(Action okAction, Action closeAction) {
        init(okAction, closeAction);
    }

    protected final void init(Action okAction, Action closeAction) {
        btOk = new JButton(okAction);
        btClose = new JButton(closeAction);
        setLayout(new BorderLayout());

        FormLayout layout = new FormLayout(
                "p",
                "p, 4dlu, p");   // rows

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.add(btOk, cc.xy(1, 1));
        builder.add(btClose, cc.xy(1, 3));

        //getContentPane().add(builder.getPanel());
        add(builder.getPanel(), BorderLayout.CENTER);
    }

    private JButton btOk;

    private JButton btClose;
}
