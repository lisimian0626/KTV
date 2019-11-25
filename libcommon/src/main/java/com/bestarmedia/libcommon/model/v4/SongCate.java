package com.bestarmedia.libcommon.model.v4;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class SongCate {
    @SerializedName("id")
    public int id;
    @SerializedName("parent_id")
    public int parentId;
    @SerializedName("name")
    public String name;
    @SerializedName("sort")
    public int sort;
    @SerializedName("is_show")
    public int isShow;

    public SongCate() {
    }

    public SongCate(int id, int parentId, String name, int sort, int isShow) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.sort = sort;
        this.isShow = isShow;
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
