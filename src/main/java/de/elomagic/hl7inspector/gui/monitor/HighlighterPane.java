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
package de.elomagic.hl7inspector.gui.monitor;

import de.elomagic.hl7inspector.utils.StringEscapeUtils;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class HighlighterPane extends JEditorPane {

    /** Creates a new instance of HighlighterPane */
    public HighlighterPane() {
        super();

        setContentType("text/html");
        setEditable(false);
        setDoubleBuffered(true);

        //refreshThread.start();
    }

    @Override
    protected void finalize() throws Throwable {
        refreshThread.terminating = true;

        super.finalize();
    }

    public void addChar(char c) {
        if (!refreshThread.isAlive()) {
            refreshThread.start();
        }

        if (c < 0x20) {
            buffer = buffer.append("<font color=\"fuchsia\">&lt;");
//            buffer = buffer.append("&lt;");            

            if (c == '&') {
                buffer = buffer.append("&amp");
            } else {
                String s = Integer.toHexString(c);
                if (s.length() < 2) {
                    s = "0".concat(s);
                }
                s = "0x".concat(s);
                buffer = buffer.append(s);
            }

//            buffer = buffer.append("&gt;");
            buffer = buffer.append("&gt;</font>");

            if (c == 0x0d) {
                buffer = buffer.append("<br>");
            }
        } else {
            buffer.append(StringEscapeUtils.escapeHtml("" + c));
        }
        changed = true;
    }

    public void addLine(String value) {
        if (!refreshThread.isAlive()) {
            refreshThread.start();
        }

        if (buffer.lastIndexOf("<br>") != (buffer.length() - 4)) {
            buffer.append("<br>");
        }
        buffer.append(StringEscapeUtils.escapeHtml(value) + "<br>");
        changed = true;
    }

    public void clear() {
        buffer = new StringBuffer();
        changed = true;

        if (!refreshThread.isAlive()) {
            refreshThread.start();
        }
    }

    private StringBuffer buffer = new StringBuffer();

    private DoRun doRun = new DoRun();

    private boolean changed = false;
    private String surroundHtml(StringBuffer sb) {
        StringBuffer result = new StringBuffer();
        result.append("<HTML><BODY><font face=\"Courier New, Tahoma, Arial\" size=\"3\">");
        result.append(sb);
        result.append("</font></BODY></HTML>");

        return result.toString();
    }

    private RefreshThread refreshThread = new RefreshThread();

    class RefreshThread extends Thread {

        @Override
        public void run() {
            while (!terminating) {
                try {
                    if (isVisible() && (!doRun.running)) {
                        SwingUtilities.invokeLater(doRun);
                    }

                    sleep(50);
                } catch (Exception e) {
                    Logger.getLogger(getClass()).error(e.getMessage(), e);
                }
            }
        }

        public boolean terminating = false;
    }

    class DoRun implements Runnable {

        public boolean running = false;
        @Override
        public void run() {
            running = true;
            try {
                if (changed) {
                    changed = false;
                    String s = getText();

                    //                if (buffer.length() > 8192) {
                    //                    int i = buffer.indexOf("<br>", 4096);
                    //                    buffer = buffer.delete(0, i);
                    //                }

                    String newValue = surroundHtml(buffer);

                    if (!s.equals(newValue)) {
                        setText(newValue);
                    }
                }
            } finally {
                running = false;
            }
        }

    }
}
