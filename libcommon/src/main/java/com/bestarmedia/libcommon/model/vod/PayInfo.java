package com.bestarmedia.libcommon.model.vod;

import com.bestarmedia.libcommon.model.ad.AdPay;
import com.bestarmedia.libcommon.model.ad.MediaContent;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/10/16.
 */

public class PayInfo implements Serializable {

    @SerializedName("ktv_net_code")
    public String ktvNetCode;

    @SerializedName("room_code")
    public String roomCode;

    @SerializedName("is_pay")
    public int isPay;

    @SerializedName("pay_url")
    public String payUrl;

    @SerializedName("amount")
    public double amount = 0.00;

    @SerializedName("media_content")
    public MediaContent mediaContent;

    @SerializedName("ad_pay")
    public AdPay adPay;
}
