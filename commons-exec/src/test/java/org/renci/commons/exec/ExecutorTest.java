package org.renci.commons.exec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.renci.common.exec.BashExecutor;
import org.renci.common.exec.CommandInput;
import org.renci.common.exec.CommandOutput;
import org.renci.common.exec.Executor;
import org.renci.common.exec.ExecutorException;

/**
 * 
 * @author rynge
 */
public class ExecutorTest {

    private final Executor executor = BashExecutor.getInstance();

    @Test
    public void testBinDate() {
        CommandInput input = new CommandInput("/bin/date", new File("/tmp"));
        try {
            CommandOutput output = executor.execute(input);
            Calendar c = Calendar.getInstance();
            assertTrue(output.getStdout().indexOf(c.get(Calendar.YEAR) + "") != -1);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBinTrue() {
        CommandInput input = new CommandInput("/bin/true", new File("/tmp"));
        int exitCode = -1;
        try {
            CommandOutput output = executor.execute(input);
            exitCode = output.getExitCode();
            assertEquals(exitCode, 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBinFalse() {
        CommandInput input = new CommandInput("/bin/false", new File("/tmp"));
        int exitCode = -1;
        try {
            CommandOutput output = executor.execute(input);
            exitCode = output.getExitCode();
            assertFalse(exitCode == 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testInputRedirect() {
        CommandInput input = new CommandInput("/bin/true </dev/null", new File("/tmp"));
        try {
            executor.execute(input);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBigOutput() {
        CommandInput input = new CommandInput("find /usr/bin", new File("/tmp"));
        try {
            CommandOutput output = executor.execute(input);
            assertTrue(output.getStdout().length() > 0);
            assertTrue(output.getStderr().length() == 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testEnv() {
        int exitCode = -1;
        CommandInput input = new CommandInput("env | grep TESTVARIABLE", new File("/tmp"));
        Map env = new HashMap();
        env.put("TESTVARIABLE", "somevalue");
        input.setEnvironment(env);
        File userHome = new File(System.getProperty("user.home"));
        try {
            CommandOutput output = executor.execute(input, new File(userHome, ".bashrc"));
            exitCode = output.getExitCode();
            assertTrue(exitCode == 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
