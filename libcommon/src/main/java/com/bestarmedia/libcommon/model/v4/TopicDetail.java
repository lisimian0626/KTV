package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopicDetail implements Serializable {
    public int id;
    public String name;
    @SerializedName("is_recommend")
    public int isRecommend;
    @SerializedName("cover_img_url")
    public String coverImgUrl;
    @SerializedName("cover_img_width")
    public int coverImgWidth;
    @SerializedName("cover_img_height")
    public int coverImgHeight;
    public int num;
    public int hot;
    public int sort;
    @SerializedName("cover_img")
    public String coverImg;
    @SerializedName("local_cover_img_path")
    public String localCoverImgPath;
    @SerializedName("detail_img")
    public String detailImg;
    @SerializedName("local_detail_img_path")
    public String localDetailImgPath;
    @SerializedName("detail_img_url")
    public String detailImgUrl;
    @SerializedName("detail_img_width")
    public int detailImgWidth;
    @SerializedName("detail_img_height")
    public int detailImgHeight;
    @SerializedName("is_words_sort")
    public int isWordsSort;
    @SerializedName("is_spell_sort")
    public int isSpellSort;
}
