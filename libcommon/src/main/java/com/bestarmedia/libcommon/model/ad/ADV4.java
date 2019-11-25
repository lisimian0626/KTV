package com.bestarmedia.libcommon.model.ad;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ADV4 {
    @Expose
    @SerializedName("ad")
    public List<ADModel> adModels;
}
