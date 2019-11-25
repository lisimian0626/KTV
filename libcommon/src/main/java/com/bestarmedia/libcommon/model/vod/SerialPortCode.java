package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2016/1/8 17:35.
 */
public class SerialPortCode {

    /**
     * 波特率
     */
    @Expose
    @SerializedName("baudrate")
    public String baudrate;

    /**
     * 切歌（上行）
     */
    @Expose
    @SerializedName("CutSongs")
    public String NextU;


    /**
     * 原伴唱（上行）
     */
    @Expose
    @SerializedName("OriginalAccompany")
    public String OriAccU;

    /**
     * 暂停/播放（上行）
     */
    @Expose
    @SerializedName("PauseBegin")
    public String PauseStartU;

    /**
     * 重唱（上行）
     */
    @Expose
    @SerializedName("Again")
    public String ReplayU;

    /**
     * 音乐音量+（上行）
     */
    @Expose
    @SerializedName("MusicVolumePlus")
    public String MusicVolUpU;


    /**
     * 音乐音量+（下行）
     */
    @Expose
    @SerializedName("MusicVolumePlusOut")
    public String MusicVolUpD;

    /**
     * 音乐音量-（上行）
     */
    @Expose
    @SerializedName("MusicVolumeLose")
    public String MusicVolDownU;

    /**
     * 音乐音量-（下行）
     */
    @Expose
    @SerializedName("MusicVolumeLoseOut")
    public String MusicVolDownD;

    /**
     * 服务端闪烁（下行）
     */
    @Expose
    @SerializedName("ServiceLightsFlashing")
    public String ServiceLightOnD;

    /**
     * 服务端闪烁（上行）
     */
    @Expose
    @SerializedName("ServiceLightsFlashingN")
    public String ServiceLightOnU;

    /**
     * 取消服务端闪烁（下行）
     */
    @Expose
    @SerializedName("ServiceLightsOff")
    public String ServiceLightOffD;

    /**
     * 取消服务端闪烁（上行）
     */
    @Expose
    @SerializedName("ServiceLightsOffN")
    public String ServiceLightOffU;

    /**
     * 静音/取消静音（上行）
     */
    @Expose
    @SerializedName("Mute")
    public String MuteAndCancelU;

    /**
     * Mic音量+（上行）
     */
    @Expose
    @SerializedName("MicrophoneVolumePlusN")
    public String MicVolUpU;

    /**
     * Mic音量+（下行）
     */
    @Expose
    @SerializedName("MicrophoneVolumePlus")
    public String MicVolUpD;

    /**
     * Mic音量-（上行）
     */
    @Expose
    @SerializedName("MicrophoneVolumeLoseN")
    public String MicVolDownU;

    /**
     * Mic音量-（下行）
     */
    @Expose
    @SerializedName("MicrophoneVolumeLose")
    public String MicVolDownD;

    /**
     * 音乐升调（上行）
     */
    @Expose
    @SerializedName("PitchPlus")
    public String MusicPitchUpU;

    /**
     * 音乐升调（下行）
     */
    @Expose
    @SerializedName("PitchPlusOut")
    public String MusicPitchUpD;

    /**
     * 音乐降调（上行）
     */
    @Expose
    @SerializedName("PitchLose")
    public String MusicPitchDownU;

    /**
     * 音乐降调（下行）
     */
    @Expose
    @SerializedName("PitchLoseOut")
    public String MusicPitchDownD;

    /**
     * 音乐原调（上行）
     */
    @Expose
    @SerializedName("OriginalPitch")
    public String MusicPitchOriU;

    /**
     * 音乐原调（下行）
     */
    @Expose
    @SerializedName("OriginalPitchOut")
    public String MusicPitchOriD;

    /**
     * 麦克风升调（上行）
     */
    @Expose
    @SerializedName("MicrophonePitchPlusIn")
    public String MicPitchUpU;

    /**
     * 麦克风升调（下行）
     */
    @Expose
    @SerializedName("MicrophonePitchPlusOut")
    public String MicPitchUpD;

    /**
     * 麦克风降调（上行）
     */
    @Expose
    @SerializedName("MicrophonePitchLostIn")
    public String MicPitchDownU;

    /**
     * 麦克风降调（下行）
     */
    @Expose
    @SerializedName("MicrophonePitchLostOut")
    public String MicPitchDownD;

    /**
     * 麦克风原调（上行）
     */
    @Expose
    @SerializedName("MicrophonePitchOriginalIn")
    public String MicPitchOriU;

    /**
     * 麦克风原调（下行）
     */
    @Expose
    @SerializedName("MicrophonePitchOriginalOut")
    public String MicPitchOriD;

    /**
     * 音效-唱将（上行）
     */
    @Expose
    @SerializedName("MusicEffectSingerIn")
    public String EffectSingU;

