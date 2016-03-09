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

    static {
        System.setProperty("org.apache.commons.exec.lenient", "false");
        System.setProperty("org.apache.commons.exec.debug", "true");
    }

    @Test
    public void testBinDate() {
        Executor executor = BashExecutor.getInstance();
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
    public void testStderr() {
        Executor executor = BashExecutor.getInstance();
        CommandInput input = new CommandInput("cd asdfasd", new File("/tmp"));
        try {
            CommandOutput output = executor.execute(input);
            assertTrue(output.getStderr().indexOf("No such file or directory") != -1);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testNoExitImmediately() {
        Executor executor = BashExecutor.getInstance();
        CommandInput input = new CommandInput("cd asdfasd; /bin/date", new File("/tmp"), Boolean.FALSE);
        try {
            CommandOutput output = executor.execute(input);
            assertTrue(output.getStderr().indexOf("No such file or directory") != -1);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testStdout() {
        Executor executor = BashExecutor.getInstance();
        CommandInput input = new CommandInput("echo foo", new File("/tmp"));
        try {
            CommandOutput output = executor.execute(input);
            assertTrue(output.getStdout().indexOf("foo") != -1);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBinTrue() {
        Executor executor = BashExecutor.getInstance();
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
            CommandOutput output = BashExecutor.getInstance().execute(input);
            exitCode = output.getExitCode();
            assertFalse(exitCode == 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testInputRedirect() {
        Executor executor = BashExecutor.getInstance();
        CommandInput input = new CommandInput("/bin/true < /dev/null", new File("/tmp"));
        try {
            executor.execute(input);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBigOutput() {
        Executor executor = BashExecutor.getInstance();
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
    public void testSleep() {

        Executor executor = BashExecutor.getInstance();
        CommandInput input = new CommandInput("echo asdf; sleep 5; echo zxcv", new File("/tmp"));
        try {
            CommandOutput output = executor.execute(input);
            assertTrue(output.getStdout().length() > 0);
            System.out.println(output.getStdout());
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
        Map<String, String> env = new HashMap<String, String>();
        env.put("TESTVARIABLE", "somevalue");
        input.setEnvironment(env);
        File userHome = new File(System.getProperty("user.home"));
        try {
            Executor executor = BashExecutor.getInstance();
            CommandOutput output = executor.execute(input, null, new File(userHome, ".bashrc"));
            exitCode = output.getExitCode();
            assertTrue(exitCode == 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
