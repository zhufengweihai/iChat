package com.zf.retry.listener;


import com.zf.retry.CallResults;

public interface OnSuccessListener extends RetryListener {

    void onSuccess(CallResults callResults);

}
