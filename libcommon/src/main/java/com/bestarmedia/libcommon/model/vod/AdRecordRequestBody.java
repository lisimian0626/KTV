package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class AdRecordRequestBody implements Serializable {

    @SerializedName("id")
    public String id;//id

    @SerializedName("name")
    public String name = "";

    @SerializedName("ad_type_id")
    public int adTypeId;

    @SerializedName("device_type")
    public int deviceType;

    @SerializedName("play_device_id")
    public String playDeviceId = "";

    @SerializedName("play_device_mac")
    public String playDeviceMac = "";

    @SerializedName("play_room_name")
    public String playRoomName = "";

    @SerializedName("room_code")
    public String roomCode = "";


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
