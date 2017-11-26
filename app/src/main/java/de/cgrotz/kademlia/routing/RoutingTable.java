package de.cgrotz.kademlia.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.node.Node;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Christoph on 21.09.2016.
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
     * Compute the bucket ID in which a given node should be placed; the bucketId is computed based on how far the
     * node is away from the Local Node.
     *
     * @param nid The Key for which we want to find which bucket it belong to
     * @return Integer The bucket ID in which the given node should be placed.
     */
    public final int getBucketId(Key nid) {
        int bId = this.localNodeId.getDistance(nid) - 1;
        /* If we are trying to insert a node into it's own routing table, then the bucket ID will be -1, so let's
        just keep it in bucket 0 */
        return bId < 0 ? 0 : bId;
    }

    public void addNode(Node node) {
        if (!node.getId().equals(localNodeId)) {
            buckets[getBucketId(node.getId())].addNode(node);
        }
    }

    public void addNodes(List<Node> nodes) {
        for (Node node: nodes) {
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
