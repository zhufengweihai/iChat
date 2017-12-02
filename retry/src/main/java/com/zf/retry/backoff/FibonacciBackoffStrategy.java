package com.zf.retry.backoff;

import java.util.ArrayList;
import java.util.List;

public class FibonacciBackoffStrategy implements BackoffStrategy {
    private List<Integer> fibonacciNumbers;
    private static final int MAX_NUM_OF_FIB_NUMBERS = 25;

    public FibonacciBackoffStrategy() {
        fibonacciNumbers = new ArrayList<>();

        fibonacciNumbers.add(0);
        fibonacciNumbers.add(1);

        for (int i = 0; i < MAX_NUM_OF_FIB_NUMBERS; i++) {
            int nextFibNum = fibonacciNumbers.get(i) + fibonacciNumbers.get(i + 1);
            fibonacciNumbers.add(nextFibNum);
        }
    }

    @Override
    public long getMillisToWait(int numberOfTriesFailed, long delayBetweenAttempts) {
        int fibNumber;
        try {
            fibNumber = fibonacciNumbers.get(numberOfTriesFailed);
        } catch (IndexOutOfBoundsException e) {
            fibNumber = fibonacciNumbers.get(MAX_NUM_OF_FIB_NUMBERS - 1);
        }
        return delayBetweenAttempts * fibNumber;
    }

    public List<Integer> getFibonacciNumbers() {
        return fibonacciNumbers;
    }
}
