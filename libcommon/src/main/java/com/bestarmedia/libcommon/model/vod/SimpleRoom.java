package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;

import java.io.Serializable;

public class SimpleRoom implements Serializable {
    private String id;
    private String ktv_room_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKtvRoomCode() {
        return ktv_room_code;
    }

    public void setKtvRoomCode(String ktv_room_code) {
        this.ktv_room_code = ktv_room_code;
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
