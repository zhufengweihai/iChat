package de.cgrotz.kademlia.client;

/**
 * Created by zhufeng7 on 2017-11-22.
 */

public interface Consumer<T> {
    void accept(T t);

    default T get() {
        return null;
    }
}
