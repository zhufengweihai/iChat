package com.zf.retry.backoff;

/**
 * AKA binary exponential backoff
 */
public class ExponentialBackoffStrategy implements BackoffStrategy {

    @Override
    public long getMillisToWait(int numberOfTriesFailed, long delayBetweenAttempts) {
        double exponentialMultiplier = Math.pow(2.0, numberOfTriesFailed - 1);
        double result = exponentialMultiplier * delayBetweenAttempts;
        return (long) Math.min(result, Long.MAX_VALUE);
    }
}
