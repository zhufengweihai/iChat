package com.zf.retry.backoff;

import java.time.Duration;

public class NoWaitBackoffStrategy implements BackoffStrategy {

    @Override
    public long getMillisToWait(int numberOfTriesFailed, Duration delayBetweenAttempts) {
        return 0;
    }
}
