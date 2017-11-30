package com.zf.retry.exception;

import com.zf.retry.CallResults;

/**
 * This exception represents a call execution that never succeeded after exhausting all retries.
 */
public class RetriesExhaustedException extends Retry4jException {

    private CallResults results;

    public RetriesExhaustedException(CallResults results) {
        super();
        this.results = results;
    }

    public RetriesExhaustedException(String message, CallResults results) {
        super(message);
        this.results = results;
    }

    public CallResults getCallResults() {
        return results;
    }
}
