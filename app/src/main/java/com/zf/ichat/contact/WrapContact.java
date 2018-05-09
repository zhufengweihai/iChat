package com.zf.ichat.contact;

import android.support.annotation.NonNull;

import com.zf.ichat.data.Contact;
import com.zf.ichat.widget.SuspensionDecoration;

public class WrapContact extends Contact implements SuspensionDecoration.SuspensionTitle {
    private String title;
    private Contact contact;

    public WrapContact(Contact contact,String title) {
        super();
        this.title = title;
        this.contact = contact;
    }

    @NonNull
    public short getId() {
        return contact.getId();
    }

    public void setId(@NonNull short id) {
        contact.setId(id);
    }

    @NonNull
    public String getUserName() {
        return contact.getUserName();
    }

    public void setUserName(@NonNull String userName) {
        contact.setUserName(userName);
    }

    public String getNickname() {
        return contact.getNickname();
    }

    public void setNickname(String nickname) {
        contact.setNickname(nickname);
    }

    public String getAvatarUrl() {
        return contact.getAvatarUrl();
    }

    public void setAvatarUrl(String avatarUrl) {
        contact.setAvatarUrl(avatarUrl);
    }

    @NonNull
    public String getPinyin() {
        return contact.getPinyin();
    }

    public void setPinyin(@NonNull String pinyin) {
        contact.setPinyin(pinyin);
    }

    public int getSex() {
        return contact.getSex();
    }

    public void setSex(int sex) {
        contact.setSex(sex);
    }

    @Override
    public String getTitle() {
        return title;
    }
}
