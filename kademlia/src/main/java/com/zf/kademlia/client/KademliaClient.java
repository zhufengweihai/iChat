package com.zf.kademlia.client;

import com.zf.kademlia.Commons;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.FindNode;
import com.zf.kademlia.protocol.FindValue;
import com.zf.kademlia.protocol.Message;
import com.zf.kademlia.protocol.MessageType;
import com.zf.kademlia.protocol.NodeReply;
import com.zf.kademlia.protocol.Ping;
import com.zf.kademlia.protocol.Pong;
import com.zf.kademlia.protocol.Store;
import com.zf.kademlia.protocol.ValueReply;
import com.zf.retry.CallExecutor;
import com.zf.retry.CallResults;
import com.zf.retry.RetryListener;
import com.zf.retry.config.RetryConfig;
import com.zf.retry.config.RetryConfigBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
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

    private void send(Node node, long seqId, Message msg) throws TimeoutException {
        kademliaClientHandler.registerHandler(seqId, consumer);

        Callable<Void> callable = () -> {
            Channel channel = bootstrap.bind(0).sync().channel();
            InetSocketAddress address = new InetSocketAddress(node.getIp(), node.getPort());
            DatagramPacket packet = new DatagramPacket(codec.encode(msg), address);
            channel.writeAndFlush(packet).sync();
            if (!channel.closeFuture().await(Commons.NETWORK_TIMEOUT)) {
                LOGGER.warn("request with seqId={} on node={} timed out.", seqId, localNode);
                throw new RuntimeException("request with seqId=" + seqId + " on node=" + localNode + " timed out.");
            }
            return null;
        };

        RetryConfig config = new RetryConfigBuilder().retryOnAnyException().withMaxNumberOfTries(3)
                .withDelayBetweenTries(1000).withExponentialBackoff().build();
        CallExecutor executor = new CallExecutor(config);
        executor.registerRetryListener(new RetryListener() {
            @Override
            public void onSuccess(CallResults results) {

            }
        });
        executor.executeAsync(callable);

        Retry.builder().interval(1000).retries(3).sender(() -> {
            try {
                Channel channel = bootstrap.bind(0).sync().channel();
                LOGGER.debug("requesting seqId={} msg={} on host={}:{}", seqId, msg, node.getIp(), node.getPort());
                channel.writeAndFlush(new DatagramPacket(codec.encode(msg), new InetSocketAddress(node.getIp(), node
                        .getPort()))).sync();
                if (!channel.closeFuture().await(Commons.NETWORK_TIMEOUT)) {
                    LOGGER.warn("request with seqId={} on node={} timed out.", seqId, localNode);
                    throw new RuntimeException("request with seqId=" + seqId + " on node=" + localNode + " timed out.");
                }
            } catch (InterruptedException e) {
                throw new TimeoutException("time out");
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
