package com.bestarmedia.libcommon.model.ad;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2017/12/22.
 */

public class LocalMarqueeAd implements Serializable {

    @Expose
    public String id;

    @Expose
    public String content;

    @Expose
    public String font_color;

    @Expose
    public int times = 1;

    @Expose
    public int interval;

    public boolean isPush = false;
}
