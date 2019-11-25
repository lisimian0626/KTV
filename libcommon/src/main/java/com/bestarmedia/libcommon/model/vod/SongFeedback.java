package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 2016/7/27.
 */
public class SongFeedback implements Serializable {

    @SerializedName("SongName")
    public String song_name;
    @SerializedName("SingerName")
    public String singer_name;
    @SerializedName("bugText")
    public String problem_description;

    public SongFeedback() {
    }

    public SongFeedback(String songName, String singerName, String bugText) {
        this.song_name = songName;
        this.singer_name = singerName;
        this.problem_description = bugText;
    }

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
