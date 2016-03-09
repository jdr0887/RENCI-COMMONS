package org.renci.common.exec;

public class ExecutorException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ExecutorException() {
        super();
    }

    public ExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutorException(String message) {
        super(message);
    }

    public ExecutorException(Throwable cause) {
        super(cause);
    }
}

