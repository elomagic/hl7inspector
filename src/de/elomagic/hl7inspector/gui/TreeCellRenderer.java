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

import de.elomagic.hl7inspector.StartupProperties;
import de.elomagic.hl7inspector.hl7.model.*;
import de.elomagic.hl7inspector.images.ResourceLoader;
import de.elomagic.hl7inspector.model.Hl7TreeModel;
import de.elomagic.hl7inspector.profile.DataElement;
import de.elomagic.hl7inspector.profile.DataTypeItem;
import de.elomagic.hl7inspector.profile.MessageDescriptor;
import de.elomagic.hl7inspector.profile.Profile;
import de.elomagic.hl7inspector.profile.SegmentItem;
import de.elomagic.hl7inspector.utils.StringEscapeUtils;
import de.elomagic.hl7inspector.utils.StringVector;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class TreeCellRenderer extends JLabel /*DefaultTreeCellRenderer*/ implements javax.swing.tree.TreeCellRenderer {
    
    /** Creates a new instance of TreeCellRenderer */
    public TreeCellRenderer() { 
        setBackground(SystemColor.textHighlight);
    }
        
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        
        StartupProperties prop = StartupProperties.getInstance();
        
        boolean highlight = FindBar.getInstance().isHighlight() && FindBar.getInstance().isVisible();
        
        StringBuffer sb = new StringBuffer();
                
        TreeModel model = (TreeModel)tree.getModel();
        int index = -1;        
        
        String nodeText = "";
        
        if (model instanceof Hl7TreeModel) {            
            if (value instanceof Hl7Object) {
                Hl7Object hl7Object = (Hl7Object)value;
                
                Hl7Object parent = hl7Object.getParent();
                                
                if (!(value instanceof de.elomagic.hl7inspector.hl7.model.Segment)) {
                    if (value instanceof de.elomagic.hl7inspector.hl7.model.Message) {
                        index = model.getIndexOfChild(hl7Object.getRoot(), value)+1;
                    } else if ((value instanceof de.elomagic.hl7inspector.hl7.model.Field)
                            && (parent.size() < 2)) {             
                        index = parent.getIndex();
                    } else if (value instanceof de.elomagic.hl7inspector.hl7.model.RepetitionField) {
                        index = hl7Object.getIndex();
                    } else {
                        index = hl7Object.getIndex()+1;
                    }

                    sb.append("<font color=\"#");
                    sb.append(Integer.toHexString(SystemColor.textInactiveText.getRGB()&0xffffff));
                    sb.append("\"><B>");

                    sb.append("&lt;#");
                    sb.append(index);
                    sb.append("&gt; ");

                    sb.append("</B></font>");                                           
                }

                if ((value instanceof de.elomagic.hl7inspector.hl7.model.Message) && (prop.getTreeViewMode() == 1)) {
                    Message m = (Message)value;                    

                    // Get date/time
                    String d = m.get(0).get(7).toHtmlEscapedString();
                    try {
                        String p = "yyyyMMddHHmmss";
                        p = p.substring(0, d.length());                        
                        Date dt = new SimpleDateFormat(p).parse(d);
                        d = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(dt);
                        sb.append(d);
                    } catch(Exception e) {
                        sb.append(d);
                        Logger.getLogger(getClass()).warn(e.getMessage(), e);
                    }
                    sb.append(' ');

                    // Append Source and destination
                    sb.append(m.get(0).get(5).toHtmlEscapedString());
                    sb.append(" &gt; ");
                    sb.append(m.get(0).get(3).toHtmlEscapedString());
                    sb.append(' ');
                    sb.append("<B>");
                    sb.append(m.get(0).get(9).toHtmlEscapedString());
                    sb.append("</B>");                        
                } else {
                    nodeText = hl7Object.toHtmlEscapedString();

                    boolean truncate = ((prop.getTreeNodeLength() != 0) && (prop.getTreeNodeLength() < nodeText.length()));                

                    if (truncate) {
                        if (nodeText.length() > prop.getTreeNodeLength()) {
                            nodeText = nodeText.substring(0, prop.getTreeNodeLength());
                        }
                        sb.append(nodeText);
//                        sb.delete(prop.getTreeNodeLength(), sb.length());
                        sb.append("<font color=\"#");
                        sb.append(Integer.toHexString(SystemColor.magenta.getRGB()&0xffffff));
                        sb.append("\"><B>###</B></font>");
                    } else {                    
                        sb.append(nodeText);
                    }
                }
            }
        }
        
        String v = sb.toString();
        String ov = v;
	
        if (highlight) {
	    String phrase = FindBar.getInstance().getEscapedPhrase();
	    
	    if (phrase.length() != 0) {                   
		boolean caseSensitive = FindBar.getInstance().isCaseSensitive();

		if (!caseSensitive) {
		    v = v.toUpperCase();
		    phrase = phrase.toUpperCase();
		}

		int i = v.indexOf(phrase);

		if (i != -1) {
		    String nv = "";
                    int oi = 0;		

		    while (i != -1) {
			String op = ov.substring(i, i+phrase.length());
			
			nv = nv.concat(ov.substring(oi, i));
			nv = nv.concat("<font color=\"#ff0000\"><b>");
			nv = nv.concat(op);
			nv = nv.concat("</b></font>");
			i = i + phrase.length();
			oi = i;
			i = v.indexOf(phrase, oi);
		    }

		    v = nv.concat(ov.substring(oi));
		} else {
		    v = ov;
		}
	    }
        }
        
        sb = new StringBuffer(v);
                
        setOpaque(sel);
        
        // Get node description
        if (value instanceof Hl7Object) {
            Hl7Object obj = (Hl7Object)value;
//            setToolTipText(obj.getValidationText());
            
            if (((Hl7TreeModel)model).isViewDescription()) {                        
                Profile profile = Desktop.getInstance().getProfile();
                
                String desc = obj.getText();
                StringVector tt = new StringVector();
                String segType = "";
                
                if (desc == null) {
                    desc = "";
                    // Get segment type 
                    Hl7Object o = obj;
                    while (!((o instanceof de.elomagic.hl7inspector.hl7.model.Segment) || (o instanceof de.elomagic.hl7inspector.hl7.model.Message))) 
                        o = o.getParent();
                    if (o != null) {
                        segType = o.get(0).toString();
                        if (segType.length() > 3) {
                            segType = segType.substring(0, 2);
                        }
                    }
                    
                    o = obj;
                    
                    while (!(o.getParent() instanceof de.elomagic.hl7inspector.hl7.model.RepetitionField) && o.getParent() != null)
                        o = o.getParent();
                    
                    int fieldIndex = index;
                    if ((o.getParent() instanceof de.elomagic.hl7inspector.hl7.model.RepetitionField)) {
                    //    && (o.getParent().size() > 1))                    
                        fieldIndex = o.getParent().getIndex();
                    }
                    
                    MessageDescriptor md = new MessageDescriptor(profile);

                    if (value instanceof de.elomagic.hl7inspector.hl7.model.Segment) {
                        try {
                            SegmentItem segDef = profile.getSegmentList().getSegment(segType);

                            if (segDef != null) {
                                desc = segDef.getDescription();                                
                                tt.add("Segment Name: ".concat(segDef.getDescription()));
                                if (segDef.getChapter().length() != 0) tt.add("Chapter: ".concat(segDef.getChapter()));
                            }
                        } catch (Exception e) {
                            Logger.getLogger(this.getClass()).error(e, e);
                        }
                    } else if (value instanceof de.elomagic.hl7inspector.hl7.model.RepetitionField) {
                        DataElement de = profile.getDataElementList().getDataElement(segType, index);
                        if (de != null) {
                            desc = de.getName();
                            tt.add("Field Name: ".concat(de.getName()));
                            if (de.getChapter().length() != 0) tt.add("Chapter: ".concat(de.getChapter()));
                            if (de.getTable().length() != 0) tt.add("Table: ".concat(de.getTable()));
                        }
                    } else if (value instanceof de.elomagic.hl7inspector.hl7.model.Field) {
                        DataElement de = md.getDataElement((Hl7Object)value);                        
//                        DataElement de = profile.getDataElementList().getDataElement(segType, index);
                        if (de != null) {
                            desc = "[".concat(de.getDataType()).concat("] ").concat(de.getName());
                            tt.add("Field Name: ".concat(de.getName()));
                            if (de.getChapter().length() != 0) tt.add("Chapter: ".concat(de.getChapter()));
                            if (de.getTable().length() != 0) tt.add("Table: ".concat(de.getTable()));
                        }
                    } else if (value instanceof de.elomagic.hl7inspector.hl7.model.Component) {
                        DataElement de = profile.getDataElementList().getDataElement(segType, fieldIndex);
                        if (de != null) {
                            DataTypeItem dt = profile.getDataTypeList().getDataType(de.getDataType(), index);
                            if (dt != null) {
                                desc = "[".concat(dt.getDataType()).concat("] ").concat(dt.getDescription());
                                tt.add("Component Type: ".concat(dt.getDescription()));
                                if (dt.getChapter().length() != 0) tt.add("Chapter: ".concat(dt.getChapter()));
                                if (dt.getTable().length() != 0) tt.add("Table: ".concat(dt.getTable()));
                            }
                        }
                    } else if (value instanceof de.elomagic.hl7inspector.hl7.model.Subcomponent) {
                        DataElement de = profile.getDataElementList().getDataElement(segType, fieldIndex);
                        if (de != null) {
                            int compIndex = obj.getParent().getIndex()+1;                            
                            DataTypeItem dt = profile.getDataTypeList().getDataType(de.getDataType(), compIndex);
                            if (dt != null) {
                                dt = profile.getDataTypeList().getDataType(dt.getDataType(), index);
                                if (dt != null) {
                                    desc = "[".concat(dt.getDataType()).concat("] ").concat(dt.getDescription());
                                    tt.add("SubComponent Type: ".concat(dt.getDescription()));
                                    if (dt.getChapter().length() != 0) tt.add("Chapter: ".concat(dt.getChapter()));
                                    if (dt.getTable().length() != 0) tt.add("Table: ".concat(dt.getTable()));
                                }
                            }
                        }                    
                    }
                    
                    desc = StringEscapeUtils.escapeHtml(desc);
                    
                    obj.setText(desc);
                    obj.setDescription(tt.toString('\n'));
                    
//                    setToolTipText(tt.toString());
                }                    

                if (desc.length() != 0) {
                    sb.append("<font color=\"#");
                    sb.append(Integer.toHexString(SystemColor.textInactiveText.getRGB()&0xffffff));
                    sb.append("\"><B> (");
                    sb.append(desc);
                    sb.append(")</B></font>");
                }
            }            
        }
        
        String fontName = prop.getTreeFontName();
        
        if (sel) {
            sb.insert(0, "<html><body><font face=\"" +  fontName + "\" color=\"#" + Integer.toHexString(SystemColor.textHighlightText.getRGB()&0xffffff) + "\">");
        } else
            sb.insert(0, "<html><body><font face=\"" +  fontName + "\">");
        
        sb.append("</font>");        
        sb.append("</body></html>");
        
        //boolean c = !(getText().equals(v));
        setText(sb.toString());
        
        if (value instanceof Hl7Object) {
            Hl7Object obj = (Hl7Object)value;
            
            if (obj.getValidateStatus() != null) {
                setIcon(obj.getValidateStatus().getIcon());
            } else {
                if ((value instanceof RepetitionField) && (!(value instanceof EncodingObject)))
                    setIcon(ResourceLoader.loadImageIcon("repeatfield.png"));
                else
                    setIcon(ResourceLoader.loadImageIcon("hole_white.gif"));                
            }                        
        } else {
            setIcon(ResourceLoader.loadImageIcon("hole_white.gif"));
        }
        
        return this;
    }
}
