package com.zf.ichat.util;

import java.io.Closeable;

public class GraceUtil {
    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {

        }
    }
}
