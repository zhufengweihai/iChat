package com.zf.chat.kademlia;

import android.content.Context;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.cgrotz.kademlia.Kademlia;
import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.storage.LocalDataAccess;

/**
 * Created by zhufeng7 on 2017-11-21.
 */

public class KademliaManager {

    private void init(Context context) {
        LocalDataAccess localStorage = new DbDataAccess(context);
        Kademlia kademlia = new Kademlia(Key.build("1"), "1", localStorage);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(kademlia::republishKeys, 1, 5, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(kademlia::refreshBuckets, 5, 60, TimeUnit.MINUTES);
    }
}
