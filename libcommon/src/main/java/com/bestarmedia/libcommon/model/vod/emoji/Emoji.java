package com.bestarmedia.libcommon.model.vod.emoji;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class Emoji implements Serializable {

    public String id;
    public String name;
    public int sort;
    public double cost;
    @SerializedName("cover_url")
    public String coverUrl;

    public Emoji(String id) {
        this.id = id;
    }
}
