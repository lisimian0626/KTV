package com.bestarmedia.libcommon.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/10/9 18:44.
 */
public class BaseModelV4 implements Serializable {

    @Expose
    @SerializedName("code")
    public int code;

    @Expose
    @SerializedName("msg")
    public String msg;

    @Expose
    @SerializedName("tips")
    public String tips;

    @Expose
    @SerializedName("url")
    public String url;

    @Expose
    @SerializedName("data")
    public JsonElement data;


    @Override
    public String toString() {
        return toJsonString();
    }

    private String toJsonString() {
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
