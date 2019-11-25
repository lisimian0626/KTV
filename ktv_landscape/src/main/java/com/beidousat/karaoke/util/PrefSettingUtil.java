package com.beidousat.karaoke.util;

import android.content.Context;

import com.bestarmedia.libcommon.util.PreferenceUtil;

/**
 * Created by J Wong on 2016/8/24.
 */
public class PrefSettingUtil {

    public final static String PREF_KEY_TV_MARQUEE_MARGIN_TOP = "pref_key_tv_marquee_margin_top";

    public final static String PREF_KEY_TV_AD_CORNER_MARGIN_RIGHT = "pref_key_tv_ad_corner_margin_right";

    public final static String PREF_KEY_TV_MARQUEE_TEXT_SIZE_TYPE = "pref_key_tv_marquee_text_size_type";

//    public final static String PREF_KEY_VIDEO_ENLARGE = "pref_key_tv_video_enlarge";

    public final static String PREF_KEY_HDMI_MODE = "pref_key_hdmi_mode";

    public final static String PREF_KEY_HDMI_SCALE = "pref_key_hdmi_scale";

    public final static String PREF_KEY_CONVERT_MODE_TO_SCALE = "pref_key_convert_mode_to_scale";

    public static void setTvMarqueeMarginTop(Context context, int marginTop) {
        PreferenceUtil.setInt(context, PREF_KEY_TV_MARQUEE_MARGIN_TOP, marginTop);
    }

    public static int getTvMarqueeMarginTop(Context context) {
        return PreferenceUtil.getInt(context, PREF_KEY_TV_MARQUEE_MARGIN_TOP, 10);
    }


    public static void setTvAdCornerMarginRight(Context context, int marginRight) {
        PreferenceUtil.setInt(context, PREF_KEY_TV_AD_CORNER_MARGIN_RIGHT, marginRight);
    }

    public static int getTvAdCornerMarginRight(Context context) {
        return PreferenceUtil.getInt(context, PREF_KEY_TV_AD_CORNER_MARGIN_RIGHT, 0);
    }


    public static int getTvMarqueeTextSizeType(Context context) {
        return PreferenceUtil.getInt(context, PREF_KEY_TV_MARQUEE_TEXT_SIZE_TYPE, 1);
    }


    public static void setTvMarqueeTextSizeType(Context context, int type) {
        PreferenceUtil.setInt(context, PREF_KEY_TV_MARQUEE_TEXT_SIZE_TYPE, type);
    }

    /**
     *
     public static boolean getTvVideoEnlarge(Context context) {
     return PreferenceUtil.getBoolean(context, PREF_KEY_VIDEO_ENLARGE, true);
     }

     public static void setTvVideoEnlarge(Context context, boolean isEnlarge) {
     PreferenceUtil.setBoolean(context, PREF_KEY_VIDEO_ENLARGE, isEnlarge);
     }
     */
    /**
     * @param context
     * @return -1:缩小；0：原始；1：放大
     */
    private static int getHdmiMode(Context context) {
        return PreferenceUtil.getInt(context, PREF_KEY_HDMI_MODE, 0);
    }

    /**
     * @param context
     */
//    private static void setHdmiMode(Context context, int mode) {
//        PreferenceUtil.setInt(context, PREF_KEY_HDMI_MODE, mode);
//    }
    private static float getHdmiVideoScale(Context context) {
        int mode = PrefSettingUtil.getHdmiMode(context.getApplicationContext());
        if (mode == -1) {//缩小
            return 0.96f;
        } else if (mode == 1) {//放大
            return 1.04f;
        } else {//原始
            return 1f;
        }
    }

    public static float getHdmiVideoScale2(Context context) {
        if (!PreferenceUtil.getBoolean(context, PREF_KEY_CONVERT_MODE_TO_SCALE, false)) {//旧数据
            setHdmiVideoScale2(context, getHdmiVideoScale(context));
            PreferenceUtil.setBoolean(context, PREF_KEY_CONVERT_MODE_TO_SCALE, true);
        }
        float scale = PreferenceUtil.getFloat(context, PREF_KEY_HDMI_SCALE, 1);
        return scale;
    }


    public static void setHdmiVideoScale2(Context context, float scale) {
        PreferenceUtil.setFloat(context, PREF_KEY_HDMI_SCALE, scale);
    }
}
