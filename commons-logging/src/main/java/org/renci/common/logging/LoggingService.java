/*******************************************************************************
 * 
 * RENCI Open Source Software License � The University of North Carolina at
 * Chapel Hill
 * 
 * The University of North Carolina at Chapel Hill (the "Licensor") through its
 * Renaissance Computing Institute (RENCI) is making an original work of
 * authorship (the "Software") available through RENCI upon the terms set forth
 * in this Open Source Software License (this "License"). This License applies
 * to any Software that has placed the following notice immediately following
 * the copyright notice for the Software: Licensed under the RENCI Open Source
 * Software License v. 1.0.
 * 
 * Licensor grants You, free of charge, a world-wide, royalty-free,
 * non-exclusive, perpetual, sublicenseable license to do the following to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  . Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimers.
 *  . Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimers in the documentation
 * and/or other materials provided with the distribution.
 *  . Neither You nor any sublicensor of the Software may use the names of
 * Licensor (or any derivative thereof), of RENCI, or of contributors to the
 * Software without explicit prior written permission. Nothing in this License
 * shall be deemed to grant any rights to trademarks, copyrights, patents, trade
 * secrets or any other intellectual property of Licensor except as expressly
 * stated herein.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * CONTIBUTORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * You may use the Software in all ways not otherwise restricted or conditioned
 * by this License or by law, and Licensor promises not to interfere with or be
 * responsible for such uses by You. This Software may be subject to U.S. law
 * dealing with export controls. If you are in the U.S., please do not mirror
 * this Software unless you fully understand the U.S. export regulations.
 * Licensees in other countries may face similar restrictions. In all cases, it
 * is licensee's responsibility to comply with any export regulations applicable
 * in licensee's jurisdiction.
 * 
 ******************************************************************************/
package org.renci.common.logging;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * 
 * @author jdr0887
 */
public class LoggingService {

    private static LogFactory factory;

    static {
        try {
            logConfigurator();
            factory = LogFactory.getFactory();
        } catch (Exception e) {
            System.out.println("Unable to initialize logger..." + e.getMessage());
        }
    }

    private static void logConfigurator() throws FileNotFoundException {
        File pwDir = new File(".");
        String config = "log4j.xml";
        File configFile = new File(pwDir, config);
        URL configUrl = null;
        try {
            if (configFile.exists()) {
                // pwd log file supersedes all others
                configUrl = configFile.toURI().toURL();
            } else {
                String userHome = System.getProperty("user.home");
                File renciHiddenDir = new File(userHome, ".renci");
                if (!renciHiddenDir.exists()) {
                    renciHiddenDir.mkdir();
                }
                configFile = new File(renciHiddenDir, config);
                if (!configFile.exists()) {
                    ClassLoader cl = LoggingService.class.getClassLoader();
                    InputStream is = cl.getResourceAsStream(config);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuffer sb = new StringBuffer();
                    String line;
                    try {
                        while ((line = br.readLine()) != null) {
                            
                            if (line.indexOf("<param name=\"File\"") != -1) {
                                
                                File logsDir = new File(userHome, "logs");
                                if (!logsDir.exists()) {
                                    logsDir.mkdirs();
                                }
                                int index = line.indexOf("value=");
                                String tmp = line.substring(index + 6, line.length()).replace("\"", "").replace("/>", "");
                                tmp = tmp.substring(tmp.lastIndexOf("/") + 1, tmp.length());
                                line = "<param name=\"File\" value=\"" +logsDir + File.separator + tmp + "\"/>"; 
                            }
                            
                            sb.append(line).append("\n");
                        }
                        FileUtils.writeStringToFile(configFile, sb.toString(), "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                configUrl = configFile.toURI().toURL();
                System.out.println("configUrl.toString() = " + configUrl.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (configUrl != null) {
            try {
                DOMConfigurator.configure(configUrl);
            } catch (Exception e) {
                e.printStackTrace();
                // throw new FileNotFoundException("Cannot file log4j.xml");
            }
        }
        
    }

    public static Log getLog(String className) {
        return (factory.getLog(className));
    }

}
