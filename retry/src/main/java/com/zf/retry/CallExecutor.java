package com.zf.retry;


import com.zf.retry.config.RetryConfig;
import com.zf.retry.config.RetryConfigBuilder;
import com.zf.retry.exception.RetriesExhaustedException;
import com.zf.retry.exception.UnexpectedException;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class CallExecutor<T> {
    private RetryConfig config;
    private RetryListener<T> retryListener;
    private Exception lastKnownExceptionThatCausedRetry;
    private ExecutorService executorService = null;

    public CallExecutor(ExecutorService executorService) {
        this(new RetryConfigBuilder().fixedBackoff5Tries10Sec().build(), executorService);
    }

    public CallExecutor(RetryConfig config, ExecutorService executorService) {
        this.config = config;
        this.executorService = executorService;
    }

    public CallResult<T> execute(Callable<T> callable) throws RetriesExhaustedException, UnexpectedException {
        CallResult<T> results = new CallResult<>();
        results.setStartTime(System.currentTimeMillis());
        int maxTries = config.getMaxNumberOfTries();
        T result = null;
        int tries = 0;
        FutureTask<T> futureTask = new FutureTask<T>(callable);
        for (; tries < maxTries && result == null; tries++) {
            result = tryCall(executorService, futureTask);
            if (result == null) {
                handleRetry(results, tries + 1);
            }
        }
        refreshRetryResults(results, result != null, tries);
        postExecutionCleanup(results, result);
        return results;
    }

    public void executeAsyncWithResult(Callable<T> callable) {
        executorService.submit(() -> {
            CallResult<T> results = new CallResult<>();
            results.setStartTime(System.currentTimeMillis());
            int maxTries = config.getMaxNumberOfTries();
            T result = null;
            int tries = 0;
            for (; tries < maxTries && result == null; tries++) {
                result = tryCallWithResult(callable);
                if (result == null) {
                    handleRetry(results, tries + 1);
                }
            }
            refreshRetryResults(results, result != null, tries);
            EventBus.getDefault().post(new RetryCallResultMessage(results));
            postExecutionCleanup(results, result);
            return results;
        });
    }

    public void executeAsync(Callable task) {
        executorService.execute(() -> {
            CallResult<T> results = new CallResult<>();
            results.setStartTime(System.currentTimeMillis());
            int maxTries = config.getMaxNumberOfTries();
            boolean result = false;
            int tries = 0;
            for (; tries < maxTries && !result; tries++) {
                result = tryCall(task);
            }
        });
    }

    private void postExecutionCleanup(CallResult<T> results, T result) {
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

    private T tryCallWithResult(Callable<T> task) throws UnexpectedException {
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

    private boolean tryCall(Callable task) {
        try {
            task.call();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void handleRetry(CallResult<T> results, int tries) {
        refreshRetryResults(results, false, tries);
        if (null != retryListener) {
            retryListener.immediatelyAfterFailedTry(results);
        }
        sleep(config.getDelayBetweenRetries(), tries);
        if (null != retryListener) {
            retryListener.immediatelyBeforeNextTry(results);
        }
    }

    private void refreshRetryResults(CallResult<T> results, boolean success, int tries) {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - results.getStartTime();
        results.setTotalTries(tries);
        results.setTotalElapsedDuration(elapsed);
        results.setSuccessful(success);
        results.setLastExceptionThatCausedRetry(lastKnownExceptionThatCausedRetry);
        results.setEndTime(System.currentTimeMillis());
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

//    public void registerRetryListener(RetryListener<T> listener) {
//        this.retryListener = listener;
//    }
}
