package org.renci.common.exec;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author jdr0887
 * 
 */
public class CommandOutput implements Serializable {

    private static final long serialVersionUID = -3237512903661251806L;

    private int exitCode;

    private StringBuffer stdout;

    private StringBuffer stderr;

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

    /**
     * clear resources used
     */
    public void clear() {
        stdout = null;
        stderr = null;
    }

    /**
     * @return the exitCode
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * @param exitCode
     *            the exitCode to set
     */
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * @return the stdout
     */
    public StringBuffer getStdout() {
        return stdout;
    }

    /**
     * @param stdout
     *            the stdout to set
     */
    public void setStdout(StringBuffer stdout) {
        this.stdout = stdout;
    }

    /**
     * @return the stderr
     */
    public StringBuffer getStderr() {
        return stderr;
    }

    /**
     * @param stderr
     *            the stderr to set
     */
    public void setStderr(StringBuffer stderr) {
        this.stderr = stderr;
    }

}
