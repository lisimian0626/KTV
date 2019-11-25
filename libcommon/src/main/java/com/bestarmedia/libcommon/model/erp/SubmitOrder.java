package com.bestarmedia.libcommon.model.erp;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2016/6/23.
 */
public class SubmitOrder implements Serializable {


    public String room_code;

    public String card;

    public String password;

    public int verify_mode;

    public List<SubmitOrderItem> purchase_goods;


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

