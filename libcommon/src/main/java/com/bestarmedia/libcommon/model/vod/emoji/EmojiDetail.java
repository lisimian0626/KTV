package com.bestarmedia.libcommon.model.vod.emoji;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class EmojiDetail implements Serializable {

    public String id;
    public String name;
    @SerializedName("file_path")
    public String filePath;   //云端文件url
    @SerializedName("local_file_path")
    public String localFilePath;
    @SerializedName("file_url")
    public String fileUrl;   //文件url
    @SerializedName("thumbnail_url")
    public String thumbnailUrl;

    //用于内置皮肤
    public int mode;//内置皮肤才有 0喝彩，1欢呼，2掌声，3倒彩，4难听
    public int drawableId;//内置皮肤才有

    public EmojiDetail(int mode, int drawableId) {
        this.mode = mode;
        this.drawableId = drawableId;
    }
}
