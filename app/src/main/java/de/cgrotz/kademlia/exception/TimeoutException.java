package de.cgrotz.kademlia.exception;

/**
 * Created by Christoph on 27.09.2016.
 */
public class TimeoutException extends RuntimeException {

    public TimeoutException(Exception e) {
        super(e);
    }

    public TimeoutException() {

    }
}
