/*
 * EndOfStreamException.java
 *
 * Created on 20. Mai 2006, 17:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.elomagic.hl7inspector.io;

import java.io.IOException;

/**
 *
 * @author rambow
 */
public class EndOfStreamException extends IOException {
    
    /** Creates a new instance of EndOfStreamException */
    public EndOfStreamException() { super("End of stream reached"); }
    
}
