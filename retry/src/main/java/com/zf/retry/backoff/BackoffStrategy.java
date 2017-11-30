package com.zf.retry.backoff;

public interface BackoffStrategy {
    long getMillisToWait(int numberOfTriesFailed, long delayBetweenAttempts);
}
