package com.zf.retry;

import com.zf.retry.CallResults;

public interface RetryListener<T> {
    default void immediatelyAfterFailedTry(CallResults<T> results) {
    }

    default void immediatelyBeforeNextTry(CallResults<T> results) {
    }

    default void onCompletion(CallResults<T> results) {
    }

    default void onFailure(CallResults<T> results) {
    }

    default void onSuccess(CallResults<T> results) {
    }
}
