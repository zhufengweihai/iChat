package com.zf.common;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
    private SecureRandom random = new SecureRandom();

    private CommonManager() {
        SynchronousQueue<Runnable> queue = new SynchronousQueue<>();
        executorService = new ThreadPoolExecutor(1, 5, 60L, TimeUnit.SECONDS, queue);
        try {
            random = SecureRandom.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("generate key error", e);
        }
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

    public long randomLong() {
        return random.nextLong();
    }

    public void nextBytes(byte[] bytes) {
        random.nextBytes(bytes);
    }
}
