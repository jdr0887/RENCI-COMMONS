package org.renci.common.config;

import java.util.Properties;

/**
 * 
 * @author jdr0887
 */
public class ConfigFactory {

    public static ConfigService createConfigService(String projectName) {
        ConfigService configService = new ConfigServiceImpl(projectName);
        return configService;
    }

    public static ConfigService createConfigService(String projectName, Properties properties) {
        ConfigService configService = new ConfigServiceImpl(projectName, properties);
        return configService;
    }

}
