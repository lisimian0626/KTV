package com.bestarmedia.libcommon.model.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayerOperate implements Serializable {

    //1切歌，2原伴唱，3暂停/播放，4静音/取消静音，5音量+，
    // 6音量-，7重唱，8评分开，9评分关，10录音开，
    // 11录音关，12mic-，13mic+，14原调，15降调、
    // 16升调
    @SerializedName("operate_type")
    public int operateType = 0;

    public PlayerOperate() {
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
