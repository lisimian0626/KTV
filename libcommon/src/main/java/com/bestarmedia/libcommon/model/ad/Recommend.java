package com.bestarmedia.libcommon.model.ad;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recommend {
    @SerializedName("recommend")
    public List<RecommendInfo> recommendInfoList;
}
