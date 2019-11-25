package com.bestarmedia.libcommon.model.node;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Author implements Serializable {
    @SerializedName("store_code")
    public String storeCode;

    public String expired;

    @SerializedName("action_time")
    public String actionTime;

    @SerializedName("auth_status")
    public int authStatus;

    @SerializedName("auth_status_desc")
    public String authStatusDesc;

    @SerializedName("auth_desc")
    public String authDesc;

    @SerializedName("auth_color")
    public String authColor;

}
