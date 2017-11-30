package com.zf.retry.config;


import com.zf.retry.backoff.BackoffStrategy;
import com.zf.retry.backoff.ExponentialBackoffStrategy;
import com.zf.retry.backoff.FibonacciBackoffStrategy;
import com.zf.retry.backoff.FixedBackoffStrategy;
import com.zf.retry.backoff.NoWaitBackoffStrategy;
import com.zf.retry.exception.InvalidRetryConfigException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RetryConfigBuilder {
    private boolean exceptionStrategySpecified;
    private RetryConfig config;
    private boolean validationEnabled;

    public RetryConfigBuilder() {
        this.config = new RetryConfig();
        this.exceptionStrategySpecified = false;
        this.validationEnabled = true;
    }

    public RetryConfigBuilder(boolean validationEnabled) {
        this();
        this.validationEnabled = validationEnabled;
    }

    public boolean isValidationEnabled() {
        return validationEnabled;
    }

    public void setValidationEnabled(boolean validationEnabled) {
        this.validationEnabled = validationEnabled;
    }

    public RetryConfigBuilder retryOnAnyException() {
        validateExceptionStrategyAddition();

        config.setRetryOnAnyException(true);

        exceptionStrategySpecified = true;
        return this;
    }

    public RetryConfigBuilder failOnAnyException() {
        validateExceptionStrategyAddition();

        config.setRetryOnAnyException(false);
        config.setRetryOnSpecificExceptions(new HashSet<>());

        exceptionStrategySpecified = true;
        return this;
    }

    @SafeVarargs
    public final RetryConfigBuilder retryOnSpecificExceptions(Class<? extends Exception>... exceptions) {
        validateExceptionStrategyAddition();

        Set<Class<? extends Exception>> setOfExceptions = new HashSet<>(Arrays.asList(exceptions));
        config.setRetryOnSpecificExceptions(setOfExceptions);

        exceptionStrategySpecified = true;
        return this;
    }

    @SafeVarargs
    public final RetryConfigBuilder retryOnAnyExceptionExcluding(Class<? extends Exception>... exceptions) {
        validateExceptionStrategyAddition();

        Set<Class<? extends Exception>> setOfExceptions = new HashSet<>(Arrays.asList(exceptions));
        config.setRetryOnAnyExceptionExcluding(setOfExceptions);

        exceptionStrategySpecified = true;
        return this;
    }

    public RetryConfigBuilder withMaxNumberOfTries(int max) {
        config.setMaxNumberOfTries(max);
        return this;
    }

    public RetryConfigBuilder withDelayBetweenTries(long duration) {
        config.setDelayBetweenRetries(duration);
        return this;
    }

    public RetryConfigBuilder withDelayBetweenTries(long amount, TimeUnit time) {
        config.setDelayBetweenRetries(time.toMillis(amount));
        return this;
    }

    public RetryConfigBuilder withBackoffStrategy(BackoffStrategy backoffStrategy) {
        validateBackoffStrategyAddition();
        config.setBackoffStrategy(backoffStrategy);
        return this;
    }

    public RetryConfigBuilder withFixedBackoff() {
        validateBackoffStrategyAddition();
        config.setBackoffStrategy(new FixedBackoffStrategy());
        return this;
    }

    public RetryConfigBuilder withExponentialBackoff() {
        validateBackoffStrategyAddition();
        config.setBackoffStrategy(new ExponentialBackoffStrategy());
        return this;
    }

    public RetryConfigBuilder withFibonacciBackoff() {
        validateBackoffStrategyAddition();
        config.setBackoffStrategy(new FibonacciBackoffStrategy());
        return this;
    }

    public RetryConfigBuilder withNoWaitBackoff() {
        validateBackoffStrategyAddition();
        config.setBackoffStrategy(new NoWaitBackoffStrategy());
        return this;
    }

    public RetryConfigBuilder withRandomBackoff() {
        validateBackoffStrategyAddition();
        config.setBackoffStrategy(new RandomBackoffStrategy());
        return this;
    }

    public RetryConfigBuilder withRandomExponentialBackoff() {
        validateBackoffStrategyAddition();
        config.setBackoffStrategy(new RandomExponentialBackoffStrategy());
        return this;
    }

    public RetryConfig build() {
        validateConfig();

        return config;
    }

    private void validateConfig() {
        if (!validationEnabled) {
            return;
        }

        if (null == config.getBackoffStrategy()) {
            throw new InvalidRetryConfigException("Retry config must specify a backoff strategy!");
        }

        if (null == config.getMaxNumberOfTries()) {
            throw new InvalidRetryConfigException("Retry config must specify a maximum number of tries!");
        }

        if (config.getDelayBetweenRetries() <= 0) {
            throw new InvalidRetryConfigException("Retry config must specify the delay between retries!");
        }
    }

    private void validateBackoffStrategyAddition() {
        if (!validationEnabled) {
            return;
        }

        if (null != config.getBackoffStrategy()) {
            throw new InvalidRetryConfigException("Retry config cannot specify more than one backoff strategy!");
        }
    }

    private void validateExceptionStrategyAddition() {
        if (!validationEnabled) {
            return;
        }

        if (exceptionStrategySpecified) {
            throw new InvalidRetryConfigException("Retry config cannot specify more than one exception strategy!");
        }
    }

    public RetryConfigBuilder fixedBackoff5Tries10Sec() {
        return new RetryConfigBuilder().retryOnAnyException().withMaxNumberOfTries(5).withDelayBetweenTries(10,
                TimeUnit.SECONDS).withFixedBackoff();
    }

    public RetryConfigBuilder exponentialBackoff5Tries5Sec() {
        return new RetryConfigBuilder().retryOnAnyException().withMaxNumberOfTries(5).withDelayBetweenTries(5,
                TimeUnit.SECONDS).withExponentialBackoff();
    }

    public RetryConfigBuilder fiboBackoff7Tries5Sec() {
        return new RetryConfigBuilder().retryOnAnyException().withMaxNumberOfTries(7).withDelayBetweenTries(5,
                TimeUnit.SECONDS).withFibonacciBackoff();
    }

    public RetryConfigBuilder randomExpBackoff10Tries60Sec() {
        return new RetryConfigBuilder().retryOnAnyException().withMaxNumberOfTries(10).withDelayBetweenTries(60,
                TimeUnit.SECONDS).withRandomExponentialBackoff();
    }

}
