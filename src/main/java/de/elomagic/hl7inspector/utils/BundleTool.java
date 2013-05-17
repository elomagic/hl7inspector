/*
 * Copyright 2013 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.utils;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 */
public final class BundleTool {
    private BundleTool() {
    }

    public static ResourceBundle getBundle(final Class c) {
        String name = c.getPackage().getName().replace('.', '/').concat("/Bundle");

        try {
            return ResourceBundle.getBundle(name);
        } catch(MissingResourceException ex) {
            return ResourceBundle.getBundle(name, Locale.ROOT);
        }
    }
}
