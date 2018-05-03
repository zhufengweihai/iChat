package com.zf.ichat.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(indices = {@Index("id")})
public class Contact {
    public static final int NEW_FRIEND = -4;
    public static final int GROUP_CHAT = -3;
    public static final int LABEL = -2;
    public static final int PUBLIC = -1;
    public static final int ME = 0;
    public static final int FRIEND = 1;

    @PrimaryKey
    @NonNull
    private short id;
    @NonNull
    private String userName;
    private String nickname;
    private String avatarUrl;
    private String pinyin;
    private int sex = 2;

    @NonNull
    public short getId() {
        return id;
    }

    public void setId(@NonNull short id) {
        this.id = id;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull String userName) {
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


}
