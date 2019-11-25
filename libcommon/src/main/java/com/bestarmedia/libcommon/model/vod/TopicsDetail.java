package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/3 17:30.
 */
public class TopicsDetail implements Serializable {

    @Expose
    public SongBaseListV4 song;

    @Expose
    public Topic topic;

//    @Expose
//    public String TopicsName;
//
//    @Expose
//    @SerializedName("ImgUrl")
//    public String Img;
//
//    @Expose
//    public int Hot;
//
//    @Expose
//    public List<Song> SongList;
//
//    @Expose
//    @SerializedName("RecommendImgUrl")
//    public String RecommendImg;

//    @Expose
//    public String Brand;
}
