package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2018/10/16.
 */

public class ProfileDetail {

    @SerializedName("name")
    public String name;

    @SerializedName("ktv_net_code")
    public String ktvNetCode;

    @SerializedName("province_code")
    public String provinceCode;

    @SerializedName("city_code")
    public String cityCode;

    @SerializedName("area_code")
    public String areaCode;

    @SerializedName("status")
    public int status;

    @SerializedName("configuration")
    public Configuration configuration;

}
