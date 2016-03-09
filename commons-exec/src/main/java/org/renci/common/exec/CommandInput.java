package org.renci.common.exec;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

public class CommandInput implements Serializable {

    private static final long serialVersionUID = 2997188750784524667L;

    private File workDir;

    private Map<String, String> environment;

    private String command;

    private Boolean exitImmediately;

    private long maxRunTime;

    private StringBuffer stdin;

    public CommandInput() {
        this.workDir = new File(System.getProperty("java.io.tmpdir"));
        this.maxRunTime = 0;
        this.exitImmediately = Boolean.TRUE;
    }

    public CommandInput(String command) {
        this();
        this.command = command;
    }

    public CommandInput(String command, File workDir) {
        this(command);
        this.workDir = workDir;
    }

    public CommandInput(String command, File workDir, Boolean exitImmediately) {
        this(command, workDir);
        this.exitImmediately = exitImmediately;
    }

    public File getWorkDir() {
        return workDir;
    }

    public void setWorkDir(File workDir) {
        this.workDir = workDir;
    }

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getMaxRunTime() {
        return maxRunTime;
    }

    public void setMaxRunTime(long maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    public StringBuffer getStdin() {
        return stdin;
    }

    public void setStdin(StringBuffer stdin) {
        this.stdin = stdin;
    }

    public Boolean getExitImmediately() {
        return exitImmediately;
    }

    public void setExitImmediately(Boolean exitImmediately) {
        this.exitImmediately = exitImmediately;
    }

    @Override
    public String toString() {
        return String.format(
                "CommandInput [workDir=%s, environment=%s, command=%s, exitImmediately=%s, maxRunTime=%s, stdin=%s]",
                workDir, environment, command, exitImmediately, maxRunTime, stdin);
    }

}
