package de.cgrotz.kademlia;

import java.util.List;

import de.cgrotz.kademlia.client.KademliaClient;
import de.cgrotz.kademlia.exception.TimeoutException;
import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.node.Node;
import de.cgrotz.kademlia.routing.RoutingTable;
import de.cgrotz.kademlia.routing.ValueTable;
import de.cgrotz.kademlia.storage.Value;
import lombok.Builder;
import lombok.Data;

/**
 * @author Christoph on 27.09.2016.
 */
@Data
@Builder
public class KeyRepublishing {
    private static final int ONE_HOUR_IN_MILLIS = 3600 * 1000;
    private final ValueTable valueTable;
    private final RoutingTable routingTable;
    private final int k;

    public void execute() {
        List<Key> keys = valueTable.getKeysBeforeTimestamp(System.currentTimeMillis() - ONE_HOUR_IN_MILLIS);
        for (Key key : keys) {
            Value value = valueTable.get(key);
            List<Node> closestNodes = routingTable.findClosest(key, k);
            for (Node node : closestNodes) {
                try {
                    KademliaClient.instance().sendContentToNode(node, key, value.getContent());
                } catch (TimeoutException exp) {
                    routingTable.retireNode(node);
                }
            }
            valueTable.updateLastPublished(key, System.currentTimeMillis());
        }
    }
}
