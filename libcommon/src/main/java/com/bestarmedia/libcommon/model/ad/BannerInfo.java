package com.bestarmedia.libcommon.model.ad;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class BannerInfo implements Serializable {

    @Expose
    public String id;

    @Expose
    public String title;

    @Expose
    public String image;

    @Expose
    public String start_time;

    @Expose
    public String end_time;

    @Expose
    public String created_at;
}
