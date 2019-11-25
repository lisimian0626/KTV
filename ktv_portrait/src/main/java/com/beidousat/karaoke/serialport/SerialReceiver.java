package com.beidousat.karaoke.serialport;

import android.text.TextUtils;

import com.beidousat.karaoke.VodApplication;
import com.bestarmedia.libcommon.model.vod.SerialPortCode;
import com.bestarmedia.libcommon.util.Logger;


/**
 * Created by J Wong on 2016/1/10 10:13.
 */
public class SerialReceiver {

    private static SerialReceiver mSerialReceiver;
    private static SerialPortCode mSerialPortCode;
    private String codeCache = "";
    private final static String TAG = SerialReceiver.class.getSimpleName();

    public static SerialReceiver getInstance(SerialPortCode serialPortCode) {
        mSerialPortCode = serialPortCode;
        if (mSerialReceiver == null) {
            mSerialReceiver = new SerialReceiver();
        }
        return mSerialReceiver;
    }

    private SerialReceiver() {
    }


    public void dealCode(String code) {
        if (TextUtils.isEmpty(code))
            return;
        if (mSerialPortCode != null) {
            code = code.replace(" ", "").toUpperCase();
            codeCache += code;
            Logger.d(TAG, "dealCode codeCache >>>>>>>>>> " + codeCache);
            /**
             * 基本功能
             */
            if (!TextUtils.isEmpty(mSerialPortCode.OriAccU) && codeCache.contains(mSerialPortCode.OriAccU.toUpperCase().replace(" ", ""))) {//原伴唱（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  原伴唱（上行）");
                VodApplication.getKaraokeController().originalAccompany();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.NextU) && codeCache.contains(mSerialPortCode.NextU.toUpperCase().replace(" ", ""))) {//切歌（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  切歌（上行）");
                VodApplication.getKaraokeController().next(true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MuteAndCancelU) && codeCache.contains(mSerialPortCode.MuteAndCancelU.toUpperCase().replace(" ", ""))) {//静音/取消静音（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  静音/取消静音（上行）");
                VodApplication.getKaraokeController().mute();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.ReplayU) && codeCache.contains(mSerialPortCode.ReplayU.toUpperCase().replace(" ", ""))) {//重唱（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  重唱（上行）");
                VodApplication.getKaraokeController().replay();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.PauseStartU) && codeCache.contains(mSerialPortCode.PauseStartU.toUpperCase().replace(" ", ""))) {//暂停/播放（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  暂停/播放（上行）");
                VodApplication.getKaraokeController().pauseStart();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MusicVolDownU) && codeCache.contains(mSerialPortCode.MusicVolDownU.toUpperCase().replace(" ", ""))) {//音乐音量-（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  音乐音量-（上行）");
                VodApplication.getKaraokeController().volumeDown();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MusicVolUpU) && codeCache.contains(mSerialPortCode.MusicVolUpU.toUpperCase().replace(" ", ""))) {//音乐音量+（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  音乐音量+（上行）");
                VodApplication.getKaraokeController().volumeUp();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.ServiceLightOnU) && codeCache.contains(mSerialPortCode.ServiceLightOnU.toUpperCase().replace(" ", ""))) {//服务端闪烁（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  服务端闪烁（上行）");
                VodApplication.getKaraokeController().setServiceMode(0, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.ServiceLightOffU) && codeCache.contains(mSerialPortCode.ServiceLightOffU.toUpperCase().replace(" ", ""))) {//取消服务端闪烁（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>>  取消服务端闪烁（上行）");
                VodApplication.getKaraokeController().setServiceMode(-1, false);
                codeCache = "";
            }
            /**
             * 调音
             */
            else if (!TextUtils.isEmpty(mSerialPortCode.MusicPitchDownU) && codeCache.contains(mSerialPortCode.MusicPitchDownU.toUpperCase().replace(" ", ""))) {//音乐降调
                Logger.d(TAG, "dealCode >>>>>>>>>> 音乐降调");
                VodApplication.getKaraokeController().toneDown();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MusicPitchUpU) && codeCache.contains(mSerialPortCode.MusicPitchUpU.toUpperCase().replace(" ", ""))) {//音乐升调
                Logger.d(TAG, "dealCode >>>>>>>>>> 音乐升调");
                VodApplication.getKaraokeController().toneUp();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MusicPitchOriU) && codeCache.contains(mSerialPortCode.MusicPitchOriU.toUpperCase().replace(" ", ""))) {//音乐原调
                Logger.d(TAG, "dealCode >>>>>>>>>> 音乐原调");
                VodApplication.getKaraokeController().toneDefault();
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MicVolDownU) && codeCache.contains(mSerialPortCode.MicVolDownU.toUpperCase().replace(" ", ""))) {//Mic音量-（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> Mic音量-（上行）");
                VodApplication.getKaraokeController().micDown(false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MicVolUpU) && codeCache.contains(mSerialPortCode.MicVolUpU.toUpperCase().replace(" ", ""))) {//Mic音量+（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> Mic音量+（上行）");
                VodApplication.getKaraokeController().micUp(false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MicPitchDownU) && codeCache.contains(mSerialPortCode.MicPitchDownU.toUpperCase().replace(" ", ""))) {//MIC降调
                Logger.d(TAG, "dealCode >>>>>>>>>> MIC降调");
                VodApplication.getKaraokeController().micToneDown(false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MicPitchOriU) && codeCache.contains(mSerialPortCode.MicPitchOriU.toUpperCase().replace(" ", ""))) {//MIC原调
                Logger.d(TAG, "dealCode >>>>>>>>>> MIC原调");
                VodApplication.getKaraokeController().micToneDefault(false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.MicPitchUpU) && codeCache.contains(mSerialPortCode.MicPitchUpU.toUpperCase().replace(" ", ""))) {//MIC升调
                Logger.d(TAG, "dealCode >>>>>>>>>> MIC升调");
                VodApplication.getKaraokeController().micToneUp(false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.EffectSingU) && codeCache.contains(mSerialPortCode.EffectSingU.toUpperCase().replace(" ", ""))) {//音效-唱将
                Logger.d(TAG, "dealCode >>>>>>>>>> 音效-唱将");
                VodApplication.getKaraokeController().effect(0, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.EffectBlameU) && codeCache.contains(mSerialPortCode.EffectBlameU.toUpperCase().replace(" ", ""))) {// 音效-搞怪
                Logger.d(TAG, "dealCode >>>>>>>>>> 音效-搞怪");
                VodApplication.getKaraokeController().effect(1, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.EffectTrickU) && codeCache.contains(mSerialPortCode.EffectTrickU.toUpperCase().replace(" ", ""))) {// 音效-整蛊
                Logger.d(TAG, "dealCode >>>>>>>>>> 音效-整蛊");
                VodApplication.getKaraokeController().effect(2, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.EffectHarmonyU) && codeCache.contains(mSerialPortCode.EffectHarmonyU.toUpperCase().replace(" ", ""))) {//音效-和声
                Logger.d(TAG, "dealCode >>>>>>>>>> 音效-和声");
                VodApplication.getKaraokeController().effect(3, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.EffectDownU) && codeCache.contains(mSerialPortCode.EffectDownU.toUpperCase().replace(" ", ""))) {//混响-
                Logger.d(TAG, "dealCode >>>>>>>>>> 混响-");
                VodApplication.getKaraokeController().reverberation(-1, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.EffectUpU) && codeCache.contains(mSerialPortCode.EffectUpU.toUpperCase().replace(" ", ""))) {//混响+
                Logger.d(TAG, "dealCode >>>>>>>>>> 混响+");
                VodApplication.getKaraokeController().reverberation(1, false);
                codeCache = "";
            }
            /**
             * 手动灯光
             */
            else if (!TextUtils.isEmpty(mSerialPortCode.LightsStandardU) && codeCache.contains(mSerialPortCode.LightsStandardU.toUpperCase().replace(" ", ""))) {//标准
                Logger.d(TAG, "dealCode >>>>>>>>>> 手动灯光-标准");
                VodApplication.getKaraokeController().lightMode(0, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightsBrightU) && codeCache.contains(mSerialPortCode.LightsBrightU.toUpperCase().replace(" ", ""))) {//灯光-明亮（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-明亮（上行）");
                VodApplication.getKaraokeController().lightMode(1, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightsSoftU) && codeCache.contains(mSerialPortCode.LightsSoftU.toUpperCase().replace(" ", ""))) {//灯光-柔和（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-柔和（上行）");
                VodApplication.getKaraokeController().lightMode(2, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightsLyricU) && codeCache.contains(mSerialPortCode.LightsLyricU.toUpperCase().replace(" ", ""))) {//灯光-抒情（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-抒情（上行）");
                VodApplication.getKaraokeController().lightMode(3, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightsDynamicU) && codeCache.contains(mSerialPortCode.LightsDynamicU.toUpperCase().replace(" ", ""))) {//灯光-动感（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-动感（上行）");
                VodApplication.getKaraokeController().lightMode(4, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightRomanticU) && codeCache.contains(mSerialPortCode.LightRomanticU.toUpperCase().replace(" ", ""))) {//灯光-浪漫（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-浪漫（上行）");
                VodApplication.getKaraokeController().lightMode(5, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightDimU) && codeCache.contains(mSerialPortCode.LightDimU.toUpperCase().replace(" ", ""))) {//灯光-朦胧（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-朦胧（上行）");
                VodApplication.getKaraokeController().lightMode(6, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightConcertU) && codeCache.contains(mSerialPortCode.LightConcertU.toUpperCase().replace(" ", ""))) {//灯光-演唱会（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-演唱会（上行）");
                VodApplication.getKaraokeController().lightMode(7, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightPassionU) && codeCache.contains(mSerialPortCode.LightPassionU.toUpperCase().replace(" ", ""))) {//灯光-激情（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-激情（上行）");
                VodApplication.getKaraokeController().lightMode(8, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightCleanU) && codeCache.contains(mSerialPortCode.LightCleanU.toUpperCase().replace(" ", ""))) {//灯光-清洁（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-清洁（上行）");
                VodApplication.getKaraokeController().lightMode(9, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightsOnU) && codeCache.contains(mSerialPortCode.LightsOnU.toUpperCase().replace(" ", ""))) {// 灯光-全开（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-全开（上行）");
                VodApplication.getKaraokeController().lightMode(10, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightsOffU) && codeCache.contains(mSerialPortCode.LightsOffU.toUpperCase().replace(" ", ""))) {//灯光-全关（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-全关（上行）");
                VodApplication.getKaraokeController().lightMode(11, false, true);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightDownU) && codeCache.contains(mSerialPortCode.LightDownU.toUpperCase().replace(" ", ""))) {//灯光-调暗（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-调暗（上行）");
                VodApplication.getKaraokeController().lightBrightness(0, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.LightUpU) && codeCache.contains(mSerialPortCode.LightUpU.toUpperCase().replace(" ", ""))) {//灯光-加亮（上行）
                Logger.d(TAG, "dealCode >>>>>>>>>> 灯光-加亮（上行）");
                VodApplication.getKaraokeController().lightBrightness(1, false);
                codeCache = "";
            }
            /**
             * 气氛
             */
            else if (!TextUtils.isEmpty(mSerialPortCode.AtCheersU) && codeCache.contains(mSerialPortCode.AtCheersU.toUpperCase().replace(" ", ""))) {//喝彩
                Logger.d(TAG, "dealCode >>>>>>>>>> 喝彩");
                VodApplication.getKaraokeController().atmosphere(0, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.AtWhoopedU) && codeCache.contains(mSerialPortCode.AtWhoopedU.toUpperCase().replace(" ", ""))) {//欢呼
                Logger.d(TAG, "dealCode >>>>>>>>>> 欢呼");
                VodApplication.getKaraokeController().atmosphere(1, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.AtApplauseU) && codeCache.contains(mSerialPortCode.AtApplauseU.toUpperCase().replace(" ", ""))) {//掌声
                Logger.d(TAG, "dealCode >>>>>>>>>> 掌声");
                VodApplication.getKaraokeController().atmosphere(2, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.AtHootingU) && codeCache.contains(mSerialPortCode.AtHootingU.toUpperCase().replace(" ", ""))) {//倒彩
                Logger.d(TAG, "dealCode >>>>>>>>>> 倒彩");
                VodApplication.getKaraokeController().atmosphere(3, false);
                codeCache = "";
            } else if (!TextUtils.isEmpty(mSerialPortCode.AtUnpleasantU) && codeCache.contains(mSerialPortCode.AtUnpleasantU.toUpperCase().replace(" ", ""))) {//难听
                Logger.d(TAG, "dealCode >>>>>>>>>> 难听");
                VodApplication.getKaraokeController().atmosphere(4, false);
                codeCache = "";
            } else {
                Logger.d(TAG, "dealCode 此码无对应功能！codeCache：" + codeCache);
            }
        } else {
            Logger.i(TAG, "KaraokeController is null  ");
        }
    }
}
