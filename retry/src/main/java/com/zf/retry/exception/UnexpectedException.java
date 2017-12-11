package com.zf.retry.exception;

/**
 * This exception represents when a call throws an exception that was not specified as one to retry on in the
 * RetryConfig.
 * @author zhufeng
 */
public class UnexpectedException extends RetryException {

    private Throwable cause;

    protected UnexpectedException() {
        super();
    }

    protected UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public UnexpectedException(Throwable cause) {
        super();
        this.setCause(cause);
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
