package com.bestarmedia.libcommon.model.vod.play;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hyun implements Serializable {
    public int id;
    public String name;
    public String thumbnail;
    @SerializedName("local_thumbnail")
    public String localThumbnail;
    @SerializedName("thumbnail_url")
    public String thumbnailUrl;
    @SerializedName("thumbnail_width")
    public String thumbnailWidth;
    @SerializedName("thumbnail_height")
    public String thumbnailHeight;
    @SerializedName("file_path")
    public String filePath;
    @SerializedName("local_file_path")
    public String localFilePath;
    @SerializedName("file_url")
    public String fileUrl;
}
