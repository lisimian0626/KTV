package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/11/3 17:30.
 */
public class Movies implements Serializable {

    @Expose
    public MovieBaseListV4 movie;

    @Expose
    public BaseWord word;
}
