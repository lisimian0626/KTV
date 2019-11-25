package com.bestarmedia.libcommon.model.v4;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class RoomDevice {
    public String id;
    public int type;
    public String name;
    @SerializedName("ktv_net_code")
    public String ktvNetCode;
    @SerializedName("room_code")
    public String roomCode;
    @SerializedName("ktv_room_code")
    public String ktvRoomCode;
    public String ip;
    @SerializedName("server_ip")
    public String serverIp;
    @SerializedName("is_online")
    public String isOnline;
    @SerializedName("serial_no")
    public String serialNo;
    @SerializedName("mac_serial")
    public String macSerial;
    public String anufacturer;
    @SerializedName("apk_version_code")
    public String apkVersionCode;
    @SerializedName("apk_version_name")
    public String apkVersionName;
    @SerializedName("apk_describe")
    public String apkDescribe;
    public String hdmi_enable;

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
