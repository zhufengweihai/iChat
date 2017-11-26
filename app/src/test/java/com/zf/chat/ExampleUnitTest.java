package com.zf.chat;

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        BitSet bitSet = new BitSet();
        bitSet.set(0, 10);
        for (int i = 0; i < 5; i++) {
            bitSet.clear(i * 2);
        }

        assertEquals(4, bitSet.toByteArray()[0]);
    }
}