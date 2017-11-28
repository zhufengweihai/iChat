package com.zf.chat.kademlia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import de.cgrotz.kademlia.node.Key;
import de.cgrotz.kademlia.routing.RoutingTable;
import de.cgrotz.kademlia.routing.ValueTable;
import de.cgrotz.kademlia.storage.KadDataDAO;
import de.cgrotz.kademlia.storage.Value;

public class DbDataAccess extends SQLiteOpenHelper implements KadDataDAO {
    private static final String NAME = "chat"; //数据库名称
    private static final int VERSION = 1; //数据库版本
    private RoutingTable routingTable = null;

    public DbDataAccess(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists value(id VARCHAR(20) primary key, lastPublished INTEGER, content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void put(Key key, Value value) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            Object[] bindArgs = {key.toString(), value.getLastPublished(), value.getContent()};
            db.execSQL("insert into value(id,content,lastPublished) values('?','?','?')", bindArgs);
            db.close();
        }
    }

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

    public boolean contains(Key key) {
        try (SQLiteDatabase db = getReadableDatabase()) {
            Cursor cursor = db.rawQuery("select id from value where id=" + key.toString(), null);
            int count = cursor.getCount();
            cursor.close();
            db.close();
            return count > 0;
        }
    }

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

    @Override
    public void save(RoutingTable routingTable) {
        routingTable.getNodes();
    }

    @Override
    public void save(ValueTable valueTable) {

    }

    @Override
    public RoutingTable readRoutingTable() {
        return null;
    }

    @Override
    public ValueTable readValueTable() {
        return null;
    }
}
