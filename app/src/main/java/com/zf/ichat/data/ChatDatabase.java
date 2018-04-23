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
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Contact.class, Convr.class, Message.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ChatDatabase extends RoomDatabase {
    private static ChatDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract ChatDao chatDao();

    public static ChatDatabase instance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                Context ac = context.getApplicationContext();
                INSTANCE = Room.databaseBuilder(ac, ChatDatabase.class, "chat.db").addCallback(new Callback() {
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
            Contact[] contacts = new Contact[10];
            for (int i = 0; i < 10; i++) {
                Contact contact = new Contact();
                contact.setId((short) i);
                contact.setAvatarUrl("https://imgsa.baidu" + "" + "" + "" +
                        ".com/baike/pic/item/d01373f082025aaf192b6064f3edab64034f1a07.jpg");
                String s = "zf" + i;
                contact.setUserName(s);
                contact.setNickname(s);
                contacts[i] = contact;
            }
            chatDao.insertContacts(contacts);

            Convr[] convrs = new Convr[10];
            for (int i = 0; i < 10; i++) {
                Convr convr = new Convr();
                convr.setContactId((short) i);
                convrs[i] = convr;
            }
            chatDao.insertConvrs(convrs);

            Message[] messages = new Message[10];
            for (int i = 0; i < 10; i++) {
                Message message = new Message();
                message.setContactId((short) 0);
                message.setCreateTime(i);
                boolean b = i % 2 == 0;
                message.setBelong(b);
                message.setMessage(b ? "那些让人过目不忘的照片，最后一张满满的即视感。" : "http://www.taopic.com/uploads/allimg/131125/240503-1311250IT642.jpg");
                message.setType(b ? MessageType.Text : MessageType.Image);
                messages[i] = message;
            }
            chatDao.insertMessages(messages);
        });
    }
}
