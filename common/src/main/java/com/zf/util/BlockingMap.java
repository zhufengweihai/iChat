package com.zf.util;

/**
 * @author zhufeng
 */
public interface BlockingMap<K, V> {
    /**
     * @param key 键值
     * @param value 取值
     * @throws InterruptedException
     */
    public void put(K key, V value) throws InterruptedException;

    /**
     * @param key 键值
     * @return 取值
     * @throws InterruptedException
     */
    public V take(K key) throws InterruptedException;

    /**
     * @param key 键值
     * @param timeout 超时时间
     * @return 取值
     * @throws InterruptedException
     */
    public V poll(K key, long timeout) throws InterruptedException;
}