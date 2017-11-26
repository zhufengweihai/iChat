package de.cgrotz.kademlia.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.List;

import de.cgrotz.kademlia.Configuration;
import de.cgrotz.kademlia.config.UdpListener;
import de.cgrotz.kademlia.exception.NoMatchingListener;
import de.cgrotz.kademlia.exception.TimeoutException;
import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.node.Node;
import de.cgrotz.kademlia.protocol.Codec;
import de.cgrotz.kademlia.protocol.FindNode;
import de.cgrotz.kademlia.protocol.FindValue;
import de.cgrotz.kademlia.protocol.Message;
import de.cgrotz.kademlia.protocol.MessageType;
import de.cgrotz.kademlia.protocol.NodeReply;
import de.cgrotz.kademlia.protocol.Ping;
import de.cgrotz.kademlia.protocol.Pong;
import de.cgrotz.kademlia.protocol.Store;
import de.cgrotz.kademlia.protocol.ValueReply;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author Christoph on 21.09.2016.
 */
public class KademliaClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(KademliaClient.class);
    private static SecureRandom random = new SecureRandom();
    private KademliaClientHandler kademliaClientHandler = null;
    private Bootstrap bootstrap = null;
    private Configuration config = null;
    private Node localNode = null;
    private NioEventLoopGroup group = null;
    private Codec codec = new Codec();
    private static KademliaClient instance = new KademliaClient();

    private KademliaClient() {
    }

    public static KademliaClient instance() {
        return instance;
    }

    public void init(Configuration config, Node localNode) {
        this.localNode = localNode;
        this.config = config;
        this.group = new NioEventLoopGroup();

        Runtime.getRuntime().addShutdownHook(new Thread(group::shutdownGracefully));

        this.bootstrap = new Bootstrap();
        kademliaClientHandler = new KademliaClientHandler();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, false).handler
                (kademliaClientHandler);
    }

    private void send(Node node, long seqId, Message msg, Consumer<Message> consumer) throws TimeoutException,
            NoMatchingListener {
        kademliaClientHandler.registerHandler(seqId, consumer);

        UdpListener udpListener = node.findUdpListener();

        Retry.builder().interval(1000).retries(3).sender(() -> {
            try {
                Channel channel = bootstrap.bind(0).sync().channel();
                LOGGER.debug("requesting seqId={} msg={} on host={}:{}", seqId, msg, udpListener.getHost(),
                        udpListener.getPort());
                channel.writeAndFlush(new DatagramPacket(codec.encode(msg), new InetSocketAddress(udpListener.getHost
                        (), udpListener.getPort()))).sync();

                if (!channel.closeFuture().await(config.getNetworkTimeoutMs())) {
                    LOGGER.warn("request with seqId={} on node={} timed out.", seqId, localNode);
                    throw new RuntimeException("request with seqId=" + seqId + " on node=" + localNode + " timed out.");
                }
            } catch (InterruptedException e) {
                throw new TimeoutException();
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("unsupported encoding for encoding msg", e);
                throw new RuntimeException(e);
            }
        }).build().execute();
    }

    public void sendPing(Node node, Consumer<Pong> pongConsumer) throws TimeoutException {
        long seqId = random.nextLong();
        send(node, seqId, new Ping(seqId, localNode), msg -> {
            pongConsumer.accept((Pong) msg);
        });
    }


    public void sendFindNode(Node node, Key key, Consumer<List<Node>> callback) throws TimeoutException {
        long seqId = random.nextLong();
        send(node, seqId, new FindNode(seqId, localNode, key), message -> {
            NodeReply nodeReply = (NodeReply) message;
            callback.accept(nodeReply.getNodes());
        });
    }

    public void sendFindValue(Node node, Key key, Consumer<NodeReply> nodeReplyConsumer, Consumer<ValueReply>
            valueReplyConsumer) throws TimeoutException {
        long seqId = random.nextLong();
        send(node, seqId, new FindValue(seqId, localNode, key), message -> {
            if (message.getType() == MessageType.NODE_REPLY) {
                NodeReply nodeReply = (NodeReply) message;
                nodeReplyConsumer.accept(nodeReply);
            } else if (message.getType() == MessageType.VALUE_REPLY) {
                ValueReply valueReply = (ValueReply) message;
                valueReplyConsumer.accept(valueReply);
            }
        });
    }

    public void sendContentToNode(Node node, Key key, String value) throws TimeoutException {
        final long seqId = random.nextLong();
        send(node, seqId, new Store(seqId, localNode, key, value), message -> {

        });
    }

    public void close() {
        try {
            group.shutdownGracefully().await();
        } catch (InterruptedException e) {

        }
    }
}
