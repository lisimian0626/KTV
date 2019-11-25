package com.bestarmedia.libcommon.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/10/9 18:44.
 */
public class ErrorException implements Serializable {

    @Expose
    @SerializedName("timestamp")
    public String timestamp;

    @Expose
    @SerializedName("status")
    public int status;

    @Expose
    @SerializedName("error")
    public String error;

    @Expose
    @SerializedName("message")
    public String message;

    @Expose
    @SerializedName("path")
    public String path;

}
