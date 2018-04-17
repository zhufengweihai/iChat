package com.zf.ichat.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Contact.class, parentColumns = "id", childColumns = "contactId", onDelete
        = CASCADE))
public class Convr {
    @PrimaryKey
    @NonNull
    private short contactId;
    private int unread = 0;

    @NonNull
    public short getContactId() {
        return contactId;
    }

    public void setContactId(@NonNull short contactId) {
        this.contactId = contactId;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
