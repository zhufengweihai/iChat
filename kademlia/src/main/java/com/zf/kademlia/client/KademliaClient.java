package com.zf.kademlia.client;

import com.zf.common.CommonManager;
import com.zf.kademlia.Commons;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.FindNode;
import com.zf.kademlia.protocol.FindValue;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.MessageType;
import com.zf.kademlia.protocol.NodeReply;
import com.zf.kademlia.protocol.Ping;
import com.zf.kademlia.protocol.Pong;
import com.zf.kademlia.protocol.Store;
import com.zf.kademlia.protocol.ValueReply;
import com.zf.retry.CallExecutor;
import com.zf.retry.config.RetryConfig;
import com.zf.retry.config.RetryConfigBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author zhufeng
 * @date 2017-11-29.
 */
public class KademliaClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(KademliaClient.class);
    private static SecureRandom random = new SecureRandom();
    private static KademliaClient instance = new KademliaClient();

    private KademliaClientHandler kademliaClientHandler = null;
    private Bootstrap bootstrap = null;
    private NioEventLoopGroup group = null;
    private Codec codec = new Codec();
    private Node localNode = KadDataManager.instance().getLocalNode();

    private KademliaClient() {
    }

    public static KademliaClient instance() {
        return instance;
    }

    public void init() {
        this.group = new NioEventLoopGroup();
        Runtime.getRuntime().addShutdownHook(new Thread(group::shutdownGracefully));
        this.bootstrap = new Bootstrap();
        kademliaClientHandler = new KademliaClientHandler();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, false).handler
                (kademliaClientHandler);
    }

    public void send(Node node, KadMessage msg) throws TimeoutException {
        long seqId = CommonManager.instance().randomLong();
        RetryConfig config = new RetryConfigBuilder().retryOnAnyException().withMaxNumberOfTries(3)
                .withDelayBetweenTries(1000).withExponentialBackoff().build();
        CallExecutor executor = new CallExecutor(config, CommonManager.instance().getExecutorService());
        executor.executeAsync(createTask(node, seqId, msg));
    }

    private Callable createTask(Node node, long seqId, KadMessage msg) {
        return () -> {
            Channel channel = bootstrap.bind(0).sync().channel();
            InetSocketAddress address = new InetSocketAddress(node.getIp(), node.getPort());
            DatagramPacket packet = new DatagramPacket(codec.encode(msg), address);
            channel.writeAndFlush(packet).sync();
            if (!channel.closeFuture().await(Commons.NETWORK_TIMEOUT)) {
                throw new RuntimeException("request with seqId=" + seqId + " on node=" + localNode + " timed out.");
            }
            return null;
        };
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
