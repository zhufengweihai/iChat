package com.zf.kademlia;

import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.FindNodeOperation;
import com.zf.kademlia.operation.PingOperation;
import com.zf.kademlia.routing.RoutingTable;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
public class Kademlia {
    private static final Logger LOGGER = LoggerFactory.getLogger(Kademlia.class);
    private RoutingTable routingTable = null;

    private void init() {
        EventBus.getDefault().register(this);
    }

    public void bootstrap(Node bootstrapNode) {
        new PingOperation(bootstrapNode).execute();
        new FindNodeOperation(bootstrapNode, KadDataManager.instance().getLocalNode().getId()).execute();
    }

    public void close() {
        EventBus.getDefault().unregister(this);
    }
}
