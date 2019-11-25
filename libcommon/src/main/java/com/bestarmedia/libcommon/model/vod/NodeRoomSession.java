package com.bestarmedia.libcommon.model.vod;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by J Wong on 2018/9/30.
 */

public class NodeRoomSession implements Serializable {

    @SerializedName("id")
    public String id;

//    @SerializedName("session")
//    public String session;

    @SerializedName("ktv_net_code")
    public String ktvNetCode;

    @SerializedName("ktv_name")
    public String ktvName;

//    @SerializedName("province_code")
//    public String provinceCode;

//    @SerializedName("city_code")
//    public String cityCode;

//    @SerializedName("area_code")
//    public String areaCode;

    @SerializedName("room_code")
    public String roomCode;

    @SerializedName("description")
    public String description;

    @SerializedName("is_purchase_enabled")
    public int isPurchaseEnabled;

//    @SerializedName("check_type")
//    public int checkType;

//    @SerializedName("erp_people_number")
//    public int erpPeopleNumber;

//    @SerializedName("erp_total_fee")
//    public int erpTotalFee;

//    @SerializedName("check_in_time")
//    public String checkInTime;

//    @SerializedName("check_out_time")
//    public String checkOutTime;

}
