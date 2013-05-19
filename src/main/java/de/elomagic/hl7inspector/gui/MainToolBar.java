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

import de.elomagic.hl7inspector.gui.actions.*;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 *
 * @author rambow
 */
public class MainToolBar extends JToolBar {
    private JToggleButton btNodeDesc;
    private JToggleButton btCompactView;
    private JToggleButton btShowDetails;
    private JToggleButton btParserWindow;
    private JToggleButton btReceiveWindow;
    private JToggleButton btSendWindow;

    /**
     * Creates a new instance of ToolBar.
     */
    public MainToolBar() {
        super();

        setRollover(true);
        setFloatable(true);

        btNodeDesc = new JToggleButton(new ViewNodeDescriptionAction(""));
        btCompactView = new JToggleButton(new ViewCompressedAction(""));
        btCompactView.setSelected(true);
        btShowDetails = new JToggleButton(new ViewNodeDetailsAction(""));
        btParserWindow = new JToggleButton(new ShowParserWindowAction(""));
        btReceiveWindow = new JToggleButton(new ShowReceiveWindowAction(false));
        btSendWindow = new JToggleButton(new ShowSendWindowAction(false));

        // FEATURE Button for rereading file.

        add(new FileNewAction());
        add(new FileOpenAction());
        add(new FileSaveAsAction());
        addSeparator();
        add(new FindWindowAction(""));
        addSeparator();
        add(btCompactView);
        //addSeparator();
        add(btNodeDesc);
        add(btShowDetails);
        add(new ValidateMessageAction());
        addSeparator();
        add(new ViewTextFile());
        add(new ViewHexFile());
        addSeparator();
        add(btParserWindow);
        add(btReceiveWindow);
        add(btSendWindow);
        addSeparator();
        add(new ExitAction());
    }

    public JToggleButton getNodeDescriptionButton() {
        return btNodeDesc;
    }

    public JToggleButton getCompactViewButton() {
        return btCompactView;
    }

    public JToggleButton getDetailsButton() {
        return btShowDetails;
    }

    public JToggleButton getParserWindowButton() {
        return btParserWindow;
    }

    public JToggleButton getReceiveWindowButton() {
        return btReceiveWindow;
    }

    public JToggleButton getSendWindowButton() {
        return btSendWindow;
    }
}
