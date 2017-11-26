package de.cgrotz.kademlia.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import de.cgrotz.kademlia.node.Key;

/**
 * Created by Christoph on 23.09.2016.
 */
public class InMemoryStorage implements LocalDataAccess {

    private final ConcurrentHashMap<Key, Value> map = new ConcurrentHashMap<>();

    @Override
    public void put(Key key, Value value) {
        map.put(key, value);
    }

    @Override
    public Value get(Key key) {
        return map.get(key);
    }

    @Override
    public boolean contains(Key key) {
        return map.containsKey(key);
    }

    @Override
    public List<Key> getKeysBeforeTimestamp(long timestamp) {
        List<Key> keys = new ArrayList<>();
        Set<Map.Entry<Key, Value>> entries = map.entrySet();
        for (Map.Entry<Key, Value> entry : entries) {
            if (entry.getValue().getLastPublished() <= timestamp) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
