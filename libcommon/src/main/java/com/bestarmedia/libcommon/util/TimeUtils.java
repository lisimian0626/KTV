package com.bestarmedia.libcommon.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by J Wong on 2015/10/23 14:14.
 */
public class TimeUtils {
    /**
     * @param time s
     * @return
     */
    public static String convertLongSecString(long time) {
        if (time <= 0) {
            return "00:00";
        }
        int s = (int) (time % 60);
        int m = (int) (time / 60 % 60);
        int h = (int) (time / 3600);
        String sStr = s < 10 ? ("0" + s) : ("" + s);
        String mStr = m < 10 ? ("0" + m) : ("" + m);
        String hStr = h <= 0 ? "" : (h < 10 ? ("0" + h) : ("" + h));

        String str = (TextUtils.isEmpty(hStr) ? "" : (hStr + ":")) + (mStr + ":" + sStr);
        return str;
    }

    /**
     * @param time ms
     * @return
     */
    public static String convertLongString(long time) {
        if (time <= 0) {
            return "00:00";
        }
        time = time / 1000;
        int s = (int) (time % 60);
        int m = (int) (time / 60 % 60);
        int h = (int) (time / 3600);
        String sStr = s < 10 ? ("0" + s) : ("" + s);
        String mStr = m < 10 ? ("0" + m) : ("" + m);
        String hStr = h <= 0 ? "" : (h < 10 ? ("0" + h) : ("" + h));

        String str = (TextUtils.isEmpty(hStr) ? "" : (hStr + ":")) + (mStr + ":" + sStr);
        return str;
    }


    public static long string2Long(String format, String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateStr);
            return date.getTime();
        } catch (Exception e) {
            Logger.w("TimeUtils", e.toString());
        }
        return 0L;
    }

    public static Date string2Date(String format, String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateStr);
            return date;
        } catch (Exception e) {
            Logger.w("TimeUtils", e.toString());
        }
        return null;
    }

    public static String long2String(String format, long milSecond) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date dt = new Date(milSecond);
            String d = sdf.format(dt);
            return d;
        } catch (Exception e) {
            Logger.w("TimeUtils", e.toString());
        }
        return null;
    }


}
