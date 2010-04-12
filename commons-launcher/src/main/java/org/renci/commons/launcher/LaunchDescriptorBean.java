package org.renci.commons.launcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author jdr0887
 */
public class LaunchDescriptorBean {

    protected Long jobId;

    protected List<String> preCommands;

    protected List<String> commands;

    protected List<String> postCommands;

    protected List<String> inputFilesToTransfer;

    protected List<String> outputFilesToTransfer;

    protected Map<String, String> requiredInputMap;

    protected Map<String, String> optionalInputMap;

    public LaunchDescriptorBean() {
        super();
        init();
    }

    public LaunchDescriptorBean(Long jobId) {
        super();
        this.jobId = jobId;
        init();
    }

    private void init() {
        this.preCommands = new LinkedList<String>();
        this.commands = new LinkedList<String>();
        this.postCommands = new LinkedList<String>();
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

    /**
     * @return the preCommands
     */
    public List<String> getPreCommands() {
        return preCommands;
    }

    /**
     * @param preCommands
     *            the preCommands to set
     */
    public void setPreCommands(List<String> preCommands) {
        this.preCommands = preCommands;
    }

    /**
     * @return the postCommands
     */
    public List<String> getPostCommands() {
        return postCommands;
    }

    /**
     * @param postCommands
     *            the postCommands to set
     */
    public void setPostCommands(List<String> postCommands) {
        this.postCommands = postCommands;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("jobId = ").append(jobId).append("\n");
        if (commands != null && commands.size() > 0) {
            sb.append("commands.size() = ").append(commands.size()).append("\n");
        } else {
            sb.append("commands is null ").append("\n");
        }

        if (inputFilesToTransfer != null && inputFilesToTransfer.size() > 0) {
            for (String inputFileToTransfer : inputFilesToTransfer) {
                sb.append("inputFileToTransfer = ").append(inputFileToTransfer).append("\n");
            }
        } else {
            sb.append("inputFilesToTransfer is null ").append("\n");
        }

        if (requiredInputMap != null && requiredInputMap.size() > 0) {
            Set<String> keySet = requiredInputMap.keySet();
            for (String key : keySet) {
                sb.append("key = ").append(key);
                sb.append(", value = ").append(requiredInputMap.get(key)).append("\n");
            }
        } else {
            sb.append("requiredInputMap is null ").append("\n");
        }

        if (optionalInputMap != null && optionalInputMap.size() > 0) {
            Set<String> keySet = optionalInputMap.keySet();
            for (String key : keySet) {
                sb.append("key = ").append(key);
                sb.append(", value = ").append(optionalInputMap.get(key)).append("\n");
            }
        } else {
            sb.append("optionalInputMap is null ").append("\n");
        }

        return sb.toString();
    }

}
