/*
 * Copyright 2011 Carsten Rambow, elomagic, Roedersheim/Gronau Germany. All rights reserved.
 */
package de.elomagic.hl7inspector.autoupdate;

import de.elomagic.hl7inspector.TestTool;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carstenrambow
 */
public class UpdateCheckerTest {

    public UpdateCheckerTest() {
    }

    @Test
    public void testCheckUpdates() throws IOException {
        System.out.println("checkUpdates");
        UpdateChecker instance = new UpdateChecker();
        String s = TestTool.readResource("/VersionText.xml");
        assertFalse(instance.checkUpdates(s, "3.0.0.0"));
        assertTrue(instance.checkUpdates(s, "2.0.0.0"));
    }

}
