package org.renci.common.exec;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jdr0887
 * 
 */
public class BashExecutor extends AbstractExecutor {

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

    /**
     * run the command
     * 
     * @return exit code
     * @throws ShellExecutorException
     */
    public CommandOutput execute(CommandInput input) throws ExecutorException {
        logger.debug("ENTERING run(ShellInput)");
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        BufferedOutputStream stdinStream = null;
        StreamGobbler stdoutGobbler = null;
        StreamGobbler stderrGobbler = null;
        int exitCode = -1;
        File wrapperFile = null;
        String wrapperContents = null;
        long processStartTime = System.currentTimeMillis();
        String delayedError = null;

        CommandOutput output = new CommandOutput();
        output.setStartDate(new Date());

        // create a shell script with the command line in it
        wrapperContents = String.format("#!/bin/bash -e%n%s%ncd %s%n%s%n", input.getSourceFile() == null ? "" : ". " + input
                .getSourceFile().getAbsolutePath(), input.getWorkDir().getAbsolutePath(), input.getCommand());
        try {
            wrapperFile = File.createTempFile("shellwrapper-", ".sh", input.getWorkDir());
            FileUtils.writeStringToFile(wrapperFile, wrapperContents, "UTF-8");
        } catch (IOException e) {
            throw new ExecutorException("Unable to create tmp file");
        }

        // chmod the temp command file
        try {
            process = runtime.exec("/bin/chmod 755 " + wrapperFile.getAbsolutePath());
            process.waitFor();
        } catch (IOException ioe) {
            throw new ExecutorException("chmod problem: " + ioe.getMessage());
        } catch (InterruptedException e) {
            // ignore
        }

        try {
            String[] env = null;
            if (input.getEnvironment() != null) {
                env = environmentToArray(input.getEnvironment());
            }
            logger.info("running command: {}", input.getCommand());
            process = runtime.exec(wrapperFile.getAbsolutePath(), env, input.getWorkDir());

            // outputs
            stdoutGobbler = new StreamGobbler(process.getInputStream());
            stdoutGobbler.start();
            stderrGobbler = new StreamGobbler(process.getErrorStream());
            stderrGobbler.start();

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

            try {
                exitCode = process.waitFor();
                output.setExitCode(exitCode);
            } catch (InterruptedException exp) {
                delayedError = exp.getMessage();
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
                output.setStdout(stdoutGobbler.getOutput());
            }
            if (stderrGobbler != null) {
                output.setStderr(stderrGobbler.getOutput());
            }

            try {

                if (stdinStream != null) {
                    stdinStream.close();
                }

                if (stdoutGobbler != null) {
                    stdoutGobbler.close();
                }
                if (stderrGobbler != null) {
                    stderrGobbler.close();
                }

                // close streams
                if (process != null) {
                    InputStream istr = process.getInputStream();
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
        output.setEndDate(new Date());

        if (delayedError != null) {
            throw new ExecutorException(delayedError);
        }

        return output;
    }
}
