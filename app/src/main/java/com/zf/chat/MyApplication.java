package com.zf.chat;

import android.app.Application;

import com.zf.chat.kademlia.DaoMaster;
import com.zf.chat.kademlia.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author zhufeng
 * @date 2017-11-27
 */
public class MyApplication extends Application {
    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "kad-db-encrypted" : "kad-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
