package org.renci.common.exec;

public interface Executor {

    /**
     * run the command
     * 
     * @return exit code
     * @throws ExecutorException
     */
    public Output execute(Input input) throws ExecutorException;

}
