package com.bestarmedia.libcommon.model.vod;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/3 17:26.
 */
public class Topic implements Serializable {

    @Expose
    public String id;

    @Expose
    public String name;

    @SerializedName("is_recommend")
    public int isRecommend;

    @Expose
    @SerializedName("cover_img_url")
    public String coverImgUrl;

    @Expose
    @SerializedName("cover_img_width")
    public int coverImgWidth;

    @Expose
    @SerializedName("cover_img_height")
    public int coverImgHidth;

    @Expose
    public String num;

    @Expose
    public String hot;

    @SerializedName("detail_img_url")
    public String detailImgUrl;

    @SerializedName("is_words_sort")
    public int isWordsSort;

    @SerializedName("is_spell_sort")
    public int isSpellSort;
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