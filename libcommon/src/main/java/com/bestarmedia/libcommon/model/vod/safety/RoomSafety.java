package com.bestarmedia.libcommon.model.vod.safety;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomSafety implements Serializable {
    public String id;
    @SerializedName("ktv_net_code")
    public String ktvNetCode;
    @SerializedName("room_code")
    public String roomCode;
    @SerializedName("fire_extinguisher_status")
    public String fireExtinguisherStatus;
    @SerializedName("circuit_status")
    public String circuitStatus;
    @SerializedName("escape_status")
    public String escapeStatus;
    @SerializedName("check_time")
    public String checkTime;
    public String url;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;

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
