package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class GetToken {



    @SerializedName("ktv_net_code")
    public String ktvNetCode;
    @SerializedName("device_serial_no")
    public String serialNo;
    @SerializedName("room_code")
    public String roomCode;


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
