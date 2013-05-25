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

import com.l2fprod.common.swing.BaseDialog;
import com.l2fprod.common.swing.JButtonBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rambow
 */
public class PanelDialog extends BaseDialog {
    private static final long serialVersionUID = 6158738386923154208L;
    private List<AbstractPanel> panelList = new ArrayList<>();
    protected AbstractPanel selectedPanel = null;

    /**
     * Creates a new instance of PanelDialog.
     */
    public PanelDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);

        init();
    }

    public AbstractPanel getSelected() {
        return selectedPanel;
    }

    public void setSelected(AbstractPanel optionPanel) {
        if(!optionPanel.equals(selectedPanel)) {
            for(int i = 0; i < getContentPane().getComponentCount(); i++) {
                if(getContentPane().getComponent(i) instanceof AbstractPanel) {
                    getContentPane().remove(i);
                }
            }

            if(selectedPanel != null) {
                selectedPanel.getBarButton().setSelected(false);
            }

            selectedPanel = optionPanel;

            if(selectedPanel != null) {
                getContentPane().add(selectedPanel, BorderLayout.CENTER);
                selectedPanel.getBarButton().setSelected(true);
            }

            selectedPanel.revalidate();
            selectedPanel.repaint();

            //pack();
        }
    }

    @Override
    public boolean ask() {
        read();

        boolean result = super.ask();

        if(result) {
            write();
        }

        return result;
    }

    protected void read() {
        for(AbstractPanel p : panelList) {
            p.read();
        }
    }

    protected void write() {
        for(AbstractPanel p : panelList) {
            p.write();
        }
    }

    protected void init() {
        getBanner().setVisible(false);

        JButtonBar buttonBar = new JButtonBar();
        buttonBar.setOrientation(JButtonBar.VERTICAL);

        for(AbstractPanel p : panelList) {
            buttonBar.add(p.getBarButton());
        }

        buttonBar.setPreferredSize(new Dimension(80, 10));

        getContentPane().add(buttonBar, BorderLayout.WEST);

        setAlwaysOnTop(true);

        if(!panelList.isEmpty()) {
            setSelected(panelList.get(0));
        }

        pack();

        setSize(500, 500);
        setLocationRelativeTo(Desktop.getInstance().getMainFrame());
    }

    public List<AbstractPanel> getPanelList() {
        return panelList;
    }
}
