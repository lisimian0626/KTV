package com.bestarmedia.libcommon.model.erp;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2016/6/23.
 */
public class SubmitOrderItem implements Serializable {
    //分类ID
    @Expose
    public String cate_id;

    //商品ID
    @Expose
    public String goods_id;

    @Expose
    public int point_num;//点单数量

    @Expose
    public int point_type;//点单类型 0表示计费，1表示赠送，2表示套餐例送

    @Expose
    public String taste;

    @Expose
    public int combo_cate_num;

    @Expose
    public List<Combo> combo;

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

