package com.zf.retry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Created by zhufeng7 on 2017-12-2.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class RetryCallResultMessage {
    private final CallResult callResult;
}
