package com.bestarmedia.libcommon.data;

import android.content.Context;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.bestarmedia.libcommon.util.Logger;

/**
 * Created by J Wong on 2016/10/10.
 */

public class ProjectorInfo {

    public static boolean IS_HAD_PROJECTOR = false;

    private static ScenesType mScenesType = ScenesType.STAR;

    private static String mCustomUrl;

    public static int mCustomSelected = 0;

    private static String mProjectorPlayingUrl;

    private static boolean mIsProjectorPlaying;

    public static boolean getProjectorPlaying() {
        return mIsProjectorPlaying;
    }

    public static void setProjectorPlaying(boolean playing) {
        mIsProjectorPlaying = playing;
    }

    public static String getProjectorPlayingUrl() {
        return mProjectorPlayingUrl;
    }

    public static void setProjectorPlayingUrl(String url) {
        mProjectorPlayingUrl = url;
    }

    public static void setCustom(int position, String customUrl) {
        mCustomSelected = position;
        mCustomUrl = customUrl;
        //屏蔽主副屏通信
//        if (DeviceHelper.getInstance().isMainVod()) {
//            SocketOperationUtil.sendVideoUrl2Projector(0, mCustomUrl);
//            SocketOperationUtil.sendCurCustomScenes2Sec(mCustomSelected + ";" + mCustomUrl);
//        } else {
//            SocketOperationUtil.sendCurCustomScenes2Main(mCustomSelected + ";" + mCustomUrl);
//        }
    }

    public static ScenesType getCurScenesType() {
        return mScenesType;
    }

    public static void setCurScenesType(Context context, ScenesType scenesType) {
        if (scenesType != mScenesType) {
            mScenesType = scenesType;
            if (DeviceHelper.isMainVod(context))
                EventBusUtil.postSticky(EventBusId.ImId.SCENES_TYPE_CHANGED, mScenesType);
            int mode = 0;
            if (mScenesType == ScenesType.STAR) {
                mode = 1;
            } else if (mScenesType == ScenesType.MV) {
                mode = 2;
            } else if (mScenesType == ScenesType.BD) {
                mode = 3;
            } else if (mScenesType == ScenesType.RM) {
                mode = 4;
            } else if (mScenesType == ScenesType.HOT) {
                mode = 5;
            } else if (mScenesType == ScenesType.CUSTOM) {
                mode = 6;
            } else if (mScenesType == ScenesType.MOVIE) {
                mode = 7;
            } else if (mScenesType == ScenesType.LIVE) {
                mode = 8;
            }
            if (mode > 0) {
                //屏蔽主副屏通信
//                if (DeviceHelper.isMainVod(context))
//                    SocketOperationUtil.sendCurScenes2Sec(mode);
//                else
//                    SocketOperationUtil.sendCurScenes2Main(mode);
            }
        }
    }


    public static void setCurScenesType(Context context, int mode) {
        ScenesType scenesType = null;
        switch (mode) {
            case 1:
                scenesType = ScenesType.STAR;
                break;
            case 2:
                scenesType = ScenesType.MV;
                break;
            case 3:
                scenesType = ScenesType.BD;
                break;
            case 4:
                scenesType = ScenesType.RM;
                break;
            case 5:
                scenesType = ScenesType.HOT;
                break;
            case 6:
                scenesType = ScenesType.CUSTOM;
                break;
            case 7:
                scenesType = ScenesType.MOVIE;
                break;
            case 8:
                scenesType = ScenesType.LIVE;
                break;
        }
        if (scenesType != mScenesType) {
            mScenesType = scenesType;
            if (DeviceHelper.isMainVod(context))
                EventBusUtil.postSticky(EventBusId.ImId.SCENES_TYPE_CHANGED, mScenesType);

            EventBusUtil.postSticky(EventBusId.ImId.CURRENT_SECENCE_MODE_CHANGED, 0);

        }
    }

    public static void setCustom(String positionUrl) {
        try {
            String[] strings = positionUrl.split(";");
            mCustomSelected = Integer.valueOf(strings[0]);
            mCustomUrl = strings[1];
            mScenesType = ScenesType.CUSTOM;
            //屏蔽主副屏通信
//            if (DeviceHelper.getInstance().isMainVod())
//                SocketOperationUtil.sendVideoUrl2Projector(0, mCustomUrl);
            EventBusUtil.postSticky(EventBusId.ImId.CURRENT_SECENCE_MODE_CHANGED, 0);
        } catch (Exception e) {
            Logger.d("ProjectorInfo", "setCustom ex:" + e.toString());
        }
    }

    public enum ScenesType {
        STAR, MV, BD, RM, HOT, CUSTOM, MOVIE, LIVE
    }
}
