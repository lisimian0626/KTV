package com.bestarmedia.libcommon.model.vod.service;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2017/1/13.
 */

public class FireAlarmInfo implements Serializable {

    @Expose
    @SerializedName("alarm_img_a")
    public String AlarmImgA;//报警图片（横版）

    @Expose
    @SerializedName("alarm_img_b")
    public String AlarmImgB;//报警图片（竖版）

    @Expose
    @SerializedName("alarm_audio")
    public String AlarmAudio;//报警音频

}
