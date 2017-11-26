package de.cgrotz.kademlia.storage;

import de.cgrotz.kademlia.routing.RoutingTable;
import de.cgrotz.kademlia.routing.ValueTable;

/**
 * Created by zhufeng7 on 2017-11-24.
 */

public interface KadDataDAO {
    public void save(RoutingTable routingTable);

    public void save(ValueTable valueTable);

    public RoutingTable readRoutingTable();

    public ValueTable readValueTable();
}
