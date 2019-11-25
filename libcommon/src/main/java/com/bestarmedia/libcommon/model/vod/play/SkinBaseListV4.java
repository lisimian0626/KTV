package com.bestarmedia.libcommon.model.vod.play;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by J Wong on 2015/10/20 10:10.
 */
public class SkinBaseListV4 extends BaseListV4 {

    @Expose
    public List<Skin> data;


}
