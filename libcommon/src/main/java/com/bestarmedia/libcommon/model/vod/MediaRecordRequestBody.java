package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class MediaRecordRequestBody implements Serializable {

    public int user_id;

    public String song_id;

    public String ktv_net_code;

    public String room_code;

    public String record_file_path;

    public String simple_song_name;

    public String singer_name;

    public String song_version;

    public int score;

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
