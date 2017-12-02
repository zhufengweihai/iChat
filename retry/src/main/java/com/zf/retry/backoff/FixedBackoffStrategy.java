package com.zf.retry.backoff;

public class FixedBackoffStrategy implements BackoffStrategy {

    @Override
    public long getMillisToWait(int numberOfTriesFailed, long delayBetweenAttempts) {
        return delayBetweenAttempts;
    }
}
