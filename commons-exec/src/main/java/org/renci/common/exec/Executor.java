package org.renci.common.exec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * This is really a wrapper for running cmdline applications locally
 * 
 * Requires jdk1.5 or greater
 * 
 * @author rynge, jdr0887
 */
public class Executor {

    // the directory where to run the command from
    private File workDir;

    // the shell environment
    private Map<String, String> environment;

    // the command to run
    private String command;

    // exit value
    private int exitCode;

    // stdout
    private StringBuffer stdout;

    // stderr
    private StringBuffer stderr;

    private String scriptTemplate = null;

    /**
     * 
     * @param command
     *            command to run
     */
    public Executor(String command) {
        super();
        this.command = command;
        this.workDir = new File("/tmp");
        environment = new HashMap<String, String>();
        initVelocity();
        scriptTemplate = readResourceToString("org/renci/commons/exec/script.sh.vm");
    }

    /**
     * 
     * @param command
     * @param workDir
     */
    public Executor(String command, File workDir) {
        super();
        this.command = command;
        this.workDir = workDir;
        environment = new HashMap<String, String>();
        initVelocity();
        scriptTemplate = readResourceToString("org/renci/commons/exec/script.sh.vm");
    }

    private void initVelocity() {
        try {
            Properties props = new Properties();
            props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
            Velocity.init(props);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * run the command
     * 
     * @return exit code
     * @throws ExecuteException
     */
    public int execute() throws ExecutorException {

        Process process = null;
        StreamGobbler stdoutGobbler = null;
        StreamGobbler stderrGobbler = null;

        VelocityContext vc = new VelocityContext();
        vc.put("workDir", workDir.getAbsolutePath());
        vc.put("command", command);

        File wrapperFile = null;

        // create the executable
        try {
            wrapperFile = File.createTempFile("shellwrapper-", ".sh", workDir);
            FileWriter fw = new FileWriter(wrapperFile);
            Velocity.evaluate(vc, fw, null, scriptTemplate);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new ExecutorException("Unable to create tmp file");
        }

        // chmod the executable
        try {
            Process chmodProcess = new ProcessBuilder("/bin/chmod", "755", wrapperFile.getAbsolutePath()).start();
            chmodProcess.waitFor();
        } catch (IOException e1) {
            throw new ExecutorException(e1.getMessage());
        } catch (InterruptedException e1) {
            throw new ExecutorException(e1.getMessage());
        }

        // run it
        try {
            ProcessBuilder pb = new ProcessBuilder(wrapperFile.getAbsolutePath());
            pb.directory(workDir);
            Map<String, String> env = pb.environment();
            env.putAll(environment);
            process = pb.start();

            // outputs
            stdoutGobbler = new StreamGobbler(process.getInputStream());
            stdoutGobbler.start();
            stderrGobbler = new StreamGobbler(process.getErrorStream());
            stderrGobbler.start();

            try {
                exitCode = process.waitFor();
            } catch (InterruptedException exp) {
                throw new ExecutorException("Interrupted: " + exp.getMessage());
            }

            // let the io streams buffering catch up
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // ignore
            }

        } catch (IOException ioe) {
            throw new ExecutorException("Process error: " + ioe.getMessage());
        } finally {

            // get the outputs weather the process failed or not
            if (stdoutGobbler != null) {
                stdout = stdoutGobbler.getOutput();
            }
            if (stderrGobbler != null) {
                stderr = stderrGobbler.getOutput();
            }

            try {

                if (stdoutGobbler != null) {
                    stdoutGobbler.close();
                }
                if (stderrGobbler != null) {
                    stderrGobbler.close();
                }

                // close streams
                if (process != null) {
                    InputStream istr = process.getErrorStream();
                    if (istr != null) {
                        istr.close();
                    }
                    OutputStream ostr = process.getOutputStream();
                    if (ostr != null) {
                        ostr.close();
                    }
                    istr = process.getErrorStream();
                    if (istr != null) {
                        istr.close();
                    }
                    process.destroy();
                }
            } catch (IOException exp) {
                // ignore
            }
        }

        // clean up
        wrapperFile.delete();
        return exitCode;
    }

    private String readResourceToString(String resource) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(resource);
        String ret = null;
        try {
            ret = IOUtils.toString(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    /**
     * sets the location of the workdir
     * 
     * @param workDir
     */
    public void setWorkDir(File workDir) {
        this.workDir = workDir;
    }

    /**
     * 
     * @return the exit code
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * 
     * @return the stdout
     */
    public String getStdout() {
        return stdout.toString();
    }

    /**
     * 
     * @return the stderr
     */
    public String getStderr() {
        return stderr.toString();
    }

}
