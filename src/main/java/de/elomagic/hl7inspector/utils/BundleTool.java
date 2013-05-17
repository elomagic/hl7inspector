/*
 * Copyright 2013 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Resource bundle utility class.
 */
public final class BundleTool {
    private BundleTool() {
    }

    /**
     * Returns resource bundle of package of given class.
     *
     * @param c Class
     * @return Bundle
     */
    public static ResourceBundle getBundle(final Class c) {
        String name = c.getPackage().getName().replace('.', '/').concat("/Bundle");

        return ResourceBundle.getBundle(name);
    }
}
