package org.renci.common.config;

import java.io.File;
import java.util.Properties;

/**
 * 
 * @author jdr0887
 */
public interface ConfigService {

    /**
     * 
     * @return
     */
    public File getConfigDirectory();

    /**
     * 
     * @param name
     * @param value
     */
    public void setProperty(String name, Object value);

    /**
     * 
     * @param property
     * @return
     */
    public String getProperty(String property);

    /**
     * 
     * @param property
     * @param def
     * @return
     */
    public String getProperty(String property, String def);

    /**
     * 
     * @param property
     * @return
     */
    public String[] getPropertyArray(String property);

    /**
     * 
     * @param property
     * @return
     */
    public String[] getPropertyArray(String property, String[] def);

    /**
     * 
     * @param properties
     */
    public void addAll(Properties properties);

}