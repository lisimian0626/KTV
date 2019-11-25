package com.bestarmedia.libcommon.model.vod.safety;

import com.bestarmedia.libcommon.data.VodConfigData;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SafetyUpload implements Serializable {
    @SerializedName("ktv_net_code")
    public String ktvNetCode;
    @SerializedName("room_code")
    public String roomCode;
    @SerializedName("ktv_room_code")
    public String ktvRoomCode;
    @SerializedName("fire_extinguisher_status")
    public int fireExtinguisherStatus;
    @SerializedName("circuit_status")
    public int circuitStatus;
    @SerializedName("escape_status")
    public int escapeStatus;
    @SerializedName("gas_status")
    public int gasStatus;

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

    public SafetyUpload putSafetyUpload(List<SafetyItem> safetyItemList){
        try {
            this.ktvNetCode= VodConfigData.getInstance().getKtvNetCode();
            this.roomCode=VodConfigData.getInstance().getRoomCode();
            this.ktvRoomCode=VodConfigData.getInstance().getKtvRoomCode();
            this.fireExtinguisherStatus=safetyItemList.get(0).type;
            this.circuitStatus=safetyItemList.get(1).type;
            this.escapeStatus=safetyItemList.get(2).type;
            this.gasStatus=safetyItemList.get(3).type;
            return this;
        }catch (Exception e){
            e.printStackTrace();
            return this;
        }
    }
}
