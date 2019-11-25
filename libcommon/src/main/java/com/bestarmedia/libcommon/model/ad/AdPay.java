package com.bestarmedia.libcommon.model.ad;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdPay implements Serializable {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("is_touch_show")
    public int isTouchShow;

    @SerializedName("is_tv_show")
    public int isTvShow;

    @SerializedName("horizontal_file_url")
    public String horizontalFileUrl;

    @SerializedName("horizontal_content_position")
    public String horizontalContentPosition;

    @SerializedName("vertical_file_url")
    public String verticalFileUrl;

    @SerializedName("vertical_content_position")
    public String verticalContentPosition;
}
