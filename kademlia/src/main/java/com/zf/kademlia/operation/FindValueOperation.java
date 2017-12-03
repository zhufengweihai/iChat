package com.zf.kademlia.operation;

import com.zf.common.CommonManager;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.FindNode;
import com.zf.kademlia.protocol.FindValue;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.NodeReply;
import com.zf.kademlia.routing.RoutingTable;

import java.util.List;

/**
 * @author zhufeng
 * @date 2017/12/3 0003
 */

public class FindValueOperation extends BaseOperation {
    private Key key = null;

    public FindValueOperation(Node node, Key key) {
        super(node);
        this.key = key;
    }

    @Override
    public KadMessage createMessage() {
        return new FindValue(CommonManager.instance().randomLong(), KadDataManager.instance().getLocalNode(), key);
    }

    @Override
    public void onOperationMessage(OperationMessage message) {
        NodeReply nodeReply = (NodeReply) message.getMessage();
        List<Node> nodes = nodeReply.getNodes();
        RoutingTable routingTable = KadDataManager.instance().getRoutingTable();
        for (Node node : nodes) {
            routingTable.addNode(node);
        }
    }
}
