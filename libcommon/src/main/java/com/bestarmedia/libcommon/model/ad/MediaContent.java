package com.bestarmedia.libcommon.model.ad;


import com.google.gson.annotations.SerializedName;

public class MediaContent {

    @SerializedName("hbg_filename")
    public String hbgFileName;
    @SerializedName("vbg_filename")
    public String vbgFileName;
    @SerializedName("video_url")
    public String videoUrl;
    @SerializedName("qrcode_url")
    public String qrCodeUrl;
    @SerializedName("position")
    public String position;
    @SerializedName("img_url")
    public String imgUrl;
    @SerializedName("playtime")
    public int playTime;
    @SerializedName("position_horizontal")
    public String positionHorizontal;
    @SerializedName("position_vertical")
    public String positionVertical;
}
