package com.bestarmedia.libcommon.model.vod.login;

import com.bestarmedia.libcommon.model.vod.GetToken;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceInfo implements Serializable {


    /**
     * id : 055b31ed-f1d9-49f7-951b-ba7efaf53326
     * type : 3
     * name : 门口屏
     * room_code : B15
     * ip : 192.168.1.152
     * server_ip : 192.168.1.147
     * is_online : 0
     * serial_no : da55c8371fee780e
     * anufacturer : 晨芯
     * os_version : 30
     * apk_version_code : 596
     * apk_version_name : V4.1.8
     * apk_describe : 星云X3(横版)
     * ktv_room_code : null
     * hdmi_enable : 0
     */

    public String id;
    public int type;
    public String name;
    @SerializedName("room_code")
    public String roomCode;
    public String ip;
    @SerializedName("server_ip")
    public String serverIp;
    @SerializedName("is_online")
    public int isOnline;
    @SerializedName("serial_no")
    public String serialNo;
    public String anufacturer;
    @SerializedName("osVersion")
    public String os_version;
    @SerializedName("apk_version_code")
    public String apkVersionCode;
    @SerializedName("apk_version_name")
    public String apkVersionName;
    @SerializedName("apk_describe")
    public String apkDescribe;
    @SerializedName("ktv_room_code")
    public String ktvRoomCode;
    @SerializedName("hdmi_enable")
    public int hdmiEnable;


    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public GetToken toGetToken(String ktvNetCode, String deviceSn){
        GetToken getToken=new GetToken();
        getToken.ktvNetCode=ktvNetCode;
        getToken.roomCode=roomCode;
        getToken.serialNo=deviceSn;
        return getToken;
    }
}
