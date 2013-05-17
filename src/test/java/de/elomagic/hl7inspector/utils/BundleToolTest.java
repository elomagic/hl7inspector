/*
 * Copyright 2013 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.utils;

import de.elomagic.hl7inspector.gui.Desktop;
import java.util.Locale;
import java.util.ResourceBundle;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carstenrambow
 */
public class BundleToolTest {
    public BundleToolTest() {
    }

    /**
     * Test of getBundle method, of class BundleTool.
     */
    @Test
    public void testGetBundle() {
        System.out.println("getBundle");

        ResourceBundle result = BundleTool.getBundle(Desktop.class);
        String s = result.getString("app_title");

        assertEquals("{0} {1} ({2}; {3}) - An Open Source project from Carsten Rambow", s);
    }
}