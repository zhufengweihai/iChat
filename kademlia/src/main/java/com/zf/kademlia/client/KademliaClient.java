package com.zf.kademlia.client;

import com.zf.common.CommonManager;
import com.zf.kademlia.Commons;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.retry.CallExecutor;
import com.zf.retry.config.RetryConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.concurrent.Callable;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import static com.zf.kademlia.Commons.NETWORK_TIMEOUT;
import static com.zf.kademlia.Commons.RETRIES_COUNT;
import static com.zf.kademlia.Commons.RETRIES_INTERVAL;

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

    private KademliaClient() {
        init();
    }

    public static KademliaClient instance() {
        return instance;
    }

    private void init() {
        group = new NioEventLoopGroup();
        Runtime.getRuntime().addShutdownHook(new Thread(group::shutdownGracefully));
        this.bootstrap = new Bootstrap();
        kademliaClientHandler = new KademliaClientHandler();
        Class<NioDatagramChannel> aClass = NioDatagramChannel.class;
        bootstrap.group(group).channel(aClass).option(ChannelOption.SO_BROADCAST, false).handler(kademliaClientHandler);
    }

    public void send(Node node, KadMessage msg) {
        long seqId = CommonManager.instance().randomLong();
        RetryConfig config = RetryConfig.defaults(RETRIES_COUNT, NETWORK_TIMEOUT, RETRIES_INTERVAL).build();
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
                throw new RuntimeException("request with seqId=" + seqId + " on node=" + node + " timed out.");
            }
            return null;
        };
    }

    public void close() {
        try {
            group.shutdownGracefully().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
