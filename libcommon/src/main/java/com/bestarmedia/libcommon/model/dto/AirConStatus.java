package com.bestarmedia.libcommon.model.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by admin on 2018/6/25.
 */
public class AirConStatus implements Serializable {

    @SerializedName("air_con_open")
    public boolean airConOpen = false;
    @SerializedName("mode")
    public int mode = 0;
    @SerializedName("wind_speed")
    public int windSpeed = 0;
    @SerializedName("wind_open")
    public boolean windOpen = false;

    public AirConStatus() {
    }

    public AirConStatus(boolean airConOpen, int mode, int windSpeed, boolean windOpen) {
        this.airConOpen = airConOpen;
        this.mode = mode;
        this.windSpeed = windSpeed;
        this.windOpen = windOpen;
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
