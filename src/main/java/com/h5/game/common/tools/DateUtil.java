package com.h5.game.common.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class DateUtil {
    private static SimpleDateFormat sdfYYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date getTodayBegin() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getTodayEnd() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static String getTodayBeginStr() {
        return sdfYYYYMMDDHHMMSS.format(getTodayBegin());
    }

    public static String getTodayEndStr() {
        return sdfYYYYMMDDHHMMSS.format(getTodayEnd());
    }
}
