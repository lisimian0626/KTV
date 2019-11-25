package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class Material implements Serializable {

    @SerializedName("id")
    public String id = "0";

    @SerializedName("absolute_path")
    public String absolutePath;

    @SerializedName("relative_path")
    public String relativePath;

    public Material() {

    }

    public Material(String id, String absolutePath, String relativePath) {
        this.id = id;
        this.absolutePath = absolutePath;
        this.relativePath = relativePath;
    }
}
