package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/8 20:10.
 */
public class RoomDetail implements Serializable {

    @Expose
    public String RoomCode = "";

    @Expose
    public long SurplusTime = 0;

    @Expose
    public String KTVRoomCode;

    @Expose
    public String InOutID;

    @Expose
    public String KTVNetName;

    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
