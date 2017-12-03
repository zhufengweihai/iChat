package com.zf.kademlia.operation;

import com.zf.common.CommonManager;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.Ping;
import com.zf.kademlia.protocol.Pong;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeoutException;

/**
 * @author zhufeng
 * @date 2017/12/3
 */
public class PingOperation extends BaseOperation {
    public PingOperation(Node node) {
        super(node);
    }

    @Override
    public KadMessage createMessage() {
        return new Ping(CommonManager.instance().randomLong(), KadDataManager.instance().getLocalNode());
    }

    @Override
    @Subscribe
    public void onOperationMessage(OperationMessage message) {
        Pong pong = (Pong) message.getMessage();
        KadDataManager.instance().getRoutingTable().addNode(pong.getOrigin());
    }
}
