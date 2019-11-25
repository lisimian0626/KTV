package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2016/6/23.
 */
public class Charge implements Serializable {
    @Expose
    private String RoomCode;    //房间编号
    @Expose
    public String KTVRoomCode;
    @Expose
    public String CheckInTime;//开房时间
    @Expose
    public String MaxQuota;//	最高限额
    @Expose
    public String DrinksDiscountRate;//酒水折扣
    @Expose
    public String RoomFeeDiscountRate;//房费折扣
    @Expose
    public String RoomFee;//包房费用
    @Expose
    public String ServiceFee;//服务费用
    @Expose
    public String HourFee;//钟点费
    @Expose
    public String DrinksFee;//酒水费
    @Expose
    public String MinConsume;//最低消费
    @Expose
    public String CurrentConsume;//当前消费
    @Expose
    public String MinBalance;//低消差额
    @Expose
    public String ConsumeTime;//	消费时间
    @Expose
    public String ConsumeTotal;//	消费总计
    @Expose
    public String RealPrice;//	实收金额

}
