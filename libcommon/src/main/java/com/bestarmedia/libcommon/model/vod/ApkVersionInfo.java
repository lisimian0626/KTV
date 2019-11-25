package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.BaseListV4;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by J Wong on 2016/12/1.
 */

public class ApkVersionInfo extends BaseListV4 {
    @Expose
    @SerializedName("ID")
    public String id;
    @Expose
    @SerializedName("VersionName")
    public String versionName;
    @Expose
    @SerializedName("VersionNumber")
    public int versionNumber;
    @Expose
    @SerializedName("type")
    public int type;
    @Expose
    @SerializedName("FilePathUrl")
    public String filePathUrl;
    @Expose
    @SerializedName("MD5Code")
    public String md5Code;
    @Expose
    @SerializedName("UpdateContent")
    public String updateContent;
    @Expose
    @SerializedName("lastUpdateTime")
    public String lastUpdateTime;
}
