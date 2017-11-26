package de.cgrotz.kademlia.routing;

import java.util.TreeSet;

import de.cgrotz.kademlia.client.KademliaClient;
import de.cgrotz.kademlia.exception.TimeoutException;
import de.cgrotz.kademlia.node.Node;
import lombok.Data;

/**
 * @author Christoph on 22.09.2016.
 */
@Data
public class Bucket {
    private final int bucketId;
    private final TreeSet<Node> nodes;
    private final TreeSet<Node> replacementNodes;
    private final int k;

    public Bucket(int k, int bucketId) {
        this.k = k;
        this.bucketId = bucketId;
        this.nodes = new TreeSet<>();
        this.replacementNodes = new TreeSet<>();
    }

    public void addNode(Node node) {
        if (nodes.size() < k) {
            nodes.add(node);
        } else {
            Node last = nodes.last();
            try {
                KademliaClient.instance().sendPing(last, message -> {
                    nodes.remove(last);
                    last.setLastSeen(System.currentTimeMillis());
                    nodes.add(last);
                    replacementNodes.add(node);
                    if (replacementNodes.size() > k) {
                        replacementNodes.remove(replacementNodes.last());
                    }
                });
            } catch (TimeoutException e) {
                nodes.remove(last);
                nodes.add(node);
            }
        }
    }

    public TreeSet<Node> getNodes() {
        TreeSet<Node> set = new TreeSet<>();
        set.addAll(nodes);
        return set;
    }

    public void refreshBucket() {
        @SuppressWarnings("unchecked") TreeSet<Node> copySet = new TreeSet(nodes);
        // Check nodes on reachability and update
        for (Node node : copySet) {
            try {
                KademliaClient.instance().sendPing(node, pong -> {
                    nodes.remove(node);
                    node.setLastSeen(System.currentTimeMillis());
                    nodes.add(node);
                });
            } catch (TimeoutException exp) {
                nodes.remove(node);
            }
        }

        // Fill up with reachable nodes from replacement set
        while (nodes.size() < k && !replacementNodes.isEmpty()) {
            Node node = replacementNodes.first();
            try {
                KademliaClient.instance().sendPing(node, pong -> {
                    replacementNodes.remove(node);
                    node.setLastSeen(System.currentTimeMillis());
                    nodes.add(node);
                });
            } catch (TimeoutException exp) {
                replacementNodes.remove(node);
            }
        }
    }

    public void retireNode(Node nodeToRetire) {
        nodes.remove(nodeToRetire);

        // Fill up with reachable nodes from replacement set
        while (nodes.size() < k && !replacementNodes.isEmpty()) {
            Node node = replacementNodes.first();
            try {
                KademliaClient.instance().sendPing(node, pong -> {
                    replacementNodes.remove(node);
                    node.setLastSeen(System.currentTimeMillis());
                    nodes.add(node);
                });
            } catch (TimeoutException exp) {
                replacementNodes.remove(node);
            }
        }
    }
}
