package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2016/6/23.
 */
public class Consume implements Serializable {

    @Expose
    public String ID;//商品ID

    @Expose
    public String Name;//商品名称

    @Expose
    public String Unit;//单位

    @Expose
    private String Price;//价格

    @Expose
    private String PointNum;//个数

    @Expose
    public String PointType;//类型，0计费，1赠送，3例送

    public int getPointNum() {
        int num = 0;
        try {
            float numF = Float.valueOf(PointNum);
            num = (int) numF;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public float getPrice() {
        float price = 0;
        try {
            price = Float.valueOf(Price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }
}
