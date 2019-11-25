package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewSongInfo implements Serializable {

    public String id;
    public String name;
    public String image;
    @SerializedName("image_url")    //推荐位图片
    public String imageUrl;
    @SerializedName("image2_url")   //详情图
    public String image2Url;
    public int num;

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
