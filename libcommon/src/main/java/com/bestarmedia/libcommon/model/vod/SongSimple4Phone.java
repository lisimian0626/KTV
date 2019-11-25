package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2016/6/30.
 */
public class SongSimple4Phone implements Serializable {

    @Expose
    @SerializedName("uuid")
    public String uuid;//已点已唱歌曲记录ID

    @Expose
    @SerializedName("song_id")
    public String songId;


    public SongSimple4Phone(String uuid, String songId) {
        this.uuid = uuid;
        this.songId = songId;
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
