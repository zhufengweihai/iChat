package com.zf.ichat.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.paging.LimitOffsetDataSource;

import java.util.List;

@Dao
public interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContacts(Contact... contacts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConvr(Convr convr);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConvrs(Convr... convrs);

    @Query("SELECT COUNT(*) FROM convr")
    int getConvrCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(Message... messages);


    @Query("SELECT contact.id contactId,nickname,avatarUrl,message,unread,createTime FROM (convr LEFT JOIN (SELECT "
            + "contactId,message,type,max(createTime) createTime FROM Message GROUP BY contactId) record ON convr" +
            ".contactId=record.contactId) LEFT JOIN contact ON convr.contactId=contact.id ORDER BY createTime DESC "
            + "LIMIT :count OFFSET :offset")
    List<Conversation> getConvrsAfter(int count, int offset);

    @Query("SELECT contact.id contactId,nickname,avatarUrl,message,unread,createTime FROM (convr LEFT JOIN (SELECT "
            + "contactId,message,type,max(createTime) createTime FROM Message GROUP BY contactId) record ON convr" +
            ".contactId=record.contactId) LEFT JOIN contact ON convr.contactId=contact.id ORDER BY createTime DESC")
    DataSource.Factory<Integer, Conversation> getAllConvrs();

    @Query("SELECT * FROM Message WHERE contactId = :contactId ORDER BY createTime DESC LIMIT :count OFFSET :offset")
    List<Message> getMessages(short contactId, int count, int offset);

    @Query("SELECT * FROM Message WHERE contactId = :contactId ORDER BY createTime DESC")
    DataSource.Factory<Integer, Message> getMessages(short contactId);

    @Query("SELECT * FROM Message WHERE type=:type and contactId = :contactId ORDER BY createTime DESC")
    LiveData<List<Message>> getMessagesByType(@Message.Type int type, short contactId);

    @Query("SELECT * FROM Contact")
    DataSource.Factory<Integer, Contact> getAllContacts();

    @Query("SELECT * FROM Contact ORDER BY pinyin ASC LIMIT :count OFFSET :offset")
    List<Contact> getContacts(int count, int offset);

    @Query("SELECT * FROM Contact ORDER BY pinyin ASC")
    LimitOffsetDataSource<Contact> getContacts();

    @Query("SELECT count(*) FROM Contact")
    int getContactCount();

    @Query("SELECT count(*) FROM Contact WHERE pinyin<:initials")
    LiveData<Integer> getPosition(String initials);
}