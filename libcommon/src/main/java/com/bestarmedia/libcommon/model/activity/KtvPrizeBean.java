package com.bestarmedia.libcommon.model.activity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KtvPrizeBean {
    @SerializedName("current_page")
    public int currentPage;
    @SerializedName("from")
    public int from;
    @SerializedName("last_page")
    public int lastPage;
    @SerializedName("next_page_url")
    public String nextPageUrl;
    @SerializedName("path")
    public String path;
    @SerializedName("per_page")
    public int perPage;
    @SerializedName("prev_page_url")
    public String prevPageUrl;
    @SerializedName("to")
    public int to;
    @SerializedName("total")
    public int total;
    @SerializedName("icon")
    public String icon;
    @SerializedName("data")
    public List<BnsActivityBean> data;

}