    /**
     * 音效-唱将（下行）
     */
    @Expose
    @SerializedName("MusicEffectSingerOut")
    public String EffectSingD;

    /**
     * 音效-搞怪（上行）
     */
    @Expose
    @SerializedName("MusicEffectBlameIn")
    public String EffectBlameU;

    /**
     * 音效-搞怪（下行）
     */
    @Expose
    @SerializedName("MusicEffectBlameOut")
    public String EffectBlameD;

    /**
     * 音效-整蛊（上行）
     */
    @Expose
    @SerializedName("MusicEffectPrankIn")
    public String EffectTrickU;

    /**
     * 音效-整蛊（下行）
     */
    @Expose
    @SerializedName("MusicEffectPrankOut")
    public String EffectTrickD;

    /**
     * 音效-和声（上行）
     */
    @Expose
    @SerializedName("MusicEffectHarmonicIn")
    public String EffectHarmonyU;

    /**
     * 音效-和声（下行）
     */
    @Expose
    @SerializedName("MusicEffectHarmonicOut")
    public String EffectHarmonyD;

    /**
     * 混响+（上行）
     */
    @Expose
    @SerializedName("ReverberatePlusIn")
    public String EffectUpU;

    /**
     * 混响+（下行）
     */
    @Expose
    @SerializedName("ReverberatePlusOut")
    public String EffectUpD;


    /**
     * 混响-（上行）
     */
    @Expose
    @SerializedName("ReverberateLostIn")
    public String EffectDownU;

    /**
     * 混响-（下行）
     */
    @Expose
    @SerializedName("ReverberateLostOut")
    public String EffectDownD;

    /**
     * 灯光-标准（上行）
     */
    @Expose
    @SerializedName("LightsStandardIn")
    public String LightsStandardU;
    /**
     * 灯光-标准（下行）
     */
    @Expose
    @SerializedName("LightsStandardOut")
    public String LightsStandardD;


    /**
     * 灯光-明亮（上行）
     */
    @Expose
    @SerializedName("LightsBrightN")
    public String LightsBrightU;

    /**
     * 灯光-明亮（下行）
     */
    @Expose
    @SerializedName("LightsBright")
    public String LightsBrightD;

    /**
     * 灯光-柔和（上行）
     */
    @Expose
    @SerializedName("LightsSoftN")
    public String LightsSoftU;

    /**
     * 灯光-柔和（下行）
     */
    @Expose
    @SerializedName("LightsSoft")
    public String LightsSoftD;

    /**
     * 灯光-抒情（上行）
     */
    @Expose
    @SerializedName("LightsLyricN")
    public String LightsLyricU;

    /**
     * 灯光-抒情（下行）
     */
    @Expose
    @SerializedName("LightsLyric")
    public String LightsLyricD;

    /**
     * 灯光-动感（上行）
     */
    @Expose
    @SerializedName("LightsDynamicN")
    public String LightsDynamicU;

    /**
     * 灯光-动感（下行）
     */
    @Expose
    @SerializedName("LightsDynamic")
    public String LightsDynamicD;

    /**
     * 灯光-浪漫（上行）
     */
    @Expose
    @SerializedName("LightsRomanticIn")
    public String LightRomanticU;

    /**
     * 灯光-浪漫（下行）
     */
    @Expose
    @SerializedName("LightsRomanticOut")
    public String LightRomanticD;

    /**
     * 灯光-朦胧（上行）
     */
    @Expose
    @SerializedName("LightsDimIn")
    public String LightDimU;

    /**
     * 灯光-浪漫（下行）
     */
    @Expose
    @SerializedName("LightsDimOut")
    public String LightDimD;

    /**
     * 灯光-演唱会（上行）
     */
    @Expose
    @SerializedName("LightsConcertIn")
    public String LightConcertU;

    /**
     * 灯光-演唱会（下行）
     */
    @Expose
    @SerializedName("LightsConcertOut")
    public String LightConcertD;

    /**
     * 灯光-激情（上行）
     */
    @Expose
    @SerializedName("LightsPassionIn")
    public String LightPassionU;

    /**
     * 灯光--激情（下行）
     */
    @Expose
    @SerializedName("LightsPassionOut")
    public String LightPassionD;

    /**
     * 灯光-清洁（上行）
     */
    @Expose
    @SerializedName("LightsCleanIn")
    public String LightCleanU;

    /**
     * 灯光--清洁（下行）
     */
    @Expose
    @SerializedName("LightsCleanOut")
    public String LightCleanD;

    /**
     * 灯光-加亮（上行）
     */
    @Expose
    @SerializedName("LightsHighLightIn")
    public String LightUpU;

    /**
     * 灯光-加亮（下行）
     */
    @Expose
    @SerializedName("LightsHighLightOut")
    public String LightUpD;

