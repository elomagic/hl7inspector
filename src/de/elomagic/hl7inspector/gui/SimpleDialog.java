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
/*
 * SimpleDialog.java
 *
 * Created on 10. May 2005, 17:01
 */

import com.l2fprod.common.swing.BaseDialog;
import de.elomagic.hl7inspector.images.ResourceLoader;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author  rambow
 */
public class SimpleDialog {

    /** Creates a new instance of SimpleDialog */
    private SimpleDialog() {
    }

    public static String ask(String text, String def) {
        return JOptionPane.showInputDialog(getParent(), text, def);
    }

    public static void error(String text) {
        showExtendedDialog("Error", text, JOptionPane.ERROR_MESSAGE);
    }
//    public static void error(String text) { JOptionPane.showMessageDialog(getParent(), text, "Error", JOptionPane.ERROR_MESSAGE); }

    public static void error(Exception e) {
        JOptionPane.showMessageDialog(getParent(), e.getMessage() + "\n\n" + getFormatedExceptionText(e, 20), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void error(Exception e, String text) {
        JOptionPane.showMessageDialog(getParent(), text + "\n\n" + getFormatedExceptionText(e, 20), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(String text) {
        JOptionPane.showMessageDialog(getParent(), text, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void info(String title, String text) {
        showExtendedDialog(title, text, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warn(String text) {
        showExtendedDialog("Warning", text, JOptionPane.WARNING_MESSAGE);
    }

    public static void warn(String title, String text) {
        showExtendedDialog(title, text, JOptionPane.WARNING_MESSAGE);
    }
//    public static void warn(String text) { JOptionPane.showMessageDialog(getParent(), text, "Warning", JOptionPane.WARNING_MESSAGE); }   

    public static int confirmYesNo(String text) {
        return JOptionPane.showConfirmDialog(getParent(), text, "Confirmation", JOptionPane.YES_NO_OPTION);
    }

    public static int YES_OPTION = JOptionPane.YES_OPTION;

    public static int NO_OPTION = JOptionPane.NO_OPTION;
    /* private */
    private static String getFormatedExceptionText(Exception e, int maxLines) {
        String s = "";
        if (e.getMessage() != null) {
            s = "Message: " + e.getMessage();
        }

        int lines = 0;
        for (int i = 0; (i < e.getStackTrace().length) && (i < maxLines); i++) {
            s = s.concat("\n" + e.getStackTrace()[i].toString());
            lines++;
        }

        if (lines > e.getStackTrace().length) {
            s = s.concat("...");
        }

        return s;
    }

    private static Window getParent() {
        return KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
    }

    private static void showExtendedDialog(String title, String message, int messageType) {
        String t = "";
        String i = "";

        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                t = "Error";
                i = "critical.png";
                break;
            case JOptionPane.INFORMATION_MESSAGE:
                t = "Information";
                i = "info.png";
                break;
            case JOptionPane.WARNING_MESSAGE:
                t = "Warning";
                i = "warning.png";
                break;
            default:
                t = "Message";
                i = "info.png";
        }

        BaseDialog dlg;

        Window c = getParent();
        if (c instanceof Frame) {
            dlg = new BaseDialog((Frame) c, t);
        } else if (c instanceof Dialog) {
            dlg = new BaseDialog((Dialog) c, t);
        } else if (Desktop.getInstance() != null) {
            dlg = new BaseDialog(Desktop.getInstance(), t);
        } else {
            dlg = null;
        }

        dlg.getBanner().setTitle(title);
        dlg.getBanner().setIcon(ResourceLoader.loadImageIcon(i, ResourceLoader.LARGE_IMAGE));
        dlg.setDialogMode(BaseDialog.CLOSE_DIALOG);
        dlg.setSize(640, 400);
        dlg.setModal(true);
        dlg.centerOnScreen();//set ToolKit.centerFrame(dlg, getParent());

        JTextArea ta = new JTextArea(message);
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);

        dlg.getContentPane().add(sp);

        dlg.ask();
    }

}
