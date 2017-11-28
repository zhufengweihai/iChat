package com.zf.kademlia.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.node.Node;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */

@ToString
@EqualsAndHashCode
public class RoutingTable {
    private final Key localNodeId;
    private final Bucket[] buckets;

    public RoutingTable(int k, Key localNodeId) {
        this.localNodeId = localNodeId;
        buckets = new Bucket[Key.ID_LENGTH];
        for (int i = 0; i < Key.ID_LENGTH; i++) {
            buckets[i] = new Bucket(k, i);
        }
    }

    /**
     * 计算给定节点应放置的桶ID; bucketId是根据多远的距离来计算的节点远离本地节点。
     */
    public final int getBucketId(Key nid) {
        int bId = this.localNodeId.getDistance(nid) - 1;
        //如果我们试图将一个节点插入到它自己的路由表中，那么存储区ID将是-1，所以将其设置为0桶
        return bId < 0 ? 0 : bId;
    }

    public void addNode(Node node) {
        if (!node.getId().equals(localNodeId)) {
            buckets[getBucketId(node.getId())].addNode(node);
        }
    }

    public void addNodes(List<Node> nodes) {
        for (Node node : nodes) {
            addNode(node);
        }
    }

    public List<Node> getNodes() {
        List<Node> nodes = new ArrayList<>();
        for (Bucket bucket : buckets) {
            nodes.addAll(bucket.getNodes());
        }
        return nodes;
    }

    public List<Node> getSortedNodes(Key key) {
        List<Node> nodes = getNodes();
        Collections.sort(nodes, (node1, node2) -> node1.getId().getKey().xor(key.getKey()).abs().compareTo
                (node2.getId().getKey().xor(key.getKey()).abs()));
        return nodes;
    }

    public List<Node> findClosest(Key lookupId, int numberOfRequiredNodes) {
        List<Node> nodes = getSortedNodes(lookupId);
        if (numberOfRequiredNodes >= nodes.size()) {
            return nodes;
        }
        return nodes.subList(0, numberOfRequiredNodes);
    }

    public void retireNode(Node node) {
        buckets[getBucketId(node.getId())].retireNode(node);
    }
}
