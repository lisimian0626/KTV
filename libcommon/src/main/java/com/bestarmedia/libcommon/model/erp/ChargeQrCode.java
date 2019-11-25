package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/12.
 */

public class ChargeQrCode implements Serializable {

    @SerializedName("code_url")
    public String codeUrl;//付款码

    //套餐类型
    public int type;

    //套餐详情
    public String detail;

    //价格
    public double price;

    //付款方式
    public int payType;

}
