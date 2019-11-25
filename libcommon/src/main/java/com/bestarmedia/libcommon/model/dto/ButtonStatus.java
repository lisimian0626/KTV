package com.bestarmedia.libcommon.model.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ButtonStatus implements Serializable {

    @SerializedName("is_pause")
    public int isPause = 0;

    @SerializedName("is_original")
    public int isOriginal = 0;

    @SerializedName("is_mute")
    public int isMute = 0;

    @SerializedName("current_volume")
    public int currentVolume = 3;

    @SerializedName("score_mode")
    public int scoreMode = 0;

    @SerializedName("is_light_auto")
    public int isLightAuto = 1;

    @SerializedName("is_hdmi_black")
    public int isHdmiBack = 0;

    @SerializedName("is_record")
    public int isRecord = 0;

    @SerializedName("service_mode")
    public int serviceMode = -1;

    @SerializedName("current_light_code")
    public String currentLightCode = "";


    public ButtonStatus() {

    }

    @Override
    public String toString() {
        return toJson();
    }

    private String toJson() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
