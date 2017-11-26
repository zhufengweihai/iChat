package com.zf.chat.kademlia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.storage.LocalStorage;
import de.cgrotz.kademlia.storage.Value;

public class DbLocalStorage extends SQLiteOpenHelper implements LocalStorage {
    private static final String NAME = "chat"; //数据库名称

    private static final int VERSION = 1; //数据库版本

    public DbLocalStorage(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS value(id VARCHAR(20) primary key, lastPublished INTEGER, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void put(Key key, Value value) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            Object[] bindArgs = {key.toString(), value.getLastPublished(), value.getContent()};
            db.execSQL("insert into value(id,content,lastPublished) values('?','?','?')", bindArgs);
            db.close();
        }
    }

    @Override
    public Value get(Key key) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            Cursor cursor = db.rawQuery("select * from value where id=" + key.toString(), null);
            cursor.moveToNext();
            long lastPublished = cursor.getLong(1);
            String content = cursor.getString(2);
            cursor.close();
            db.close();
            return Value.builder().lastPublished(lastPublished).content(content).build();
        }
    }

    @Override
    public boolean contains(Key key) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            Cursor cursor = db.rawQuery("select id from value where id=" + key.toString(), null);
            int count = cursor.getCount();
            cursor.close();
            db.close();
            return count > 0;
        }
    }

    @Override
    public List<Key> getKeysBeforeTimestamp(long timestamp) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            Cursor cursor = db.rawQuery("select id from value where lastPublished<=" + timestamp, null);
            List<Key> keys = new ArrayList<>();
            while (cursor.moveToNext()) {
                keys.add(Key.build(cursor.getString(1)));
            }
            cursor.close();
            db.close();
            return keys;
        }
    }
}
