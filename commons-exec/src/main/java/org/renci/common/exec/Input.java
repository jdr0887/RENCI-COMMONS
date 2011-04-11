package org.renci.common.exec;

import java.io.File;
import java.util.Map;

public class Input {

    // the directory where to run the command from
    protected File workDir;

    // the shell environment
    protected Map<String, String> environment;

    // the command to run
    protected String command;

    // timeout for the command - in seconds
    protected long maxRunTime = 60;

    // stdin
    protected StringBuffer stdin;

    private String profileFile;

    /**
     * 
     * @param command
     *            command to run
     */
    public Input() {
        this.environment = null;
        this.workDir = new File("/tmp");
    }

    public Input(String command, File workDir) {
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

    /**
     * @return the profileFile
     */
    public String getProfileFile() {
        return profileFile;
    }

    /**
     * @param profileFile
     *            the profileFile to set
     */
    public void setProfileFile(String profileFile) {
        this.profileFile = profileFile;
    }

}
