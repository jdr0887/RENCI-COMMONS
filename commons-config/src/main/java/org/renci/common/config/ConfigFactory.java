package org.renci.common.config;

import java.util.Properties;

/**
 * 
 * @author jdr0887
 */
public class ConfigFactory {

    public static ConfigService createConfigService(String projectName) {
        ConfigService configService = ConfigServiceImpl.getInstance(projectName);
        return configService;
    }

    public static ConfigService createConfigService(String projectName, Properties properties) {
        ConfigService configService = ConfigServiceImpl.getInstance(projectName, properties);
        return configService;
    }

}
