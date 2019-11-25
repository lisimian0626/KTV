package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class CornerStylePage implements Serializable {
    @Expose
    @SerializedName("corner")
    public CornerStyleList corner;

    public static class CornerStyleList {
        @Expose
        public List<CornerStyle> data;
    }

    public static class CornerStyle {
        public int id;
        public String name;
        public String img1;
        @SerializedName("local_img1")
        public String localImg1;
        @SerializedName("img1_url")
        public String img1Url;
        @SerializedName("img1_width")
        public int img1Width;
        @SerializedName("img1_height")
        public int img1Height;
        public String img2;
        @SerializedName("local_img2")
        public String localImg2;
        @SerializedName("img2_url")
        public String img2Url;
        @SerializedName("img2_width")
        public int img2Width;
        @SerializedName("img2_height")
        public int img2Height;
        public String color;
        public int status;
    }
}
