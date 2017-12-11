package com.zf.kademlia.operation;

import com.zf.common.CommonManager;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.MessageType;
import com.zf.kademlia.protocol.Store;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author zhufeng
 * @date 2017/12/4
 */
public class StoreOperation extends BaseOperation {
    private Key key = null;
    private String value = null;

    public StoreOperation(Node node, Key key, String value) {
        super(node);
        this.key = key;
        this.value = value;
    }

    @Override
    public void execute() {
        List<Node> nodes = KadDataManager.instance().getRoutingTable().findClosest(key);
        for (Node node : nodes) {
            try {
                KademliaClient.instance().send(node, createMessage());
            } catch (TimeoutException e) {
                KadDataManager.instance().getRoutingTable().retireNode(node);
            }
        }
    }

    @Override
    public KadMessage createMessage() {
        return new Store(CommonManager.instance().randomLong(), KadDataManager.instance().getLocalNode(), key, value);
    }

    @Override
    public void onOperationMessage(OperationMessage message) {
        KadMessage kadMessage = message.getMessage();
        if (kadMessage.getType() == MessageType.NODE_REPLY) {

        }
    }
}
