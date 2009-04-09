package org.renci.common.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author jdr0887
 */
public class ConfigServiceImpl extends AbstractConfigService implements ConfigService {

    private PropertiesConfiguration config = null;

    private Properties properties;

    public ConfigServiceImpl(String projectName, Properties properties) {
        super(projectName);
        this.properties = properties;
        init();
    }

    public ConfigServiceImpl(String projectName) {
        super(projectName);
        init();
    }

    private void init() {
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

        try {
            config = new PropertiesConfiguration(configPropertiesFile);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        if (this.properties != null) {
            for (Object key : this.properties.keySet()) {
                config.setProperty(key.toString(), this.properties.get(key));
            }
            try {
                config.save();
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
        }

    }

    public void setProperty(String name, Object value) {
        config.setProperty(name, value);
        try {
            config.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.renci.common.configuration.ConfigurationService#getProperty(java.
     * lang.String)
     */
    public String getProperty(String property) {
        return (config.getString(property));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.renci.common.configuration.ConfigurationService#getPropertyArray(
     * java.lang.String)
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
     * java.lang.String)
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
     * @see
     * org.renci.common.config.ConfigService#getPropertyArray(java.lang.String,
     * java.lang.String[])
     */
    public String[] getPropertyArray(String property, String[] def) {
        String[] ret = getPropertyArray(property);
        if (ret == null || (ret != null && ret.length == 0)) {
            return def;
        }
        return ret;
    }

    public void addAll(Properties properties) {
        this.properties.putAll(properties);
    }

}
