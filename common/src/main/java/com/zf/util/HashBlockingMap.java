package com.zf.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhufeng
 */
public class HashBlockingMap<K, V> implements BlockingMap<K, V> {
    private ConcurrentMap<K, Entry<V>> map = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void put(K key, V value) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            if (map.containsKey(key)) {
                Entry<V> item = map.get(key);
                item.put(value);
            } else {
                Entry<V> item = new Entry<>();
                map.put(key, item);
                item.put(value);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V take(K key) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            if (!map.containsKey(key)) {
                map.put(key, new Entry<V>());
            }
        } finally {
            lock.unlock();
        }

        Entry<V> item = map.get(key);
        V x = item.take();
        map.remove(key);

        return x;
    }

    @Override
    public V poll(K key, long timeout) throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            if (!map.containsKey(key)) {
                map.put(key, new Entry<V>());
            }
        } finally {
            lock.unlock();
        }
        Entry<V> item = map.get(key);
        V x = item.poll(timeout);
        map.remove(key);
        return x;
    }

    private static class Entry<E> {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition cond = lock.newCondition();
        private E obj = null;

        private void put(E o) throws InterruptedException {
            if (o == null) {
                throw new NullPointerException();
            }
            final ReentrantLock lock = this.lock;
            lock.lockInterruptibly();
            try {
                obj = o;
                cond.signal();
            } finally {
                lock.unlock();
            }
        }

        E take() throws InterruptedException {
            E x;
            final ReentrantLock lock = this.lock;
            lock.lockInterruptibly();
            try {
                try {
                    while (obj == null) {
                        cond.await();
                    }
                } catch (InterruptedException ie) {
                    cond.signal();
                    throw ie;
                }
                x = obj;
            } finally {
                lock.unlock();
            }
            return x;
        }

        private E poll(long timeout) throws InterruptedException {
            timeout = TimeUnit.MILLISECONDS.toNanos(timeout);
            E x;
            final ReentrantLock lock = this.lock;
            lock.lockInterruptibly();
            try {
                for (; ; ) {
                    if (obj != null) {
                        x = obj;
                        break;
                    }
                    if (timeout <= 0) {
                        return null;
                    }
                    try {
                        timeout = cond.awaitNanos(timeout);
                    } catch (InterruptedException ie) {
                        cond.signal();
                        throw ie;
                    }
                }
            } finally {
                lock.unlock();
            }
            return x;
        }
    }
}