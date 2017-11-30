package com.zf.retry.listener;


import com.zf.retry.CallResults;

public interface OnCompletionListener extends RetryListener {

    void onCompletion(CallResults callResults);
}
