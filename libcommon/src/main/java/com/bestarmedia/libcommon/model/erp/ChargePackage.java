package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class ChargePackage implements Serializable {

    @SerializedName("_id")
    public String id;//套餐sku id

    @SerializedName("item_id")
    public String itemId;//套餐 item id

    @SerializedName("name")
    public String name;//套餐名称

    @SerializedName("description")
    public String description;//描述

    @SerializedName("type")
    public int type;//	类型 1单曲 2计时 3欢唱

    @SerializedName("price")
    public double price;//	价格

    @SerializedName("cost_price")
    public double costPrice;//	成本价

    @SerializedName("maket_price")
    public double marketPrice;//市场价

    @SerializedName("seller_id")
    public int sellerId;//商家id

}
