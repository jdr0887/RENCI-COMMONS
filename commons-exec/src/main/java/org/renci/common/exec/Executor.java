package org.renci.common.exec;

import java.io.File;
import java.util.Map;

/**
 * 
 * @author jdr0887
 * 
 */
public interface Executor {

    public CommandOutput execute(CommandInput input) throws ExecutorException;

    public CommandOutput execute(CommandInput input, File... sources) throws ExecutorException;

    public CommandOutput execute(CommandInput input, Map<String, String> substitutionMap, File... sources)
            throws ExecutorException;

}
