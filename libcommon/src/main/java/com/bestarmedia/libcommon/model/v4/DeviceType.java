package com.bestarmedia.libcommon.model.v4;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class DeviceType {
    @Expose
    public int type; //1点歌屏 2副屏 3门口屏 4墙控面板 5投影
    @Expose
    public String name;


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


