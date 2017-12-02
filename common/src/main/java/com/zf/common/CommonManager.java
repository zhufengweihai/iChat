package com.zf.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhufeng
 * @date 2017-12-2
 */

public class CommonManager {
    private static CommonManager instance = new CommonManager();
    private ExecutorService executorService = null;

    private CommonManager() {
        executorService = new ThreadPoolExecutor(1, 5, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public static CommonManager instance() {
        return instance;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void shutdownExcutorService() {
        executorService.shutdownNow();
    }
}
