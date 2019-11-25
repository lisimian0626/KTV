package com.bestarmedia.libcommon.model.v4;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Language implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("language_name")
    public String languageName;

    @SerializedName("sort")
    public int sort;

    @SerializedName("is_show")
    public int isShow;

    public Language() {
    }

    public Language(int id, String languageName, int sort, int isShow) {
        this.id = id;
        this.languageName = languageName;
        this.sort = sort;
        this.isShow = isShow;
    }

    @Override
    public String toString() {
        return toJson();
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
