package com.zf.retry.exception;

public class RetryException extends Exception {
    public RetryException(String message) {
        super(message);
    }

    public RetryException() {
        super();
    }
}
