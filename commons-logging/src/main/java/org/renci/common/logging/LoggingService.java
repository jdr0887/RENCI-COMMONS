package org.renci.common.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

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

            // pwd config file supersedes all others
            if (configFile.exists()) {
                configUrl = configFile.toURI().toURL();
                DOMConfigurator.configure(configUrl);
                return;
            }

            // now look in users home directory
            String userHome = System.getProperty("user.home");
            configFile = new File(userHome, config);
            if (configFile.exists()) {
                configUrl = configFile.toURI().toURL();
                DOMConfigurator.configure(configUrl);
                return;
            }

            // now look in the users hidden ~/.renci directory
            configFile = new File(userHome + File.separator + ".renci", config);
            if (configFile.exists()) {
                configUrl = configFile.toURI().toURL();
                DOMConfigurator.configure(configUrl);
                return;
            }

            // now look in classloader
            try {
                InputStream is = LoggingService.class.getClassLoader().getResourceAsStream(config);
                if (is != null && !configFile.exists()) {
                    List<String> lines = IOUtils.readLines(is);
                    FileUtils.writeLines(configFile, "UTF-8", lines);
                    configUrl = configFile.toURI().toURL();
                    DOMConfigurator.configure(configUrl);
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // now create a basic logger using system specific temp directory
            try {
                Properties props = new Properties();
                props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
                Velocity.init(props);
            } catch (Exception e) {
                e.printStackTrace();
            }

            VelocityContext vc = new VelocityContext();
            vc.put("logHome", System.getProperty("java.io.tmpdir"));

            String xml = createLog4jXml(vc);
            if (StringUtils.isNotEmpty(xml)) {
                FileUtils.writeStringToFile(configFile, xml);
                configUrl = configFile.toURI().toURL();
                DOMConfigurator.configure(configUrl);
                return;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String createLog4jXml(VelocityContext velocityContext) {
        StringWriter sw = new StringWriter();
        try {
            InputStream is = LoggingService.class.getClassLoader().getResourceAsStream("log4j.xml.vm");
            StringBuilder sb = new StringBuilder();
            if (is != null) {
                List<String> lines = IOUtils.readLines(is);
                for (String line : lines) {
                    sb.append(line).append("\n");
                }
            }
            Velocity.evaluate(velocityContext, sw, null, sb.toString());
        } catch (ParseErrorException e) {
            e.printStackTrace();
        } catch (MethodInvocationException e) {
            e.printStackTrace();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public static Log getLog(String className) {
        return (factory.getLog(className));
    }

}
