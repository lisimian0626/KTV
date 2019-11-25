package com.bestarmedia.libcommon.model.vod.play;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Skin implements Serializable {
    public String id;
    public String name;
    @SerializedName("skin_type_id")
    public String skinTypeId;
    @SerializedName("skin_type_name")
    public String skinTypeName;
    public String version;
    @SerializedName("across_img")
    public String acrossImg;
    @SerializedName("local_across_img")
    public String localAcrossImg;
    @SerializedName("across_img_url")
    public String acrossImgUrl;
    @SerializedName("vertical_img")
    public String verticalImg;
    @SerializedName("local_vertical_img")
    public String localVerticalImg;
    @SerializedName("vertical_img_url")
    public String verticalImgUrl;
    @SerializedName("file_path")
    public String filePath;
    @SerializedName("local_file_path")
    public String localFilePath;
    @SerializedName("file_url")
    public String fileUrl;
    public String description;
    public int status;
    public int style;
    @SerializedName("start_time")
    public String startTime;
    @SerializedName("end_time")
    public String endTime;
}
