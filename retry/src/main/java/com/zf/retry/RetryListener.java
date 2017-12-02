package com.zf.retry;

public interface RetryListener<T> {
    default void immediatelyAfterFailedTry(CallResult<T> results) {
    }

    default void immediatelyBeforeNextTry(CallResult<T> results) {
    }

    default void onCompletion(CallResult<T> results) {
    }

    default void onFailure(CallResult<T> results) {
    }

    default void onSuccess(CallResult<T> results) {
    }
}
