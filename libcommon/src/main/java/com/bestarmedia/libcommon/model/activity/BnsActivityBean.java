package com.bestarmedia.libcommon.model.activity;

import com.google.gson.annotations.SerializedName;

public class BnsActivityBean {
    @SerializedName("id")
    public int id;
    @SerializedName("uid")
    public int uid;
    @SerializedName("cms_activity_id")
    public int cmsActivityId;
    @SerializedName("name")
    public String name;
    @SerializedName("image")
    public String image;
    @SerializedName("image_width")
    public int imageWidth;
    @SerializedName("image_height")
    public int imageHeight;
    @SerializedName("across_x")
    public int acrossX;
    @SerializedName("across_y")
    public int acrossY;
    @SerializedName("vertical_x")
    public int verticalX;
    @SerializedName("vertical_y")
    public int verticalY;
    @SerializedName("activity_url")
    public String activityUrl;
    @SerializedName("backend_url")
    public String backendUrl;
    @SerializedName("content_url")
    public String contentUrl;
    @SerializedName("is_recommend")
    public int isRecommend;
    @SerializedName("status")
    public int status;
    @SerializedName("on_off")
    public int onOff;
    @SerializedName("start_time")
    public String startTime;
    @SerializedName("end_time")
    public String endTime;
}
