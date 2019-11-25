package com.bestarmedia.libcommon.model.ad;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2015/11/3 17:30.
 */
public class BannerBaseListV4 extends BaseListV4 implements Serializable {

    @Expose
    public List<BannerInfo> data;

}