    /**
     * 灯光-调暗（上行）
     */
    @Expose
    @SerializedName("LightsDarkIn")
    public String LightDownU;

    /**
     * 灯光-调暗（下行）
     */
    @Expose
    @SerializedName("LightsDarkOut")
    public String LightDownD;

    /**
     * 灯光-全开（上行）
     */
    @Expose
    @SerializedName("LightsFullOpenN")
    public String LightsOnU;

    /**
     * 灯光-全开（下行）
     */
    @SerializedName("LightsFullOpen")
    public String LightsOnD;

    /**
     * 灯光-全关（上行）
     */
    @Expose
    @SerializedName("LightsFullCloseN")
    public String LightsOffU;

    /**
     * 灯光-全关（下行）
     */
    @Expose
    @SerializedName("LightsFullClose")
    public String LightsOffD;

    /**
     * 音乐灯光-浪漫（下行）
     */
    @Expose
    @SerializedName("MusicLightsRomanticOut")
    public String MusicLightsRomanticD;

    /**
     * 音乐灯光-柔和（下行）
     */
    @Expose
    @SerializedName("MusicLightsSoftOut")
    public String MusicLightsSoftD;

    /**
     * 音乐灯光-动感（下行）
     */
    @Expose
    @SerializedName("MusicLightsInnervationOut")
    public String MusicLightsDynamicD;

    /**
     * 音乐灯光-明亮（下行）
     */
    @Expose
    @SerializedName("MusicLightsShiningOut")
    public String MusicLightsBrightD;

    /**
     * 音乐灯光-摇滚（慢歌）（下行）
     */
    @Expose
    @SerializedName("MusicLightsRockSlow")
    public String MusicLightsRockSlowD;

    /**
     * 音乐灯光-摇滚（快歌）（下行）
     */
    @Expose
    @SerializedName("MusicLightsRockFast")
    public String MusicLightsRockFastD;

    /**
     * 音乐灯光-流行（慢歌）（下行）
     */
    @Expose
    @SerializedName("MusicLightsFashionSlow")
    public String MusicLightsFashionSlowD;

    /**
     * 音乐灯光-流行（快歌）（下行）
     */
    @Expose
    @SerializedName("MusicLightsFashionFast")
    public String MusicLightsFashionFastD;

    /**
     * 音乐灯光-民谣（下行）
     */
    @Expose
    @SerializedName("MusicLightsBallad")
    public String MusicLightsBalladD;

    /**
     * 音乐灯光-嘻哈（下行）
     */
    @Expose
    @SerializedName("MusicLightsHipHop")
    public String MusicLightsHipHopD;

    /**
     * 音乐灯光-儿歌（下行）
     */
    @Expose
    @SerializedName("MusicLightsRhymes")
    public String MusicLightsRhymesD;

    /**
     * 音乐灯光-舞曲（迪斯科）（下行）
     */
    @Expose
    @SerializedName("MusicLightsDanceDisco")
    public String MusicLightsDanceDiscoD;

    /**
     * 音乐灯光-舞曲（慢摇）（下行）
     */
    @Expose
    @SerializedName("MusicLightsDanceRoll")
    public String MusicLightsDanceRollD;

    /**
     * 音乐灯光-革命歌曲（下行）
     */
    @Expose
    @SerializedName("MusicLightsRevolution")
    public String MusicLightsRevolutionD;

    /**
     * 音乐灯光-安静的独唱（下行）
     */
    @Expose
    @SerializedName("MusicLightsQuietSolo")
    public String MusicLightsQuietSoloD;

    /**
     * 音乐灯光-大合唱（下行）
     */
    @Expose
    @SerializedName("MusicLightsCantata")
    public String MusicLightsCantataD;

    /**
     * 智能灯光-开机默认（下行）
     */
    @Expose
    @SerializedName("AILightsDefaultOut")
    public String IntelLightsBootD;

    /**
     * 智能灯光-开房（下行）
     */
    @Expose
    @SerializedName("AILightsOpenRoomOut")
    public String IntelLightsRoomOpenD;

    /**
     * 智能灯光-关房（下行）
     */
    @Expose
    @SerializedName("AILightsCloseRoomOut")
    public String IntelLightsRoomCloseD;

    /**
     * 智能灯光-公播（下行）
     */
    @Expose
    @SerializedName("AILightsBroadcastOut")
    public String IntelLightsPublicD;

    /**
     * 智能灯光-电视屏关（下行）
     */
    @Expose
    @SerializedName("AILightsTVCloseOut")
    public String IntelLightsTvOffD;

    /**
     * 智能灯光-电视屏开（下行）
     */
    @Expose
    @SerializedName("AILightsTVOpenOut")
    public String IntelLightsTvOnD;

    /**
     * 智能灯光-原唱（下行）
     */
    @Expose
    @SerializedName("AILightsOriginalOut")
    public String IntelLightsOriD;

