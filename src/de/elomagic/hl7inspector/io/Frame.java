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

package de.elomagic.hl7inspector.io;

/**
 *
 * @author rambow
 */
public class Frame {
  
  /** Creates a new instance of Frame */
  public Frame() { start = 0xb; stops = new char[] { 0x1c, 0xd }; }

  public Frame(char startFrame, char[] stopFrames) {
    start = startFrame;
    stops = stopFrames;
  }
  
  public Frame(Character startChar, Character stopChar1, Character stopChar2) {
    start = startChar.charValue();
    
    int len = (stopChar2!=null)?2:1;
    
    stops = new char[len];   
    stops[0] = stopChar1.charValue();
    
    if (len > 1) {
        stops[1] = stopChar2.charValue();
    }
  }
  
  public void setStartChar(char c) { start = c; }
  
  public void setStopChars(char[] c) { stops = c; }  
  
  public char getStartFrame() { return start; }
  
  public char[] getStopFrame() { return stops; }
  
  public int getStopFrameLength() { return stops.length; }
  
  private char start;
  private char[]stops;  
}
