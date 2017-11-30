package com.zf.retry.listener;


import com.zf.retry.CallResults;

public interface BeforeNextTryListener extends RetryListener {

    void immediatelyBeforeNextTry(CallResults results);

}
