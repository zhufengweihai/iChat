package de.cgrotz.kademlia.protocol;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.cgrotz.kademlia.config.Listener;
import de.cgrotz.kademlia.config.UdpListener;
import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.node.Node;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * Created by Christoph on 21.09.2016.
 */
public class Codec {
    private final Base64.Decoder decoder = Base64.getDecoder();
    private final Base64.Encoder encoder = Base64.getEncoder();

    public Message decode(ByteBuf buffer) throws UnsupportedEncodingException {
        String message = buffer.toString(CharsetUtil.UTF_8);
        String[] parts = message.split("\\|");
        long seqId = Long.parseLong(parts[1]);
        Node origin = decodeNode(parts[2]);

        if (parts[0].equals(MessageType.FIND_NODE.name())) {
            return new FindNode(seqId, origin, Key.build(parts[3]));
        } else if (parts[0].equals(MessageType.PING.name())) {
            return new Ping(seqId, origin);
        } else if (parts[0].equals(MessageType.PONG.name())) {
            return new Pong(seqId, origin);
        } else if (parts[0].equals(MessageType.NODE_REPLY.name())) {
            List<Node> nodes = new ArrayList<>();
            for (int i = 3; i < parts.length; i++) {
                nodes.add(decodeNode(parts[i]));
            }
            return new NodeReply(seqId, origin, nodes);
        } else if (parts[0].equals(MessageType.STORE.name())) {
            return new Store(seqId, origin, Key.build(parts[3]), new String(decoder.decode(parts[4]), CharsetUtil
                    .UTF_8.name()));
        } else if (parts[0].equals(MessageType.STORE_REPLY.name())) {
            return new StoreReply(seqId, origin);
        } else if (parts[0].equals(MessageType.FIND_VALUE.name())) {
            return new FindValue(seqId, origin, Key.build(parts[3]));
        } else if (parts[0].equals(MessageType.VALUE_REPLY.name())) {
            return new ValueReply(seqId, origin, Key.build(parts[3]), parts[4]);
        } else {
            System.out.println("Can't decode message_type=" + parts[0]);
            throw new RuntimeException("Unknown message type=" + parts[0] + " message=" + Arrays.toString(parts));
        }
    }

    private Node decodeNode(String nodeEncoded) {
        String[] nodeParts = nodeEncoded.split(",");
        List<Listener> listeners = new ArrayList<>();
        for (int i = 1; i < nodeParts.length; i++) {
            listeners.add(Listener.fromUrl(nodeParts[i]));
        }
        Key key = Key.build(nodeParts[0]);
        return Node.builder().id(key).advertisedListeners(listeners).lastSeen(System.currentTimeMillis()).build();
    }

    private String encodeNode(Node node) {
        List<Listener> listeners = node.getAdvertisedListeners();
        StringBuilder sb = new StringBuilder();
        sb.append(node.getId()).append(',');
        int size = listeners.size();
        for (int i = 0; i < size; i++) {
            Listener listener = listeners.get(i);
            UdpListener udpListener = (UdpListener) listener;
            sb.append("udp://").append(udpListener.getHost()).append(':').append(udpListener.getPort());
            if (i < size - 1) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    private ByteBuf encode(Ping msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        return byteBuf;
    }

    public ByteBuf encode(Pong msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        return byteBuf;
    }

    private ByteBuf encode(FindNode msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        byteBuf.writeCharSequence("|" + msg.getLookupId().toString(), CharsetUtil.UTF_8);
        return byteBuf;
    }

    public ByteBuf encode(NodeReply msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        for (Node node : msg.getNodes()) {
            byteBuf.writeCharSequence("|" + encodeNode(node), CharsetUtil.UTF_8);
        }
        return byteBuf;
    }

    private ByteBuf encode(Store msg) throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        byteBuf.writeCharSequence("|" + msg.getKey(), CharsetUtil.UTF_8);
        byteBuf.writeCharSequence("|" + encoder.encodeToString(msg.getValue().getBytes(CharsetUtil.UTF_8.name())),
                CharsetUtil.UTF_8);
        return byteBuf;
    }

    public ByteBuf encode(StoreReply msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        return byteBuf;
    }

    private ByteBuf encode(FindValue msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        byteBuf.writeCharSequence("|" + msg.getKey(), CharsetUtil.UTF_8);
        return byteBuf;
    }

    public ByteBuf encode(ValueReply msg) {
        ByteBuf byteBuf = Unpooled.buffer();
        encodeHeader(byteBuf, msg);
        byteBuf.writeCharSequence("|" + msg.getKey(), CharsetUtil.UTF_8);
        byteBuf.writeCharSequence("|" + msg.getValue(), CharsetUtil.UTF_8);
        return byteBuf;
    }

    private void encodeHeader(ByteBuf byteBuf, Message msg) {
        byteBuf.writeCharSequence(msg.getType().name() + "|" + msg.getSeqId(), CharsetUtil.UTF_8);
        byteBuf.writeCharSequence("|" + encodeNode(msg.getOrigin()), CharsetUtil.UTF_8);
    }

    public ByteBuf encode(Message msg) throws UnsupportedEncodingException {
        if (msg instanceof ValueReply) {
            return encode((ValueReply) msg);
        } else if (msg instanceof FindNode) {
            return encode((FindNode) msg);
        } else if (msg instanceof NodeReply) {
            return encode((NodeReply) msg);
        } else if (msg instanceof FindValue) {
            return encode((FindValue) msg);
        } else if (msg instanceof Store) {
            return encode((Store) msg);
        } else if (msg instanceof StoreReply) {
            return encode((StoreReply) msg);
        } else if (msg instanceof Ping) {
            return encode((Ping) msg);
        } else if (msg instanceof Pong) {
            return encode((Pong) msg);
        } else {
            throw new RuntimeException("Unknown msg type:" + msg);
        }
    }
}
