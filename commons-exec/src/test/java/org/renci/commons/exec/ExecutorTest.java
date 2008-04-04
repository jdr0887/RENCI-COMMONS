package org.renci.commons.exec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.renci.common.exec.Executor;
import org.renci.common.exec.ExecutorException;

/**
 * 
 * @author rynge
 */
public class ExecutorTest {

	@Test
    public void testBinDate() {
        Executor cmd = new Executor("/bin/date");
        cmd.setWorkDir(new File("/tmp"));
        try {
            cmd.execute();
            System.out.println("Stdout: " + cmd.getStdout());
            System.out.println("Stderr: " + cmd.getStderr());
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

	@Test
    public void testBinTrue() {
    	Executor cmd = new Executor("/bin/true");
        int exitCode = -1;
        try {
            cmd.execute();
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        exitCode = cmd.getExitCode();
        System.out.println("Exit code: " + exitCode);
        assertEquals(exitCode, 0);
    }

	@Test
    public void testBinFalse() {
    	Executor cmd = new Executor("/bin/false");
        int exitCode = -1;
        try {
            cmd.execute();
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        exitCode = cmd.getExitCode();
        System.out.println("Exit code: " + exitCode);
        assertFalse(exitCode == 0);
    }
    
	@Test
    public void testInputRedirect() {
    	Executor cmd = new Executor("/bin/true </dev/null");
        try {
            cmd.execute();
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

	@Test
    public void testbigOutput() {
    	Executor cmd = new Executor("find /usr/bin");
        try {
            cmd.execute();
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        System.out.println("Stdout size:" + cmd.getStdout().length());
        System.out.println("Stderr size:" + cmd.getStderr().length());
    }

	@Test
    public void testEnv() {
        int exitCode = -1;
        Map env = new HashMap();
        env.put("TESTVARIABLE", "somevalue");
        Executor cmd = new Executor("env | grep TESTVARIABLE");
        cmd.setEnvironment(env);
        try {
            cmd.execute();
        } catch (ExecutorException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        exitCode = cmd.getExitCode();
        System.out.println("Exit code: " + exitCode);
        System.out.println("Stdout: " + cmd.getStdout());
        System.out.println("Stderr: " + cmd.getStderr());
    }

}
