package com.zf.retry.config;


import com.zf.retry.backoff.BackoffStrategy;

import java.util.HashSet;
import java.util.Set;

public class RetryConfig {
    private Boolean retryOnAnyException = false;
    private Set<Class<? extends Exception>> retryOnSpecificExceptions = new HashSet<>();
    private Set<Class<? extends Exception>> retryOnAnyExceptionExcluding = new HashSet<>();
    private Integer maxNumberOfTries;
    private long delayBetweenRetries;
    private BackoffStrategy backoffStrategy;

    public Boolean isRetryOnAnyException() {
        return retryOnAnyException;
    }

    public void setRetryOnAnyException(boolean retryOnAnyException) {
        this.retryOnAnyException = retryOnAnyException;
    }

    public Set<Class<? extends Exception>> getRetryOnSpecificExceptions() {
        return retryOnSpecificExceptions;
    }

    public void setRetryOnSpecificExceptions(Set<Class<? extends Exception>> retryOnSpecificExceptions) {
        this.retryOnSpecificExceptions = retryOnSpecificExceptions;
    }

    public Set<Class<? extends Exception>> getRetryOnAnyExceptionExcluding() {
        return retryOnAnyExceptionExcluding;
    }

    public void setRetryOnAnyExceptionExcluding(Set<Class<? extends Exception>> retryOnAnyExceptionExcluding) {
        this.retryOnAnyExceptionExcluding = retryOnAnyExceptionExcluding;
    }

    public Integer getMaxNumberOfTries() {
        return maxNumberOfTries;
    }

    public void setMaxNumberOfTries(int maxNumberOfTries) {
        if (maxNumberOfTries < 0) {
            throw new IllegalArgumentException("Must be a non-negative number.");
        }

        this.maxNumberOfTries = maxNumberOfTries;
    }

    public long getDelayBetweenRetries() {
        return delayBetweenRetries;
    }

    public void setDelayBetweenRetries(long delayBetweenRetries) {
        this.delayBetweenRetries = delayBetweenRetries;
    }

    public BackoffStrategy getBackoffStrategy() {
        return backoffStrategy;
    }

    public void setBackoffStrategy(BackoffStrategy backoffStrategy) {
        this.backoffStrategy = backoffStrategy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RetryConfig{");
        sb.append("retryOnAnyException=").append(retryOnAnyException);
        sb.append(", retryOnSpecificExceptions=").append(retryOnSpecificExceptions);
        sb.append(", maxNumberOfTries=").append(maxNumberOfTries);
        sb.append(", delayBetweenRetries=").append(delayBetweenRetries);
        sb.append(", backoffStrategy=").append(backoffStrategy);
        sb.append('}');
        return sb.toString();
    }
}
