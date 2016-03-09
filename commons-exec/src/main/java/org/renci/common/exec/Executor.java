package org.renci.common.exec;

import java.io.File;
import java.util.Map;

/**
 * 
 * @author jdr0887
 * 
 */
public interface Executor {

    /**
     * 
     * @param input
     * @return
     * @throws ExecutorException
     */
    public CommandOutput execute(CommandInput input) throws ExecutorException;

    /**
     * 
     * @param input
     * @param substitutionMap
     * @param sources
     * @return
     * @throws ExecutorException
     */
    public CommandOutput execute(CommandInput input, Map<String, String> substitutionMap, File... sources)
            throws ExecutorException;

}
