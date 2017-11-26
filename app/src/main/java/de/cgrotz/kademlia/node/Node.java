package de.cgrotz.kademlia.node;

import java.util.List;

import de.cgrotz.kademlia.config.Listener;
import de.cgrotz.kademlia.config.UdpListener;
import de.cgrotz.kademlia.exception.NoMatchingListener;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

/**
 * @author Christoph on 21.09.2016.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
public class Node implements Comparable<Node> {
    private Key id;
    @Singular
    private final List<Listener> advertisedListeners;
    @Builder.Default
    private long lastSeen = System.currentTimeMillis();

    public UdpListener findUdpListener() {
        for (Listener listener : advertisedListeners) {
            if (listener instanceof UdpListener) {
                return (UdpListener) listener;
            }
        }
        throw new NoMatchingListener();
    }

    @Override
    public int compareTo(Node o) {
        if (this.equals(o)) {
            return 0;
        }

        return (this.lastSeen > o.lastSeen) ? 1 : -1;
    }
}
