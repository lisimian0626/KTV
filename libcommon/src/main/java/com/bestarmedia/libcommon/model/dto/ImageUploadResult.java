package com.bestarmedia.libcommon.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageUploadResult {
    @SerializedName("url")
    public String url;
    @SerializedName("thumbnail")
    public List<ThumbnailBean> thumbnail;

    public static class ThumbnailBean {
        @SerializedName("file_size")
        public int fileSize;
        @SerializedName("url")
        private String url;
    }
}
