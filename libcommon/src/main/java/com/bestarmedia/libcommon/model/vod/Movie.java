package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/3 17:26.
 */
public class Movie implements Serializable {

    @Expose
    public String ID;

    @Expose
    public String SimpName;

    @Expose
    @SerializedName("ImgUrl")
    public String Img;

    @Expose
    public String MovieType;

    @Expose
    public String Director;

    @Expose
    public String Starring;

    @Expose
    public String Description;

    @Expose
    public String ScreenedDate;

    @Expose
    public String Duration;

    @Expose
    public String RegionName;

    @Expose
    public String MovieFilePathUrl;
}