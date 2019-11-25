package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class RegisterDevice {


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
    @SerializedName("ktv_net_code")
    public String ktvNetCode;
    public int type;
    public String name;
    @SerializedName("room_code")
    public String roomCode;
    public String ip;
    @SerializedName("server_ip")
    public String serverIp;
    @SerializedName("serial_no")
    public String serialNo;
    @SerializedName("mac_serial")
    public String macSerial;
    public String anufacturer;
    @SerializedName("os_version")
    public String osVersion;
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
    //设备id（要替换的设备id）
    public String id;

    public RegisterDevice() {
    }

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
}
