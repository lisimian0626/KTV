package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/3 17:26.
 */
public class Live implements Serializable {

    @Expose
    public String id;

    @Expose
    public String live_name;

    @Expose
    public String image_thumbnail;

    @Expose
    public String image;

    @Expose
    public String live_type;

    @Expose
    public String live_address;

    @Expose
    public String description;

    @Expose
    public String start_time;

    @Expose
    public String end_time;

    @Expose
    public String created_at;

    @Expose
    public int live_status;

}