package com.bestarmedia.libcommon.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.bestarmedia.libcommon.util.PreferenceUtil;

/**
 * Created by J Wong on 2016/11/24.
 */

public class PrefData {

    public static String getMngPassword(Context context) {
        return PreferenceUtil.getString(context, PreferenceKey.PREF_KEY_PWD, "666666");
    }

    public static void setMngPassword(Context context, String pwd) {
        PreferenceUtil.setString(context, PreferenceKey.PREF_KEY_PWD, pwd);
    }

    public static int getInitVol(Context context) {
        return PreferenceUtil.getInt(context, PreferenceKey.PREF_KEY_INIT_VOL, 4);
    }

    public static void setInitVol(Context context, int vol) {
        if (vol > 15) {
            vol = 15;
        } else if (vol < 0) {
            vol = 0;
        }
        PreferenceUtil.setInt(context, PreferenceKey.PREF_KEY_INIT_VOL, vol);
    }

//    public static void setHyunCheckedPosition(Context context, int position) {
//        PreferenceUtil.setInt(context, PreferenceKey.PREF_KEY_HYUN_CHECKED_POSITION, position);
//    }
//
//    public static int getHyunCheckedPosition(Context context) {
//        return PreferenceUtil.getInt(context, PreferenceKey.PREF_KEY_HYUN_CHECKED_POSITION, 0);
//    }

    public static void setTvMarqueeMarginTop(Context context, int marginTop) {
        PreferenceUtil.setInt(context, PreferenceKey.PREF_KEY_TV_MARQUEE_MARGIN_TOP, marginTop);
    }

    public static int getTvMarqueeMarginTop(Context context) {
        return PreferenceUtil.getInt(context, PreferenceKey.PREF_KEY_TV_MARQUEE_MARGIN_TOP, 10);
    }

    public static void setTvAdCornerMarginRight(Context context, int marginRight) {
        PreferenceUtil.setInt(context, PreferenceKey.PREF_KEY_TV_AD_CORNER_MARGIN_RIGHT, marginRight);
    }

    public static int getTvAdCornerMarginRight(Context context) {
        return PreferenceUtil.getInt(context, PreferenceKey.PREF_KEY_TV_AD_CORNER_MARGIN_RIGHT, 0);
    }

    public static int getTvMarqueeTextSizeType(Context context) {
        return PreferenceUtil.getInt(context, PreferenceKey.PREF_KEY_TV_MARQUEE_TEXT_SIZE_TYPE, 1);
    }

    public static void setTvMarqueeTextSizeType(Context context, int type) {
        PreferenceUtil.setInt(context, PreferenceKey.PREF_KEY_TV_MARQUEE_TEXT_SIZE_TYPE, type);
    }

    /**
     * @param context
     * @return -1:缩小；0：原始；1：放大
     */
    private static int getHdmiMode(Context context) {
        return PreferenceUtil.getInt(context, PreferenceKey.PREF_KEY_HDMI_MODE, 0);
    }

    private static float getHdmiVideoScale(Context context) {
        int mode = getHdmiMode(context.getApplicationContext());
        if (mode == -1) {//缩小
            return 0.96f;
        } else if (mode == 1) {//放大
            return 1.0f;
        } else {//原始
            return 1f;
        }
    }

    public static float getHdmiVideoScale2(Context context) {
        if (!PreferenceUtil.getBoolean(context, PreferenceKey.PREF_KEY_CONVERT_MODE_TO_SCALE, false)) {//旧数据
            setHdmiVideoScale2(context, getHdmiVideoScale(context));
            PreferenceUtil.setBoolean(context, PreferenceKey.PREF_KEY_CONVERT_MODE_TO_SCALE, true);
        }
        float scale = PreferenceUtil.getFloat(context, PreferenceKey.PREF_KEY_HDMI_SCALE, 1);
        return scale;
    }


    public static void setHdmiVideoScale2(Context context, float scale) {
        PreferenceUtil.setFloat(context, PreferenceKey.PREF_KEY_HDMI_SCALE, scale);
    }

    /**
     * 获取主屏ip
     *
     * @param context
     * @return
     */
    public static String getMainDeviceIp(Context context) {
        return PreferenceUtil.getString(context, PreferenceKey.PREF_KEY_MAIN_DEVICE_IP, "");
    }

    /**
     * 设置主屏ip
     *
     * @param context
     * @param ip
     */
    public static void setMainDeviceIp(Context context, String ip) {
        PreferenceUtil.setString(context, PreferenceKey.PREF_KEY_MAIN_DEVICE_IP, ip);
    }


    /**
     * 获取JWT
     *
     * @param context
     * @return
     */
    public static String getJWT(Context context) {
        return PreferenceUtil.getString(context, PreferenceKey.PREF_KEY_JWT, "");
    }

    /**
     * 设置JWT
     *
     * @param context
     * @param jwt
     */
    public static void setJWT(Context context, String jwt) {
        PreferenceUtil.setString(context, PreferenceKey.PREF_KEY_JWT, jwt);
    }

    /**
     * 获取皮肤id
     *
     * @param context
     * @return
     */
    public static String getSkinID(Context context) {
        return PreferenceUtil.getString(context, PreferenceKey.PREF_KEY_SKIN_ID, "");
    }

    /**
     * 设置皮肤id
     *
     * @param context
     * @param skinID
     */
    public static void setSkinID(Context context, String skinID) {
        PreferenceUtil.setString(context, PreferenceKey.PREF_KEY_SKIN_ID, skinID);
    }


}
