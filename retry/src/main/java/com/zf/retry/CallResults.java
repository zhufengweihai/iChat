package com.zf.retry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallResults<T> {
    private long startTime;
    private long endTime;
    private boolean successful;
    private int totalTries;
    private long totalElapsedDuration;
    private T result;
    private Exception lastExceptionThatCausedRetry;
}
