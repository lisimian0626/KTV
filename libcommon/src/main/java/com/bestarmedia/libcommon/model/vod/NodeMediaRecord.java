package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class NodeMediaRecord implements Serializable {

    @SerializedName("id")
    public String id;

    @SerializedName("user_id")
    public int user_id;

    @SerializedName("song_code")
    public String song_code;

    @SerializedName("song_simple_name")
    public String song_simple_name;

    @SerializedName("singer_id")
    public String singer_id;

    @SerializedName("score")
    public int score;

    @SerializedName("material_id")
    public String material_id;//素材id

    @SerializedName("cloud_file_path")
    public String cloud_file_path;

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
