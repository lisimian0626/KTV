package com.bestarmedia.libcommon.model.v4;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by J Wong on 2018/7/6.
 */

public class StartSimpleList extends BaseListV4 {

    @Expose
    @SerializedName("data")
    public List<Start> data;
    
}
