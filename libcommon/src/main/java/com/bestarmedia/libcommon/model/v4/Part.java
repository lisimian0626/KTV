package com.bestarmedia.libcommon.model.v4;

import com.google.gson.annotations.SerializedName;

public class Part {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("sort")
    public int sort;
    @SerializedName("is_show")
    public int isShow;

    public Part(int id, String name, int sort, int isShow) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.isShow = isShow;
    }
}
