package com.zf.retry;


import com.zf.retry.config.RetryConfig;
import com.zf.retry.config.RetryConfigBuilder;
import com.zf.retry.exception.RetriesExhaustedException;
import com.zf.retry.exception.UnexpectedException;
import com.zf.retry.listener.AfterFailedTryListener;
import com.zf.retry.listener.BeforeNextTryListener;
import com.zf.retry.listener.OnCompletionListener;
import com.zf.retry.listener.OnFailureListener;
import com.zf.retry.listener.OnSuccessListener;
import com.zf.retry.listener.RetryListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CallExecutor<T> {
    private Logger logger = LoggerFactory.getLogger(CallExecutor.class);

    private RetryConfig config;
    private AfterFailedTryListener afterFailedTryListener;
    private BeforeNextTryListener beforeNextTryListener;
    private OnFailureListener onFailureListener;
    private OnSuccessListener onSuccessListener;
    private OnCompletionListener onCompletionListener;
    private ExecutorService executorService;
    private Exception lastKnownExceptionThatCausedRetry;
    private CallResults<T> results = new CallResults<>();

    public CallExecutor() {
        this(new RetryConfigBuilder().fixedBackoff5Tries10Sec().build());
    }

    public CallExecutor(RetryConfig config) {
        this.config = config;
    }

    public CallResults<T> execute(Callable<T> callable) throws RetriesExhaustedException, UnexpectedException {
        logger.trace("Starting retry4j execution with callable {}", config, callable);
        logger.debug("Starting retry4j execution with executor state {}", this);

        long start = System.currentTimeMillis();
        results.setStartTime(start);

        int maxTries = config.getMaxNumberOfTries();
        long millisBetweenTries = config.getDelayBetweenRetries();
        this.results.setCallName(callable.toString());

        T result = null;
        int tries;

        try {
            for (tries = 0; tries < maxTries && result == null; tries++) {
                logger.trace("Retry4j executing callable {}", callable);
                result = tryCall(callable);

                if (result == null) {
                    handleRetry(millisBetweenTries, tries + 1);
                }

                logger.trace("Retry4j retrying for time number {}", tries);
            }

            refreshRetryResults(result != null, tries);
            results.setEndTime(System.currentTimeMillis());

            postExecutionCleanup(callable, maxTries, result);

            logger.debug("Finished retry4j execution in {} ms", results.getTotalElapsedDuration());
            logger.trace("Finished retry4j execution with executor state {}", this);
        } finally {
            if (null != onCompletionListener) {
                onCompletionListener.onCompletion(results);
            }
        }

        return results;
    }

    public void executeAsync(Callable<T> callable) {
        if (null == executorService) {
            executorService = Executors.newFixedThreadPool(10);
        }
        Runnable runnable = () -> this.execute(callable);
        executorService.execute(runnable);
    }

    private void postExecutionCleanup(Callable<T> callable, int maxTries, T result) {
        if (result == null) {
            String failureMsg = String.format(Locale.getDefault(), "Call '%s' failed after %d tries!", callable
                    .toString(), maxTries);
            if (null != onFailureListener) {
                onFailureListener.onFailure(results);
            } else {
                logger.trace("Throwing retries exhausted exception");
                throw new RetriesExhaustedException(failureMsg, results);
            }
        } else {
            results.setResult(result);
            if (null != onSuccessListener) {
                onSuccessListener.onSuccess(results);
            }
        }
    }

    private T tryCall(Callable<T> callable) throws UnexpectedException {
        try {
            return callable.call();
        } catch (Exception e) {
            if (shouldThrowException(e)) {
                logger.trace("Throwing expected exception {}", e);
                throw new UnexpectedException(e);
            }
            lastKnownExceptionThatCausedRetry = e;
            return null;
        }
    }

    private void handleRetry(long millisBetweenTries, int tries) {
        refreshRetryResults(false, tries);

        if (null != afterFailedTryListener) {
            afterFailedTryListener.immediatelyAfterFailedTry(results);
        }

        sleep(millisBetweenTries, tries);

        if (null != beforeNextTryListener) {
            beforeNextTryListener.immediatelyBeforeNextTry(results);
        }
    }

    private void refreshRetryResults(boolean success, int tries) {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - results.getStartTime();
        results.setTotalTries(tries);
        results.setTotalElapsedDuration(elapsed);
        results.setSuccessful(success);
        results.setLastExceptionThatCausedRetry(lastKnownExceptionThatCausedRetry);
    }

    private void sleep(long millis, int tries) {
        long millisToSleep = config.getBackoffStrategy().getMillisToWait(tries, millis);

        logger.trace("Retry4j executor sleeping for {} ms", millisToSleep);
        try {
            TimeUnit.MILLISECONDS.sleep(millisToSleep);
        } catch (InterruptedException ignored) {
        }
    }

    private boolean shouldThrowException(Exception e) {
        //config says to always retry
        if (this.config.isRetryOnAnyException()) {
            return false;
        }

        //config says to retry only on specific exceptions
        for (Class<? extends Exception> exceptionInSet : this.config.getRetryOnSpecificExceptions()) {
            if (exceptionInSet.isAssignableFrom(e.getClass())) {
                return false;
            }
        }

        //config says to retry on all except specific exceptions
        for (Class<? extends Exception> exceptionInSet : this.config.getRetryOnAnyExceptionExcluding()) {
            if (!exceptionInSet.isAssignableFrom(e.getClass())) {
                return false;
            }
        }

        return true;
    }

    public void registerRetryListener(RetryListener listener) {
        if (listener instanceof AfterFailedTryListener) {
            this.afterFailedTryListener = (AfterFailedTryListener) listener;
        } else if (listener instanceof BeforeNextTryListener) {
            this.beforeNextTryListener = (BeforeNextTryListener) listener;
        } else if (listener instanceof OnSuccessListener) {
            this.onSuccessListener = (OnSuccessListener) listener;
        } else if (listener instanceof OnFailureListener) {
            this.onFailureListener = (OnFailureListener) listener;
        } else if (listener instanceof OnCompletionListener) {
            this.onCompletionListener = (OnCompletionListener) listener;
        } else {
            throw new IllegalArgumentException("Tried to register an unrecognized RetryListener!");
        }

        logger.trace("Registered listener on retry4j executor {}", listener);
    }

    public void setConfig(RetryConfig config) {
        logger.trace("Set config on retry4j executor {}", config);
        this.config = config;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CallExecutor{");
        sb.append("config=").append(config);
        sb.append(", afterFailedTryListener=").append(afterFailedTryListener);
        sb.append(", beforeNextTryListener=").append(beforeNextTryListener);
        sb.append(", onFailureListener=").append(onFailureListener);
        sb.append(", onSuccessListener=").append(onSuccessListener);
        sb.append(", executorService=").append(executorService);
        sb.append(", lastKnownExceptionThatCausedRetry=").append(lastKnownExceptionThatCausedRetry);
        sb.append(", results=").append(results);
        sb.append('}');
        return sb.toString();
    }
}
