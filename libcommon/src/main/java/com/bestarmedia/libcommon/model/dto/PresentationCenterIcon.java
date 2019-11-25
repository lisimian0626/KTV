package com.bestarmedia.libcommon.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PresentationCenterIcon implements Serializable {

    @SerializedName("icon_id")
    public int iconId;

    @SerializedName("text")
    public String text;

    @SerializedName("auto_dismiss")
    public boolean autoDismiss;

    public PresentationCenterIcon(int iconId, String text, boolean autoDismiss) {
        this.iconId = iconId;
        this.text = text;
        this.autoDismiss = autoDismiss;
    }
}
