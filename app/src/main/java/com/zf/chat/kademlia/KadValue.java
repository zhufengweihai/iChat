package com.zf.chat.kademlia;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhufeng7 on 2017-11-28.
 */
@Entity
public class KadValue {
    @Id
    private long id;
    private String content;
    private long lastPublished;
    @Generated(hash = 1263138461)
    public KadValue(long id, String content, long lastPublished) {
        this.id = id;
        this.content = content;
        this.lastPublished = lastPublished;
    }
    @Generated(hash = 1083009998)
    public KadValue() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getLastPublished() {
        return this.lastPublished;
    }
    public void setLastPublished(long lastPublished) {
        this.lastPublished = lastPublished;
    }
}
