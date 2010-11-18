package org.renci.commons.exec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.renci.common.exec.Executor;
import org.renci.common.exec.ExecutorException;
import org.renci.common.exec.Input;
import org.renci.common.exec.Output;

/**
 * 
 * @author rynge
 */
public class ExecutorTest {

    @Test
    public void testBinDate() {
        Executor cmd = Executor.getInstance();
        Input input = new Input("/bin/date", new File("/tmp"));
        try {
            Output output = cmd.run(input);
            Calendar c = Calendar.getInstance();
            assertTrue(output.getStdout().indexOf(c.get(Calendar.YEAR) + "") != -1);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBinTrue() {
        Executor cmd = Executor.getInstance();
        Input input = new Input("/bin/true", new File("/tmp"));
        int exitCode = -1;
        try {
            Output output = cmd.run(input);
            exitCode = output.getExitCode();
            assertEquals(exitCode, 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBinFalse() {
        Executor cmd = Executor.getInstance();
        Input input = new Input("/bin/false", new File("/tmp"));
        int exitCode = -1;
        try {
            Output output = cmd.run(input);
            exitCode = output.getExitCode();
            assertFalse(exitCode == 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testInputRedirect() {
        Executor cmd = Executor.getInstance();
        Input input = new Input("/bin/true </dev/null", new File("/tmp"));
        try {
            cmd.run(input);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void testBigOutput() {
        Executor cmd = Executor.getInstance();
        Input input = new Input("find /usr/bin", new File("/tmp"));
        try {
            Output output = cmd.run(input);
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
        
        Executor cmd = Executor.getInstance();
        Input input = new Input("env | grep TESTVARIABLE", new File("/tmp"));
        Map env = new HashMap();
        env.put("TESTVARIABLE", "somevalue");
        input.setEnvironment(env);
        try {
            Output output = cmd.run(input);
            exitCode = output.getExitCode();
            assertTrue(exitCode == 0);
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
