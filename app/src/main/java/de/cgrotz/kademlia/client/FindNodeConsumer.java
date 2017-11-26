package de.cgrotz.kademlia.client;

import java.util.List;

import de.cgrotz.kademlia.node.Node;
import de.cgrotz.kademlia.routing.RoutingTable;

/**
 * Created by zhufeng7 on 2017-11-22.
 */

public class FindNodeConsumer implements Consumer<List<Node>> {
    private RoutingTable routingTable;

    public FindNodeConsumer(RoutingTable routingTable) {
        this.routingTable = routingTable;
    }

    @Override
    public void accept(List<Node> nodes) {
        for (Node node : nodes) {
            routingTable.addNode(node);
        }
    }
}
