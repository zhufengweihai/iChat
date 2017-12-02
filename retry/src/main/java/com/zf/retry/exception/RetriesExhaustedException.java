package com.zf.retry.exception;

import com.zf.retry.CallResult;

/**
 * This exception represents a call execution that never succeeded after exhausting all retries.
 */
public class RetriesExhaustedException extends Retry4jException {

    private CallResult results;

    public RetriesExhaustedException(CallResult results) {
        super();
        this.results = results;
    }

    public RetriesExhaustedException(String message, CallResult results) {
        super(message);
        this.results = results;
    }

    public CallResult getCallResults() {
        return results;
    }
}
