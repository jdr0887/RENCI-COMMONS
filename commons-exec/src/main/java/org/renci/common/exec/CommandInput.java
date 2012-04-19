package org.renci.common.exec;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author jdr0887
 * 
 */
public class CommandInput implements Serializable {

    private static final long serialVersionUID = 2997188750784524667L;

    private File workDir;

    private Map<String, String> environment;

    private String command;

    private long maxRunTime = 0;

    private StringBuffer stdin;

    public CommandInput() {
        this.environment = null;
        this.workDir = new File(System.getProperty("java.io.tmpdir"));
    }

    public CommandInput(String command, File workDir) {
        this.environment = null;
        this.workDir = workDir;
        this.command = command;
    }

    /**
     * @return the workDir
     */
    public File getWorkDir() {
        return workDir;
    }

    /**
     * @param workDir
     *            the workDir to set
     */
    public void setWorkDir(File workDir) {
        this.workDir = workDir;
    }

    /**
     * @return the environment
     */
    public Map<String, String> getEnvironment() {
        return environment;
    }

    /**
     * @param environment
     *            the environment to set
     */
    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command
     *            the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the maxRunTime
     */
    public long getMaxRunTime() {
        return maxRunTime;
    }

    /**
     * @param maxRunTime
     *            the maxRunTime to set
     */
    public void setMaxRunTime(long maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    /**
     * @return the stdin
     */
    public StringBuffer getStdin() {
        return stdin;
    }

    /**
     * @param stdin
     *            the stdin to set
     */
    public void setStdin(StringBuffer stdin) {
        this.stdin = stdin;
    }

}
