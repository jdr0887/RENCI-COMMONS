package org.renci.common.logging;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.junit.Test;

/**
 * 
 * @author jdr0887
 */
public class LoggingServiceTest {

    @Test
    public void testLogger() {
        Log logger = LoggingService.getLog(this.getClass().getName());
        assertNotNull(logger);
        logger.debug("test");
    }

    @Test
    public void testDifferentPackage() {
        Log logger = LoggingService.getLog("org.renci.common");
        assertNotNull(logger);
        logger.info("test");
    }

}
