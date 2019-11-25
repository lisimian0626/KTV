package com.bestarmedia.libcommon.model.view;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RollText implements Serializable {

    @SerializedName("id")
    public String id;

    @SerializedName("direction")
    public int direction;

    @SerializedName("text")
    public String text;

    @SerializedName("color")
    public int color;

    @SerializedName("text_size")
    public float textSize;

    @SerializedName("bg_color")
    public int bgColor;

    @SerializedName("duration")
    public int duration;

    public RollText(String id, int direction, String text, int color, float textSize, int bgColor, int duration) {
        this.id = id;
        this.direction = direction;
        this.text = text;
        this.color = color;
        this.textSize = textSize;
        this.bgColor = bgColor;
        this.duration = duration;
    }
}
