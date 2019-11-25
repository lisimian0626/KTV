package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J Wong on 2018/10/12.
 */

public class ChargePackages implements Serializable {

    @SerializedName("item_sku")
    public List<ChargePackage> itemSku;//套餐列表

}
