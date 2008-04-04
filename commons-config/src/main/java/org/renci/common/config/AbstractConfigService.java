package org.renci.common.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @author jdr0887
 */
public abstract class AbstractConfigService {
    
    protected String projectName;

    protected File projectDir;

    protected AbstractConfigService(String projectName) {
        super();
        this.projectName = projectName;
        // need to create config file if it doesn't exist
        String userHome = System.getProperty("user.home");
        File renciConfigDir = new File(userHome, ".renci");
        if (!renciConfigDir.exists()) {
            renciConfigDir.mkdirs();
        }

        File projectDir = new File(renciConfigDir, projectName);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }
        this.projectDir = projectDir;
    }

    protected String readResourceToString(String resource) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(resource);
        String ret = null;
        try {
            ret = IOUtils.toString(is);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

}
