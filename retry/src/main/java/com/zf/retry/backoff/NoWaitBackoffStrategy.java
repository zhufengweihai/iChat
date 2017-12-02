package com.zf.retry.backoff;

public class NoWaitBackoffStrategy implements BackoffStrategy {

    @Override
    public long getMillisToWait(int numberOfTriesFailed, long delayBetweenAttempts) {
        return 0;
    }
}
