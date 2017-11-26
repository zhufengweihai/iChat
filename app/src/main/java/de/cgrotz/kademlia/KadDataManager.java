package de.cgrotz.kademlia;

import de.cgrotz.kademlia.node.Node;
import de.cgrotz.kademlia.routing.RoutingTable;
import de.cgrotz.kademlia.routing.ValueTable;
import lombok.Data;

/**
 * @author zhufeng7 on 2017-11-24.
 */
@Data
public class KadDataManager {
    private Configuration configuration = null;
    private Node node = null;
    private RoutingTable routingTable = null;
    private ValueTable valueTable = null;
    private KadDataManager instance = new KadDataManager();

    private KadDataManager() {
    }

    private KadDataManager instance() {
        return instance;
    }
}
