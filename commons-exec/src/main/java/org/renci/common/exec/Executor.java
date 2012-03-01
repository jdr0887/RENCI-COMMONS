package org.renci.common.exec;

import java.io.File;

/**
 * 
 * @author jdr0887
 *
 */
public interface Executor {

    /**
     * run the command
     * 
     * @return exit code
     * @throws ExecutorException
     */
    public CommandOutput execute(CommandInput input) throws ExecutorException;

    /**
     * 
     * @param input
     * @param sources
     * @return
     * @throws ExecutorException
     */
    public CommandOutput execute(CommandInput input, File... sources) throws ExecutorException;

}
