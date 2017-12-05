package com.zf.kademlia;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.FindNodeOperation;
import com.zf.kademlia.operation.PingOperation;
import com.zf.kademlia.operation.StoreOperation;
import com.zf.kademlia.routing.RoutingTable;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
public class Kademlia {
    private static final Logger LOGGER = LoggerFactory.getLogger(Kademlia.class);

    private void init() {
        EventBus.getDefault().register(this);
    }

    public void bootstrap(Node bootstrapNode) {
        new PingOperation(bootstrapNode).execute();
        new FindNodeOperation(bootstrapNode, KadDataManager.instance().getLocalNode().getId()).execute();
        refreshBuckets();
    }

    public void store(Key key, String value) {

    }

    private void refreshBuckets() {
        for (int i = 1; i < Key.ID_LENGTH; i++) {
            final Key current = KadDataManager.instance().getLocalNode().getId().generateNodeIdByDistance(i);
            List<Node> nodes = KadDataManager.instance().getRoutingTable().getNodes();
            for (Node node : nodes) {
                new FindNodeOperation(node,current).execute();
            }
        }
    }

    public void close() {
        EventBus.getDefault().unregister(this);
    }
}
