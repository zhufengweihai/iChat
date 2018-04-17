package com.zf.ichat.data;

import android.arch.persistence.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static MessageType toMessageType(short value) {
        return MessageType.values()[value];
    }

    @TypeConverter
    public static short fromMessageType(MessageType messageType) {
        return (short) messageType.ordinal();
    }
}