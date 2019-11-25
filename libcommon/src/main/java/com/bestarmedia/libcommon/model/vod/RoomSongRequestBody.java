package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class RoomSongRequestBody implements Serializable {

    public String uuid;//id

    public String session = "";

    public String room_code;

    public String ktv_net_code;

    public String ktv_name = "";

    public Integer type = 0;

    public Integer is_priority = 0;

    public String song_id;

    public Integer mv_id = 0;

    public String mv_galleries = "";

    public Integer user_id = 0;

    public String user_name;

    public String user_avatar;

    public Integer status = 0;

    public Integer score = 0;

    public Integer duration = 0;

    public Integer play_time = 0;

    public String record_file;

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
