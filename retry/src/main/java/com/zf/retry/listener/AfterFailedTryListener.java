package com.zf.retry.listener;


import com.zf.retry.CallResults;

public interface AfterFailedTryListener extends RetryListener {

    void immediatelyAfterFailedTry(CallResults results);

}
