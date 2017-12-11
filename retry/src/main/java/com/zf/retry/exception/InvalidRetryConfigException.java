package com.zf.retry.exception;

public class InvalidRetryConfigException extends RetryException {

    public InvalidRetryConfigException(String message) {
        super(message);
    }
}
