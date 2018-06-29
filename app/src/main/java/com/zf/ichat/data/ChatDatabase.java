/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zf.ichat.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.github.promeg.pinyinhelper.Pinyin;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Database(entities = {Contact.class, Convr.class, Message.class}, version = 1, exportSchema = false)
public abstract class ChatDatabase extends RoomDatabase {
    private static ChatDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract ChatDao chatDao();

    public static ChatDatabase instance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                Context ac = context.getApplicationContext();

                SQLiteCipherSpec cipherSpec = new SQLiteCipherSpec().setPageSize(4096).setKDFIteration(64000);

                WCDBOpenHelperFactory factory = new WCDBOpenHelperFactory().passphrase("passphrase".getBytes())
                        .cipherSpec(cipherSpec).writeAheadLoggingEnabled(true);

                INSTANCE = Room.databaseBuilder(ac, ChatDatabase.class, "chat.db").allowMainThreadQueries()
                        .openHelperFactory(factory).addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        initData(context);
                    }
                }).build();
            }
            return INSTANCE;
        }
    }

    private static void initData(Context context) {
        AsyncTask.execute(() -> {
            ChatDao chatDao = ChatDatabase.instance(context).chatDao();
            Contact[] contacts = new Contact[100];
            for (int i = 0; i < 100; i++) {
                Contact contact = new Contact();
                contact.setId((short) i);
                if (i % 2 == 0) {
                    contact.setAvatarUrl("http://t1.27270.com/uploads/tu/201712/266/eb3d6d0b81.jpg");
                } else {
                    contact.setAvatarUrl("http://t1.27270.com/uploads/tu/201712/181/4471a4d986.jpg");
                }
                String name = getRandomChar();
                contact.setUserName(name);
                contact.setNickname(name);
                contact.setPinyin(Pinyin.toPinyin(name, ""));
                contacts[i] = contact;
            }
            chatDao.insertContacts(contacts);

            Convr[] convrs = new Convr[100];
            for (int i = 0; i < 100; i++) {
                Convr convr = new Convr();
                convr.setContactId((short) i);
                convrs[i] = convr;
            }
            chatDao.insertConvrs(convrs);

            Message[] messages = new Message[100];
            long now = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                Message message = new Message();
                message.setContactId((short) 0);
                message.setCreateTime((now -= (i % 2 == 0 ? i * 60 * 1000 : i * 60 * 1000 * 60 * 3)));
                if (i % 2 == 0) {
                    message.setMessage("http://www.taopic.com/uploads/allimg/131125/240503-1311250IT642.jpg");
                    message.setType(Message.IMAGE);
                    message.setBelong(true);
                } else if (i % 3 == 0) {
                    message.setMessage("http://img1.3lian.com/2015/w23/5/d/61.jpg");
                    message.setType(Message.IMAGE);
                    message.setBelong(false);
                } else if (i % 5 == 0) {
                    message.setMessage("https://b-ssl.duitang.com/uploads/item/201605/30/20160530163343_TiAHx" + "" +
                            ".thumb.700_0.gif");
                    message.setType(Message.IMAGE);
                    message.setBelong(true);
                } else {
                    boolean belong = i % 7 == 0;
                    message.setMessage(belong ? "那些让人过目不忘的照片。" : "本书是曼联功勋教练弗格森和红杉资本主席莫里茨联手之作," +
                            "全面解析弗格森38年的领导心得——如何打造并管理一支永葆战斗力的队伍...");
                    message.setType(Message.TEXT);
                    message.setBelong(belong);
                }

                messages[i] = message;
            }
            chatDao.insertMessages(messages);
        });
    }

    public static String getRandomChar() {
        String str = "";
        int highCode;
        int lowCode;

        Random random = new Random();

        highCode = (176 + Math.abs(random.nextInt(39))); //B0 + 0~39(16~55) 一级汉字所占区
        lowCode = (161 + Math.abs(random.nextInt(93))); //A1 + 0~93 每区有94个汉字

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highCode)).byteValue();
        b[1] = (Integer.valueOf(lowCode)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
