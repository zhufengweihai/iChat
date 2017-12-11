package com.zf.retry.config;

import com.zf.retry.backoff.BackoffStrategy;

import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhufeng
 */
@Getter
@Setter
@Builder
public class RetryConfig {
    private final boolean retryOnAnyException;
    private final Set<Class<? extends Exception>> retryOnSpecificExceptions = null;
    private final Set<Class<? extends Exception>> retryOnAnyExceptionExcluding = null;
    private final int maxNumberOfTries;
    private final long tryTimeout;
    private final long delayBetweenRetries;

    public static RetryConfigBuilder defaults() {
        return RetryConfig.builder().retryOnAnyException(true).maxNumberOfTries(3).tryTimeout(5000).delayBetweenRetries(1000);
    }
}
