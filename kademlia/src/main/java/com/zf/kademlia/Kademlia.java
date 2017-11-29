package com.zf.kademlia;

import com.zf.kademlia.client.MessageEvent;
import com.zf.kademlia.routing.RoutingTable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {/* Do something */}

    public void close() {
        EventBus.getDefault().unregister(this);
    }
}
