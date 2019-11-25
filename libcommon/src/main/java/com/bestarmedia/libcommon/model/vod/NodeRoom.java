package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class NodeRoom implements Serializable {

    @SerializedName("id")
    public String id;

//    @SerializedName("erp_room_code")
//    public String erpRoomCode;

    @SerializedName("ktv_room_code")
    public String ktvRoomCode;

    @SerializedName("room_code")
    public String roomCode;

//    @SerializedName("is_enabled")
//    public int isEnabled;

    @SerializedName("status")
    public int status;

//    @SerializedName("evacuation_lan")
//    public String evacuationLan;

//    @SerializedName("control_box_code")
//    public int controlBoxCode;

//    @SerializedName("control_box_name")
//    public String controlBoxName;

    @SerializedName("control_box_scheme")
    public SerialPortCode controlBoxScheme;

    @SerializedName("vod_token")
    public String vodToken;

    @SerializedName("current_session")
    public String currentSession;

//    @SerializedName("hdmi_enable")
//    public int hdmiEnable;

//    @SerializedName("is_online")
//    public int isOnline;

//    @SerializedName("anufacturer")
//    public String anufacturer;

//    @SerializedName("os_version")
//    public String osVersion;

//    @SerializedName("apk_version_code")
//    public String apkVersionCode;

//    @SerializedName("apk_version_name")
//    public String apkVersionName;

//    @SerializedName("apk_describe")
//    public String apkDescribe;

    @SerializedName("ip")
    public String ip;

}
