package com.zf.kademlia;

import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.BaseOperation;
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
        BaseOperation operation = new PingOperation(bootstrapNode);
        KademliaClient.instance().send();
    }

    public void close() {
        EventBus.getDefault().unregister(this);
    }
}
