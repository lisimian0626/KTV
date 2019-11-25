package com.bestarmedia.libcommon.model.v4;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TokenRequestBody implements Serializable {

    @SerializedName(value = "ktv_net_code")
    public String ktvNetCode;

    @SerializedName(value = "room_code")
    public String roomCode;

    @SerializedName(value = "deviceSerialNo")
    public String deviceSerialNo;

    public TokenRequestBody(String ktvNetCode, String roomCode, String deviceSerialNo) {
        this.ktvNetCode = ktvNetCode;
        this.roomCode = roomCode;
        this.deviceSerialNo = deviceSerialNo;
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
