package de.cgrotz.kademlia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import de.cgrotz.kademlia.client.Consumer;
import de.cgrotz.kademlia.client.FindNodeConsumer;
import de.cgrotz.kademlia.client.KademliaClient;
import de.cgrotz.kademlia.config.Listener;
import de.cgrotz.kademlia.config.UdpListener;
import de.cgrotz.kademlia.events.Event;
import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.node.Node;
import de.cgrotz.kademlia.protocol.NodeReply;
import de.cgrotz.kademlia.protocol.ValueReply;
import de.cgrotz.kademlia.routing.RoutingTable;
import de.cgrotz.kademlia.routing.ValueTable;
import de.cgrotz.kademlia.server.KademliaServer;
import de.cgrotz.kademlia.storage.InMemoryStorage;
import de.cgrotz.kademlia.storage.LocalDataAccess;
import io.netty.util.internal.ConcurrentSet;

/**
 * @author Christoph on 21.09.2016.
 */
public class Kademlia {
    private static final Logger LOGGER = LoggerFactory.getLogger(Kademlia.class);

    protected final RoutingTable routingTable;
    protected final KademliaClient client = KademliaClient.instance();
    protected final List<KademliaServer> servers = new ArrayList<>();
    protected final ValueTable valueTable;
    protected final Node localNode;
    protected final Configuration config;
    private final Map<String, Consumer<Event>> eventListeners = new HashMap<>();

    public Kademlia(Key nodeId) {
        this(Configuration.defaults().nodeId(nodeId).build());
    }

    public Kademlia(Key nodeId, String listeners) {
        this(nodeId, listeners, new InMemoryStorage());
    }


    public Kademlia(Key nodeId, String listeners, LocalDataAccess localStorage) {
        this(Configuration.defaults().nodeId(nodeId).listeners(Listener.fromUrls(listeners)).advertisedListeners
                (Listener.fromUrls(listeners)).build(), localStorage);
    }

    public Kademlia(Configuration config) {
        this(config, new InMemoryStorage());
    }

    public Kademlia(Configuration config, LocalDataAccess localStorage) {
        this.config = config;
        List<Listener> advertisedListeners = config.getAdvertisedListeners();
        this.localNode = Node.builder().id(config.getNodeId()).advertisedListeners(advertisedListeners).build();
        client.init(config, localNode);
        this.routingTable = new RoutingTable(config.getKValue(), config.getNodeId());
        this.valueTable = new ValueTable();

        List<Listener> listeners = config.getListeners();
        for (Listener listener : listeners) {
            UdpListener udplistener = (UdpListener) listener;
            KademliaServer server = new KademliaServer(udplistener.getHost(), udplistener.getPort(), config.getKValue
                    (), routingTable, valueTable, localNode, eventListeners);
            this.servers.add(server);
        }
    }

    public void bootstrap(Node bootstrapNode) {
        LOGGER.debug("bootstrapping node={}", localNode);

        client.sendPing(bootstrapNode, pong -> {
            LOGGER.debug("bootstrapping node={}, ping from remote={} received", localNode, bootstrapNode);
            routingTable.addNode(pong.getOrigin());
        });

        // FIND_NODE with own IDs to find nearby nodes
        client.sendFindNode(bootstrapNode, localNode.getId(), nodes -> {
            LOGGER.debug("bootstrapping node={}, sendFind node from remote={} received, nodes={}", localNode,
                    bootstrapNode, nodes.size());
            for (Node node : nodes) {
                routingTable.addNode(node);
            }
        });

        LOGGER.debug("bootstrapping node={}, refreshing buckets", localNode);
        refreshBuckets();
    }

    /**
     * Put or Update the value in the DHT
     *
     * @param key
     * @param value
     */
    public void put(Key key, String value) {
        client.sendFindNode(localNode, key, nodes -> {
            for (Node node : nodes) {
                client.sendContentToNode(node, key, value);
            }
        });
    }

    /**
     * Retrieve the Value associated with the Key
     *
     * @param key
     * @return
     */
    public String get(Key key) {
        Consumer<ValueReply> consumer = new Consumer<ValueReply>() {
            ValueReply valueReply = null;

            @Override
            public void accept(ValueReply valueReply) {
                this.valueReply = valueReply;
            }

            @Override
            public ValueReply get() {
                while (valueReply == null) {
                }
                return valueReply;
            }
        };
        get(key, consumer);
        return consumer.get().getValue();
    }

    public void get(Key key, Consumer<ValueReply> valueReplyConsumer) {
        if (valueTable.contains(key)) {
            valueReplyConsumer.accept(new ValueReply(-1, localNode, key, valueTable.get(key).getContent()));
        } else {
            ConcurrentSet<Node> alreadyCheckedNodes = new ConcurrentSet<>();
            AtomicBoolean found = new AtomicBoolean(false);
            List<Node> nodes = routingTable.getSortedNodes(key);
            get(found, key, nodes, alreadyCheckedNodes, valueReply -> {
                if (!found.getAndSet(true)) {
                    valueReplyConsumer.accept(valueReply);
                }
            });
        }
    }

    private void get(AtomicBoolean found, Key key, List<Node> nodes, ConcurrentSet<Node> alreadyCheckedNodes,
                     Consumer<ValueReply> valueReplyConsumer) {
        for (Node node : nodes) {
            if (!alreadyCheckedNodes.contains(node) && !found.get()) {
                Consumer<NodeReply> consumer = nodeReply -> {
                    routingTable.addNodes(nodeReply.getNodes());
                    Kademlia.this.get(found, key, nodeReply.getNodes(), alreadyCheckedNodes, valueReplyConsumer);
                };
                client.sendFindValue(node, key, consumer, valueReplyConsumer);
                alreadyCheckedNodes.add(node);
            }
        }
    }

    /**
     * Execute key republishing
     * <p>
     * Iterate over all keys that weren't updated within the last hour and republish
     * to the other k-nodes that are closest to the associated keys
     */
    public void republishKeys() {
        KeyRepublishing.builder().valueTable(valueTable).routingTable(routingTable).k
                (config.getKValue()).build().execute();
    }

    public void refreshBuckets() {
        // Refresh buckets
        for (int i = 1; i < Key.ID_LENGTH; i++) {
            // Construct a Key that is i bits away from the current node Id
            final Key current = this.localNode.getId().generateNodeIdByDistance(i);
            FindNodeConsumer callback = new FindNodeConsumer(routingTable);
            List<Node> nodes = routingTable.getNodes();
            for (Node node : nodes) {
                client.sendFindNode(node, current, callback);
            }
        }
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public Node getLocalNode() {
        return localNode;
    }

    public void close() {
        for (KademliaServer server : servers) {
            server.close();
        }
        client.close();
    }

    public void addEventListener(String registrationId, Consumer<Event> eventConsumer) {
        this.eventListeners.put(registrationId, eventConsumer);
    }

    public void removeEventListener(String registrationId) {
        this.eventListeners.remove(registrationId);
    }
}
