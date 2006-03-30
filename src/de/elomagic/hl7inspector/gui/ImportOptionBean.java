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

import de.elomagic.hl7inspector.io.Frame;

/**
 *
 * @author rambow
 */
public class ImportOptionBean {
    
    /** Creates a new instance of ImportOptionBean */
    public ImportOptionBean() {
    }
    
    public enum StreamFormat { AUTO_DETECT, FRAMED, TEXT_LINE };     
    
    private boolean         readBottom      = false;
    private boolean         useRegExpr      = false;
    private boolean         negReg          = false;
    private String          phrase          = "";
    private StreamFormat    importMode      = StreamFormat.AUTO_DETECT;
    private Character       startChar       = new Character((char)11);
    private Character       stopChar1       = new Character((char)28);
    private Character       stopChar2       = new Character((char)13);
    private int             bufferSize      = 100;
    private boolean         clearBuffer     = false;
    private String          source          = "";
    private long            fileSize        = -1;
    private String          sourceLabel     = "Name:";
    private boolean         caseSensitive   = false;
    private String          encoding        = "ISO-8859-1";
    private boolean         validate        = false;
            
            public Frame getFrame() { return new Frame(startChar, stopChar1, stopChar2); }
    
    public boolean isReadBottom() { return readBottom; }
    
    public void setReadBottom(boolean readBottom) { this.readBottom = readBottom; }
    
    public boolean isUseRegExpr() { return useRegExpr; }
    
    public void setUseRegExpr(boolean useRegExpr) { this.useRegExpr = useRegExpr; }
    
    public boolean isNegReg() { return negReg; }
    
    public void setNegReg(boolean negReg) { this.negReg = negReg; }
    
    public String getPhrase() { return phrase; }
    
    public void setPhrase(String phrase) { this.phrase = phrase; }
    
    public StreamFormat getImportMode() { return importMode; }
    
    public void setImportMode(StreamFormat importMode) { this.importMode = importMode; }
    
    public void setFrame(Frame frame) {
        startChar = new Character(frame.getStartFrame());
        
        if (frame.getStopFrameLength() > 0) {
            stopChar1 = new Character(frame.getStopFrame()[0]);
        }
        
        if (frame.getStopFrameLength() > 1) {
            stopChar2 = new Character(frame.getStopFrame()[1]);
        }
    }
    
    public Character getStartChar() { return startChar; }
    
    public void setStartChar(Character startChar) { this.startChar = startChar; }
    
    public Character getStopChar1() { return stopChar1; }
    
    public void setStopChar1(Character stopChar1) { this.stopChar1 = stopChar1; }
    
    public Character getStopChar2() { return stopChar2; }
    
    public void setStopChar2(Character stopChar2) { this.stopChar2 = stopChar2; }
    
    public int getBufferSize() { return bufferSize; }
    
    public void setBufferSize(int bufferSize) { this.bufferSize = bufferSize; }
    
    public boolean isClearBuffer() { return clearBuffer; }
    
    public void setClearBuffer(boolean ClearBuffer) { this.clearBuffer = ClearBuffer; }
    
    public String getSource() { return source; }
    
    public void setSource(String source) { this.source = source; }
    
    public String getSourceLabel() { return sourceLabel; }
    
    public void setSourceLabel(String sourceLabel) { this.sourceLabel = sourceLabel; }
    
    public boolean isCaseSensitive() { return caseSensitive; }
    
    public void setCaseSensitive(boolean caseSensitive) { this.caseSensitive = caseSensitive; }
    
    public long getFileSize() { return fileSize; }
    
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    
    public String getEncoding() { return encoding; }

    /** 
     * Supported Encoders:
     *  US-ASCII    Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode character set
     *  ISO-8859-1  ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
     *  UTF-8       Eight-bit UCS Transformation Format
     *  UTF-16BE    Sixteen-bit UCS Transformation Format, big-endian byte order
     *  UTF-16LE    Sixteen-bit UCS Transformation Format, little-endian byte order
     *  UTF-16
     **/    
    public void setEncoding(String encoding) { this.encoding = encoding; }

    public boolean isValidate() {  return validate; }

    public void setValidate(boolean validate) { this.validate = validate; }    
}
