package com.zf.ichat.util;

import android.text.format.Time;

import java.util.Calendar;

public class DateUtils extends android.text.format.DateUtils {
    public static boolean isYesterday(long when) {
        Time time = new Time();
        time.set(when);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return (calendar.get(Calendar.YEAR) == time.year) && (calendar.get(Calendar.MONTH) == time.month) &&
                (calendar.get(Calendar.DAY_OF_MONTH) == time.monthDay);
    }
}
