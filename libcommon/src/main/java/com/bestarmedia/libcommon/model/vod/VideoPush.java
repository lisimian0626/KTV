package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/12/18.
 */

public class VideoPush implements Serializable {

    @Expose
    public String id;

    @Expose
    public String room_code;

    @Expose
    public String path;

    @Expose
    public String title;

    @Expose
    public String content;
}
