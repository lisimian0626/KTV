package com.bestarmedia.libcommon.model.web;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.Serializable;

public class PassWord implements Serializable {
    public String password;


    @Override
    public String toString() {
        return toJsonString();
    }

    private String toJsonString() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public JsonElement toJson(){
        try {
            Gson gson = new Gson();
            return gson.toJsonTree(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
