package com.zf.kademlia.operation;

import com.zf.common.CommonManager;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.MessageType;
import com.zf.kademlia.protocol.Store;

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
        new FindNodeOperation(KadDataManager.instance().getLocalNode(), key).execute();
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
