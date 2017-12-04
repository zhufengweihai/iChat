package com.zf.kademlia.operation;

import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;

import java.util.concurrent.TimeoutException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author zhufeng
 * @date 2017/12/3
 */
@Getter
@Setter
@RequiredArgsConstructor
public abstract class BaseOperation {
    final Node node;

    public abstract KadMessage createMessage();

    public void execute() {
        try {
            KademliaClient.instance().send(node, createMessage());
        } catch (TimeoutException e) {
            KadDataManager.instance().getRoutingTable().retireNode(node);
        }
    }

    public void onOperationMessage(OperationMessage message) {

    }
}
