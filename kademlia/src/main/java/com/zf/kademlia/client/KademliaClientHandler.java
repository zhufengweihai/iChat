package com.zf.kademlia.client;

import com.zf.kademlia.protocol.Codec;
import com.zf.kademlia.protocol.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * @author zhufeng
 * @date 2017-11-29.
 */
@ChannelHandler.Sharable
public class KademliaClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private final Codec codec = new Codec();
    private Map<Long, Consumer<Message>> handlers = new HashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        Message message = codec.decode(packet.content());
        handlers.get(message.getSeqId()).accept(message);
        handlers.remove(message.getSeqId());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    void registerHandler(long seqId, Consumer<Message> consumer) {
        this.handlers.put(seqId, consumer);
    }
}
