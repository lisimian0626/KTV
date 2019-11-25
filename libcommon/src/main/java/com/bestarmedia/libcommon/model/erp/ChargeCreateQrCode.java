package com.bestarmedia.libcommon.model.erp;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class ChargeCreateQrCode implements Serializable {

    public String ktv_net_code;

    public String room_code;

    public String song_code;

    public float time;

    public int type;

    public int number;

    public ChargeCreateQrCode() {
    }

    public ChargeCreateQrCode(String ktvNetCode, String roomCode, String songCode, float time, int type, int number) {
        this.ktv_net_code = ktvNetCode;
        this.room_code = roomCode;
        this.song_code = songCode;
        this.time = time;
        this.type = type;
        this.number = number;
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
