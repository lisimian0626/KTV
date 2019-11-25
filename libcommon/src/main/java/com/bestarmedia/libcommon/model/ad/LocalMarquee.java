package com.bestarmedia.libcommon.model.ad;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2017/12/22.
 */

public class LocalMarquee implements Serializable {


    @Expose
    public List<LocalMarqueeAd> notice;

}
