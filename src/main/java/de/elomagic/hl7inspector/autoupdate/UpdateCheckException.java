/*
 * Copyright 2011 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.autoupdate;

/**
 *
 * @author carstenrambow
 */
public class UpdateCheckException extends Exception {

    private static final long serialVersionUID = 2147054167671262268L;

    /**
     * Creates a new instance of <code>UpdateCheckException</code> without detail message.
     */
    public UpdateCheckException() {
    }

    public UpdateCheckException(Throwable thrwbl) {
        super(thrwbl);
    }

    /**
     * Constructs an instance of <code>UpdateCheckException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UpdateCheckException(String msg) {
        super(msg);
    }

}
