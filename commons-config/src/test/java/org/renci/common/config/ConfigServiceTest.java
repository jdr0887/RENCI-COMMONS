package org.renci.common.config;

import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author jdr0887
 */
public class ConfigServiceTest {

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.put("test.key", "test.value");
        ConfigService mgr = ConfigFactory.createConfigService("test", properties);
        assertNotNull(mgr);
        String testValue = mgr.getProperty("test.key");
        assertEquals(testValue, "test.value");
    }

    @Test
    public void testSetProperty() {
        ConfigService mgr = ConfigFactory.createConfigService("test");
        assertNotNull(mgr);
        mgr.setProperty("asdfasd", "qwerqwer");
    }

}
