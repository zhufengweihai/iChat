package de.cgrotz.kademlia.client;

import de.cgrotz.kademlia.protocol.ValueReply;

/**
 * Created by zhufeng7 on 2017-11-22.
 */

public class GetConsumer implements Consumer<ValueReply> {

    private ValueReply message = null;

    @Override
    public void accept(ValueReply message) {
        this.message = message;
    }

    @Override
    public ValueReply get() {
        return message;
    }
}
