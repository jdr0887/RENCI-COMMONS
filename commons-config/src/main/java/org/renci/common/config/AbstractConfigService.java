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

}
