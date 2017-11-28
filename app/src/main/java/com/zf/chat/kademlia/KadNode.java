package com.zf.chat.kademlia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/27 0027.
 */
@Entity(indexes = {@Index(value = "key", unique = true)})
public class KadNode {
    @Id
    private long id;
    @NotNull
    private String key;
    private long lastSeen;
    private String address;
    @Generated(hash = 1641967766)
    public KadNode(long id, @NotNull String key, long lastSeen, String address) {
        this.id = id;
        this.key = key;
        this.lastSeen = lastSeen;
        this.address = address;
    }
    @Generated(hash = 2016314917)
    public KadNode() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public long getLastSeen() {
        return this.lastSeen;
    }
    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
