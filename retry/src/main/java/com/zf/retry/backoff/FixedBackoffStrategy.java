package com.zf.retry.backoff;

import java.time.Duration;

public class FixedBackoffStrategy implements BackoffStrategy {

    @Override
    public long getMillisToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
        return delayBetweenAttempts.toMillis();
    }
}
