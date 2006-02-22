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

package de.elomagic.hl7inspector.hl7.model;

import java.io.File;
import org.apache.log4j.Logger;

/**
 *
 * @author rambow
 */
public class Message extends Hl7Object {
  
  /** Creates a new instance of Message */
  public Message() { }
  
  public char getSubDelimiter() { return (char)0x0d; }
  
  public Hl7Object getNewClientInstance() { return new Segment(); }     

  private String source = "";
  public void setSource(String messageSource) { source = messageSource; }
  public String getSource() { return source; }
  
  public void setFile(File f) { 
      try {
          source = (f==null)?"":f.toURL().toString(); 
      } catch (Exception e) {
          source = "";
          Logger.getLogger(getClass()).error(e.getMessage(), e);
      }
  }  
  
  /** @deprecated */
  public File getFile() { return null; }
}
