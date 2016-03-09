package org.renci.common.exec;

import java.io.Serializable;
import java.util.Date;

public class CommandOutput implements Serializable {

    private static final long serialVersionUID = -3237512903661251806L;

    private int exitCode;

    private StringBuilder stdout = new StringBuilder();

    private StringBuilder stderr = new StringBuilder();

    private Date startDate;

    private Date endDate;

    public CommandOutput() {
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void clear() {
        stdout = null;
        stderr = null;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public StringBuilder getStdout() {
        return stdout;
    }

    public void setStdout(StringBuilder stdout) {
        this.stdout = stdout;
    }

    public StringBuilder getStderr() {
        return stderr;
    }

    public void setStderr(StringBuilder stderr) {
        this.stderr = stderr;
    }

    @Override
    public String toString() {
        return String.format("CommandOutput [exitCode=%s, stdout=%s, stderr=%s, startDate=%s, endDate=%s]", exitCode,
                stdout, stderr, startDate, endDate);
    }

}