    /**
     * 智能灯光-伴唱（下行）
     */
    @Expose
    @SerializedName("AILightsAccompanyOut")
    public String IntelLightsAccD;

    /**
     * 智能灯光-暂停（下行）
     */
    @Expose
    @SerializedName("AILightsPauseOut")
    public String IntelLightsPauseD;

    /**
     * 智能灯光-静音（下行）
     */
    @Expose
    @SerializedName("AILightsMuteOut")
    public String IntelLightsMuteD;

    /**
     * 气氛-喝彩（上行）
     */
    @Expose
    @SerializedName("AILightsCheerIn")
    public String AtCheersU;

    /**
     * 气氛-喝彩（下行）
     */
    @Expose
    @SerializedName("AILightsCheerOut")
    public String AtCheersD;

    /**
     * 气氛-欢呼（上行）
     */
    @Expose
    @SerializedName("AILightsAcclaimIn")
    public String AtWhoopedU;

    /**
     * 气氛-欢呼（下行）
     */
    @Expose
    @SerializedName("AILightsAcclaimOut")
    public String AtWhoopedD;

    /**
     * 气氛-掌声（上行）
     */
    @Expose
    @SerializedName("AILightsApplauseIn")
    public String AtApplauseU;

    /**
     * 气氛-掌声（下行）
     */
    @Expose
    @SerializedName("AILightsApplauseOut")
    public String AtApplauseD;

    /**
     * 气氛-倒彩（上行）
     */
    @Expose
    @SerializedName("AILightsBooingIn")
    public String AtHootingU;

    /**
     * 气氛-倒彩（下行）
     */
    @Expose
    @SerializedName("AILightsBooingOut")
    public String AtHootingD;
    /**
     * 气氛-难听（上行）
     */
    @Expose
    @SerializedName("AILightsHarshIn")
    public String AtUnpleasantU;
    /**
     * 气氛-难听（下行）
     */
    @Expose
    @SerializedName("AILightsHarshOut")
    public String AtUnpleasantD;

    /**
     * 空调模式-自动
     */
    @Expose
    @SerializedName("AirConditioningAutoOut")
    public String AirConModeAutoD;

    /**
     * 空调模式-制冷
     */
    @Expose
    @SerializedName("AirConditioningRefrigeratingOut")
    public String AirConModeCoolD;

    /**
     * 空调模式-制热
     */
    @Expose
    @SerializedName("AirConditioningHeatingOut")
    public String AirConModeHotD;

    /**
     * 空调模式-送风
     */
    @Expose
    @SerializedName("AirConditioningSupplyOut")
    public String AirConModeWindD;

    /**
     * 空调风速-自动
     */
    @Expose
    @SerializedName("AirConditioningWindSpeedAutoOut")
    public String AirConWindAutoD;

    /**
     * 空调风速-高风
     */
    @Expose
    @SerializedName("AirConditioningWindSpeedHighOut")
    public String AirConWindHighD;

    /**
     * 空调风速-中风
     */
    @Expose
    @SerializedName("AirConditioningWindSpeedMiddleOut")
    public String AirConWindMidD;

    /**
     * 空调风速-低风
     */
    @Expose
    @SerializedName("AirConditioningWindSpeedLowOut")
    public String AirConWindLowD;

    /**
     * 温度+
     */
    @Expose
    @SerializedName("AirConditioningTemperaturePlusOut")
    public String AirConTempUpD;

    /**
     * 温度-
     */
    @Expose
    @SerializedName("AirConditioningTemperatureLostOut")
    public String AirConTempDownD;

    /**
     * 排风开
     */
    @Expose
    @SerializedName("AirConditioningOpenExhaustOut")
    public String AirConAirOnD;

    /**
     * 排风关
     */
    @Expose
    @SerializedName("AirConditioningCloseExhaustOut")
    public String AirConAirOffD;

    /**
     * 空调开
     */
    @Expose
    @SerializedName("AirConditioningOpenOut")
    public String AirConOnD;

    /**
     * 空调关
     */
    @Expose
    @SerializedName("AirConditioningCloseOut")
    public String AirConOffD;

    /**
     * 喷泡泡（下行）
     */
    @Expose
    @SerializedName("PeripheralPapawOut")
    public String BubbleD;

    /**
     * 喷烟雾（下行）
     */
    @Expose
    @SerializedName("PeripheralSmokeOut")
    public String SmokeD;


//    /**
//     * 门牌灯-开（下行）
//     */
//    @Expose
//    @SerializedName("PeripheralOpenDoorCardMachineOut")
//    public String DoorplateOnD;
//
//    /**
//     * 门牌灯-关（下行）
//     */
//    @Expose
//    @SerializedName("PeripheralCloseDoorCardMachineOut")
//    public String DoorplateOffD;

}
