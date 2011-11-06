/*
 * Copyright 2011 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author carstenrambow
 */
public class TestTool {

    public static String readResource(String resourceName) throws IOException {
        InputStream in = TestTool.class.getResourceAsStream(resourceName);
        return IOUtils.toString(in);
    }

}
