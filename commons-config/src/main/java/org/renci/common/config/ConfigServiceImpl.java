package org.renci.common.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * 
 * @author jdr0887
 */
public class ConfigServiceImpl extends AbstractConfigService implements ConfigService {

    private static ConfigServiceImpl instance = null;

    private Configuration config = null;

    private Properties properties;

    static ConfigServiceImpl getInstance(String projectName, Properties properties) {
        if (instance == null) {
            instance = new ConfigServiceImpl(projectName, properties);
        }
        return (instance);
    }

    static ConfigServiceImpl getInstance(String projectName) {
        if (instance == null) {
            instance = new ConfigServiceImpl(projectName);
        }
        return (instance);
    }

    private ConfigServiceImpl(String projectName, Properties properties) {
        super(projectName);
        this.properties = properties;
        init();
    }

    private ConfigServiceImpl(String projectName) {
        super(projectName);
        init();
    }

    private void init() {

        File projectConfigFile = new File(projectDir, this.projectName + ".xml");
        if (!projectConfigFile.exists()) {

            try {
                Velocity.init();
            } catch (Exception e) {
                e.printStackTrace();
            }

            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("projectName", projectName);
            StringWriter sw = new StringWriter();
            try {
                String template = readResourceToString("org/renci/common/config/config.vm");
                Velocity.evaluate(velocityContext, sw, null, template);
                FileUtils.writeStringToFile(projectConfigFile, sw.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (sw != null) {
                    try {
                        sw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            File configPropertiesFile = new File(projectDir, this.projectName + ".properties");

            if (!configPropertiesFile.exists()) {

                try {
                    if (this.properties != null) {
                        FileWriter fileWriter = new FileWriter(configPropertiesFile);
                        properties.store(fileWriter, null);
                    } else {
                        FileUtils.touch(configPropertiesFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        try {
            ConfigurationFactory configFactory = new ConfigurationFactory(projectConfigFile.getAbsolutePath());
            config = configFactory.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.renci.common.configuration.ConfigurationService#getProperty(java.lang.String)
     */
    public String getProperty(String property) {
        return (config.getString(property));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.renci.common.configuration.ConfigurationService#getPropertyArray(java.lang.String)
     */
    public String[] getPropertyArray(String property) {
        return (config.getStringArray(property));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.renci.common.config.ConfigService#getConfigDirectory()
     */
    public File getConfigDirectory() {
        return projectDir;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.renci.common.config.ConfigService#getProperty(java.lang.String,
     *      java.lang.String)
     */
    public String getProperty(String property, String def) {
        String ret = getProperty(property);
        if (StringUtils.isEmpty(ret)) {
            return def;
        }
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.renci.common.config.ConfigService#getPropertyArray(java.lang.String,
     *      java.lang.String[])
     */
    public String[] getPropertyArray(String property, String[] def) {
        String[] ret = getPropertyArray(property);
        if (ret == null || (ret != null && ret.length == 0)) {
            return def;
        }
        return ret;
    }

}
