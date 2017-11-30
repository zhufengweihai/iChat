package com.zf.retry.exception;

public class InvalidRetryConfigException extends Retry4jException {

    public InvalidRetryConfigException(String message) {
        super(message);
    }
}
