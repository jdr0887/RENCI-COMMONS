package org.renci.commons.launcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author jdr0887
 */
public class LaunchDescriptorBean {

    protected Long jobId;

    protected List<String> commands;

    protected List<String> inputFilesToTransfer;

    protected List<String> outputFilesToTransfer;

    protected Map<String, String> requiredInputMap;

    protected Map<String, String> optionalInputMap;

    public LaunchDescriptorBean() {
        super();
        this.commands = new LinkedList<String>();
    }

    public LaunchDescriptorBean(Long jobId) {
        super();
        this.jobId = jobId;
        this.commands = new LinkedList<String>();
    }

    /**
     * @return the requiredInputMap
     */
    public Map<String, String> getRequiredInputMap() {
        return requiredInputMap;
    }

    /**
     * @param requiredInputMap
     *            the requiredInputMap to set
     */
    public void setRequiredInputMap(Map<String, String> requiredInputMap) {
        this.requiredInputMap = requiredInputMap;
    }

    /**
     * @return the optionalInputMap
     */
    public Map<String, String> getOptionalInputMap() {
        return optionalInputMap;
    }

    /**
     * @param optionalInputMap
     *            the optionalInputMap to set
     */
    public void setOptionalInputMap(Map<String, String> optionalInputMap) {
        this.optionalInputMap = optionalInputMap;
    }

    /**
     * @return the jobId
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * @param jobId
     *            the jobId to set
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * @return the commands
     */
    public List<String> getCommands() {
        return commands;
    }

    /**
     * @param commands
     *            the commands to set
     */
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    /**
     * @return the inputFilesToTransfer
     */
    public List<String> getInputFilesToTransfer() {
        return inputFilesToTransfer;
    }

    /**
     * @param inputFilesToTransfer
     *            the inputFilesToTransfer to set
     */
    public void setInputFilesToTransfer(List<String> inputFilesToTransfer) {
        this.inputFilesToTransfer = inputFilesToTransfer;
    }

    /**
     * @return the outputFilesToTransfer
     */
    public List<String> getOutputFilesToTransfer() {
        return outputFilesToTransfer;
    }

    /**
     * @param outputFilesToTransfer
     *            the outputFilesToTransfer to set
     */
    public void setOutputFilesToTransfer(List<String> outputFilesToTransfer) {
        this.outputFilesToTransfer = outputFilesToTransfer;
    }

}
