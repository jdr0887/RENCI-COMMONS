package org.renci.common.exec;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jdr0887
 * 
 */
public class BashExecutor implements Executor {

    private final Logger logger = LoggerFactory.getLogger(BashExecutor.class);

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.renci.common.exec.Executor#execute(org.renci.common.exec.CommandInput
     * , java.io.File[])
     */
    public CommandOutput execute(CommandInput input, File... sources) throws ExecutorException {
        logger.debug("ENTERING execute(CommandInput, File...)");
        BufferedOutputStream stdinStream = null;
        int exitCode = -1;
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

        try {
            wrapperFile = File.createTempFile("shellwrapper-", ".sh", input.getWorkDir());

            String wrapperContents;
            if (input.getExitImmediately()) {
                wrapperContents = String.format("#!/bin/bash -e%n%s%ncd %s%n%s%n", sourceFileSB.length() == 0 ? ""
                        : sourceFileSB.toString(), input.getWorkDir().getAbsolutePath(), input.getCommand());
            } else {
                wrapperContents = String.format("#!/bin/bash%n%s%ncd %s%n%s%n", sourceFileSB.length() == 0 ? ""
                        : sourceFileSB.toString(), input.getWorkDir().getAbsolutePath(), input.getCommand());
            }

            logger.debug("wrapperContents: {}", wrapperContents);
            FileUtils.writeStringToFile(wrapperFile, wrapperContents, "UTF-8");
        } catch (IOException e) {
            throw new ExecutorException("Unable to create tmp file");
        }

        // chmod the temp command file
        try {
            Process process = new ProcessBuilder("/bin/chmod", "755", wrapperFile.getAbsolutePath()).start();
            process.waitFor();
        } catch (IOException ioe) {
            throw new ExecutorException("chmod problem: " + ioe.getMessage());
        } catch (InterruptedException e) {
            // ignore
        }

        try {
            File stdErrFile = new File(input.getWorkDir(), wrapperFile.getName().replace(".sh", ".err"));
            File stdOutFile = new File(input.getWorkDir(), wrapperFile.getName().replace(".sh", ".out"));

            ProcessBuilder processBuilder = new ProcessBuilder(wrapperFile.getAbsolutePath());
            processBuilder.directory(input.getWorkDir());
            if (input.getEnvironment() != null) {
                processBuilder.environment().putAll(input.getEnvironment());
            }
            processBuilder.redirectError(stdErrFile);
            processBuilder.redirectOutput(stdOutFile);
            Process process = processBuilder.start();

            // inputs
            if (input.getStdin() != null) {
                stdinStream = new BufferedOutputStream(process.getOutputStream());
                stdinStream.write(input.getStdin().toString().getBytes());
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
                        exitCode = process.exitValue();
                        output.setExitCode(exitCode);
                        // can't get here until process has finished
                        stillRunning = false;
                    } catch (IllegalThreadStateException e) {
                        // ignore
                    }
                }
            }

            exitCode = process.waitFor();
            output.setExitCode(exitCode);
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
        } finally {
            try {
                if (stdinStream != null) {
                    stdinStream.close();
                }
            } catch (IOException exp) {
                // ignore
            }
        }

        if (delayedError != null) {
            throw new ExecutorException(delayedError);
        }

        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.renci.common.exec.Executor#execute(org.renci.common.exec.CommandInput
     * )
     */
    public CommandOutput execute(CommandInput input) throws ExecutorException {
        return execute(input, (File[]) null);
    }
}
