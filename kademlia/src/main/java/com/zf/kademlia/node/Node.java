package com.zf.kademlia.node;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Data
@EqualsAndHashCode(of = {"id"})
@Builder
public class Node implements Comparable<Node> {
    private Key id = null;
    private String ip = null;
    private int port = -1;
    @Builder.Default
    private long lastSeen = System.currentTimeMillis();

    @Override
    public int compareTo(Node o) {
        if (this.equals(o)) {
            return 0;
        }
        return (this.lastSeen > o.lastSeen) ? 1 : -1;
    }
}
