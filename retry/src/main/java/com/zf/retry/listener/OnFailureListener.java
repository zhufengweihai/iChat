package com.zf.retry.listener;

import com.zf.retry.CallResults;

public interface OnFailureListener extends RetryListener {

    void onFailure(CallResults callResults);

}
