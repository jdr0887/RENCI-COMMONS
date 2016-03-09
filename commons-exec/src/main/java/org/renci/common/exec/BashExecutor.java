package org.renci.common.exec;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jdr0887
 * 
 */
public class BashExecutor implements Executor {

    private static final Logger logger = LoggerFactory.getLogger(BashExecutor.class);

    private static BashExecutor instance = null;

    public static BashExecutor getInstance() {
        if (instance == null) {
            return new BashExecutor();
        }
        return instance;
    }

    private BashExecutor() {
        super();
    }

    public CommandOutput execute(CommandInput input, Map<String, String> subsitutionMap, File... sources)
            throws ExecutorException {
        logger.debug("ENTERING execute(CommandInput, File...)");
        File wrapperFile = null;
        long processStartTime = System.currentTimeMillis();
        String delayedError = null;

        CommandOutput output = new CommandOutput();
        output.setStartDate(new Date());

        // create a shell script with the command line in it
        StringBuilder sourceFileSB = new StringBuilder();
        if (sources != null && sources.length > 0) {
            for (File source : sources) {
                if (source.exists()) {
                    sourceFileSB.append(String.format(". %s%n", source.getAbsolutePath()));
                }
            }
        }

        logger.info(input.toString());

        try {
            
            wrapperFile = File.createTempFile("shellwrapper-", ".sh", input.getWorkDir());
            String resolvedCommand = input.getCommand();
            if (subsitutionMap != null) {
                for (String key : subsitutionMap.keySet()) {
                    if (resolvedCommand.contains(key)) {
                        resolvedCommand = resolvedCommand.replaceAll(key, subsitutionMap.get(key));
                    }
                }
            }

            String wrapperContents;
            if (input.getExitImmediately()) {
                wrapperContents = String.format("#!/bin/bash -e%n%s%ncd %s%n%s%n",
                        sourceFileSB.length() == 0 ? "" : sourceFileSB.toString(), input.getWorkDir().getAbsolutePath(),
                        resolvedCommand);
            } else {
                wrapperContents = String.format("#!/bin/bash%n%s%ncd %s%n%s%n",
                        sourceFileSB.length() == 0 ? "" : sourceFileSB.toString(), input.getWorkDir().getAbsolutePath(),
                        resolvedCommand);
            }

            logger.debug("wrapperContents: {}", wrapperContents);
            FileUtils.writeStringToFile(wrapperFile, wrapperContents, "UTF-8");
        } catch (IOException e) {
            throw new ExecutorException("Unable to create tmp file");
        }

        try {
            File stdErrFile = new File(input.getWorkDir(), wrapperFile.getName().replace(".sh", ".err"));
            File stdOutFile = new File(input.getWorkDir(), wrapperFile.getName().replace(".sh", ".out"));

            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", wrapperFile.getAbsolutePath());
            processBuilder.directory(input.getWorkDir()).redirectError(stdErrFile).redirectOutput(stdOutFile);
            if (input.getEnvironment() != null) {
                processBuilder.environment().putAll(input.getEnvironment());
            }
            Process process = processBuilder.start();

            // inputs
            if (input.getStdin() != null) {
                IOUtils.write(input.getStdin().toString().getBytes(), process.getOutputStream());
            }

            // do we have a max runtime to consider?
            if (input.getMaxRunTime() > 0) {
                boolean stillRunning = true;
                while (stillRunning) {

                    long currentRunTime = (System.currentTimeMillis() - processStartTime) / 1000;

                    // check timeout
                    if (currentRunTime > input.getMaxRunTime()) {
                        process.destroy();
                        delayedError = "Process timed out";
                    }

                    // sleep
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // ignore
                    }

                    // now check the process
                    try {
                        output.setExitCode(process.exitValue());
                        // can't get here until process has finished
                        stillRunning = false;
                    } catch (IllegalThreadStateException e) {
                        // ignore
                    }
                }
            }

            output.setExitCode(process.waitFor());
            output.getStderr().append(FileUtils.readFileToString(stdErrFile));
            output.getStdout().append(FileUtils.readFileToString(stdOutFile));
            output.setEndDate(new Date());

            // clean up
            if (output.getExitCode() == 0) {
                wrapperFile.delete();
                stdErrFile.delete();
                stdOutFile.delete();
            }

        } catch (InterruptedException e) {
            delayedError = e.getMessage();
        } catch (IOException ioe) {
            throw new ExecutorException("Process error: " + ioe.getMessage());
        }

        if (delayedError != null) {
            throw new ExecutorException(delayedError);
        }

        return output;
    }

    public CommandOutput execute(CommandInput input, File... sources) throws ExecutorException {
        return execute(input, null, sources);
    }

    public CommandOutput execute(CommandInput input) throws ExecutorException {
        return execute(input, null, (File[]) null);
    }
}
