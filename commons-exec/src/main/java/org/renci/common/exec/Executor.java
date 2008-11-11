package org.renci.common.exec;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * This is really a wrapper for running cmdline applications locally
 * 
 * Requires jdk1.5 or greater
 * 
 * @author rynge, jdr0887
 */
public class Executor {

	// the directory where to run the command from
	private File workDir;

	// the shell environment
    private Map<String, String> environment;

	// the command to run
    private String command;

	// exit value
    private int exitCode;

	// stdout
    private String stdout;

	// stderr
    private String stderr;

	/**
	 * 
	 * @param command
	 *            command to run
	 */
	public Executor(String command) {
		this.command = command;
		environment = new HashMap<String, String>();
	}

    /**
     * 
     * @param command
     * @param workDir
     */
    public Executor(String command, File workDir) {
        this.command = command;
        this.workDir = workDir;
        environment = new HashMap<String, String>();
    }


	/**
	 * run the command
	 * 
	 * @return exit code
	 * @throws ExecuteException
	 */
	public int execute() throws ExecutorException {

		// create a shell script with the command line in it
		StringBuffer wrapperContents = new StringBuffer("#!/bin/sh -e\n");
		wrapperContents.append("if [ -e ~/.biorc ]; then . ~/.biorc; fi")
				.append("\n");
		wrapperContents.append("cd " + workDir.getAbsolutePath()).append("\n");
		wrapperContents.append(command).append("\n");
		File wrapperFile = null;

		// create the executable
		try {
			wrapperFile = File.createTempFile("shellwrapper-", ".sh", workDir);
			FileUtils.writeStringToFile(wrapperFile,
					wrapperContents.toString(), "UTF-8");
		} catch (IOException e) {
			throw new ExecutorException("Unable to create tmp file");
		}

		// chmod the executable
		try {
			Process chmodProcess = new ProcessBuilder("/bin/chmod", "755",
					wrapperFile.getAbsolutePath()).start();
			chmodProcess.waitFor();
		} catch (IOException e1) {
			throw new ExecutorException(e1.getMessage());
		} catch (InterruptedException e1) {
			throw new ExecutorException(e1.getMessage());
		}

		// run it
		try {
			ProcessBuilder pb = new ProcessBuilder(wrapperFile
					.getAbsolutePath());
			pb.directory(workDir);
			Map<String, String> env = pb.environment();
			env.putAll(environment);
			Process process = pb.start();

			this.exitCode = process.waitFor();
            this.stdout = IOUtils.toString(process.getInputStream());
            this.stderr = IOUtils.toString(process.getErrorStream());

		} catch (IOException e) {
			throw new ExecutorException(e.getMessage());
		} catch (InterruptedException e) {
			throw new ExecutorException(e.getMessage());
		}

		// clean up
		wrapperFile.delete();
		return exitCode;
	}

	public void setEnvironment(Map<String, String> environment) {
		this.environment = environment;
	}

	/**
	 * sets the location of the workdir
	 * 
	 * @param workDir
	 */
	public void setWorkDir(File workDir) {
		this.workDir = workDir;
	}

	/**
	 * 
	 * @return the exit code
	 */
	public int getExitCode() {
		return exitCode;
	}

	/**
	 * 
	 * @return the stdout
	 */
	public String getStdout() {
		return stdout;
	}

	/**
	 * 
	 * @return the stderr
	 */
	public String getStderr() {
		return stderr;
	}

}
