package com.zf.ichat.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Contact.class, parentColumns = "id", childColumns = "contactId", onDelete
        = CASCADE), indices = {@Index("id"), @Index("contactId"), @Index("createTime")})
public class Message {
    public static final int TEXT = 0;
    public static final int IMAGE = 1;
    public static final int VOICE = 2;
    public static final int VIDEO = 3;
    public static final int LINK = 4;
    public static final int EMOJI = 5;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @NonNull
    private short contactId;
    private String message;
    private int type;
    private long createTime;
    private boolean belong;

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public short getContactId() {
        return contactId;
    }

    public void setContactId(@NonNull short contactId) {
        this.contactId = contactId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Type
    public int getType() {
        return type;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isBelong() {
        return belong;
    }

    public void setBelong(boolean belong) {
        this.belong = belong;
    }

    @IntDef({TEXT, IMAGE, VOICE, VIDEO, LINK, EMOJI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
}
