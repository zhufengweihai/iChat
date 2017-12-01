package com.zf.retry;


import com.zf.retry.config.RetryConfig;
import com.zf.retry.config.RetryConfigBuilder;
import com.zf.retry.exception.RetriesExhaustedException;
import com.zf.retry.exception.UnexpectedException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CallExecutor<T> {
    private RetryConfig config;
    private RetryListener<T> retryListener;
    private Exception lastKnownExceptionThatCausedRetry;


    public CallExecutor() {
        this(new RetryConfigBuilder().fixedBackoff5Tries10Sec().build());
    }

    public CallExecutor(RetryConfig config) {
        this.config = config;
    }

    public CallResults<T> execute(Callable<T> callable) throws RetriesExhaustedException, UnexpectedException {
        CallResults<T> results = new CallResults<>();
        results.setStartTime(System.currentTimeMillis());
        int maxTries = config.getMaxNumberOfTries();
        T result = null;
        int tries = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<T> futureTask = new FutureTask<T>(callable);
        for (; tries < maxTries && result == null; tries++) {
            result = tryCall(executor, futureTask);
            if (result == null) {
                handleRetry(results, tries + 1);
            }
        }
        executor.shutdown();
        refreshRetryResults(results, result != null, tries);
        results.setEndTime(System.currentTimeMillis());
        postExecutionCleanup(results, result);
        return results;
    }

    public void executeAsync(Callable<T> callable) {
        new Thread(new FutureTask<>(() -> {
            CallResults<T> results = new CallResults<>();
            results.setStartTime(System.currentTimeMillis());
            int maxTries = config.getMaxNumberOfTries();
            T result = null;
            int tries = 0;
            for (; tries < maxTries && result == null; tries++) {
                result = tryCall(callable);
                if (result == null) {
                    handleRetry(results, tries + 1);
                }
            }
            refreshRetryResults(results, result != null, tries);
            results.setEndTime(System.currentTimeMillis());
            postExecutionCleanup(results, result);
            return results;
        })).start();
    }

    private void postExecutionCleanup(CallResults<T> results, T result) {
        if (result == null) {
            if (null != retryListener) {
                retryListener.onFailure(results);
            } else {
                throw new RetriesExhaustedException("Call failed", results);
            }
        } else {
            results.setResult(result);
            if (null != retryListener) {
                retryListener.onSuccess(results);
            }
        }

        if (null != retryListener) {
            retryListener.onCompletion(results);
        }
    }

    private T tryCall(ExecutorService executor, FutureTask<T> futureTask) throws UnexpectedException {
        try {
            executor.execute(futureTask);
            return futureTask.get();
        } catch (Exception e) {
            if (shouldThrowException(e)) {
                throw new UnexpectedException(e);
            }
            lastKnownExceptionThatCausedRetry = e;
            return null;
        }
    }

    private T tryCall(Callable<T> task) throws UnexpectedException {
        try {
            return task.call();
        } catch (Exception e) {
            if (shouldThrowException(e)) {
                throw new UnexpectedException(e);
            }
            lastKnownExceptionThatCausedRetry = e;
        }
        return null;
    }

    private void handleRetry(CallResults<T> results, int tries) {
        refreshRetryResults(results, false, tries);
        if (null != retryListener) {
            retryListener.immediatelyAfterFailedTry(results);
        }
        sleep(config.getDelayBetweenRetries(), tries);
        if (null != retryListener) {
            retryListener.immediatelyBeforeNextTry(results);
        }
    }

    private void refreshRetryResults(CallResults<T> results, boolean success, int tries) {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - results.getStartTime();
        results.setTotalTries(tries);
        results.setTotalElapsedDuration(elapsed);
        results.setSuccessful(success);
        results.setLastExceptionThatCausedRetry(lastKnownExceptionThatCausedRetry);
    }

    private void sleep(long millis, int tries) {
        long millisToSleep = config.getBackoffStrategy().getMillisToWait(tries, millis);
        try {
            TimeUnit.MILLISECONDS.sleep(millisToSleep);
        } catch (InterruptedException ignored) {
        }
    }

    private boolean shouldThrowException(Exception e) {
        if (this.config.isRetryOnAnyException()) {
            return false;
        }
        for (Class<? extends Exception> exceptionInSet : this.config.getRetryOnSpecificExceptions()) {
            if (exceptionInSet.isAssignableFrom(e.getClass())) {
                return false;
            }
        }
        for (Class<? extends Exception> exceptionInSet : this.config.getRetryOnAnyExceptionExcluding()) {
            if (!exceptionInSet.isAssignableFrom(e.getClass())) {
                return false;
            }
        }
        return true;
    }

    public void registerRetryListener(RetryListener<T> listener) {
        this.retryListener = listener;
    }
}
