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

    /**
     * 
     * @return
     */
    public Boolean isSet(String key);

    /**
     * Retrieves an attribute as bool
     * 
     * @param key
     * @return
     */
    public Boolean getAsBoolean(String key);

    /**
     * Retrieves an attribute as longint
     * 
     * @param key
     * @return
     */
    public Long getAsLong(String key);

    /**
     * Retrieves an attribute as double
     * 
     * @param key
     * @return
     */
    public Double getAsDouble(String key);

}