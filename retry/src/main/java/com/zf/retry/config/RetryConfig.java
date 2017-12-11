package com.zf.retry.config;

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

    public static RetryConfigBuilder defaults(int maxNumberOfTries, long tryTimeout, long delayBetweenRetries) {
        return RetryConfig.builder().retryOnAnyException(true).maxNumberOfTries(maxNumberOfTries).tryTimeout
                (tryTimeout).delayBetweenRetries(delayBetweenRetries);
    }

    public static RetryConfigBuilder defaults() {
        return RetryConfig.builder().retryOnAnyException(true).maxNumberOfTries(5000).tryTimeout(3)
                .delayBetweenRetries(5000);
    }
}
