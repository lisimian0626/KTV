package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2016/6/23.
 */
public class ShopCart implements Serializable {

    //分类ID
    @Expose
    public String CateID;

    //商品ID
    @Expose
    public String GoodsID;

    @Expose
    public int PointNum;//点单数量

    @Expose
    public int PointType;//点单类型 0表示计费，1表示赠送，2表示套餐例送

    @Expose
    public String Taste;

    @Expose
    public int ComboCateNum;

    @Expose
    public String Name;

    @Expose
    public String Unit;

    @Expose
    public float Price;

//    @Expose
//    public List<Good> gifts;
//    @Expose
//    public int UseNum;
//    @Expose
//    public String comboId;

    public Map<String, List<Good>> comboGoods = new HashMap<>();


    @Expose
    public String IsCombo;


    public boolean isSelect = true;

    public boolean haveGiftsNoSelect = true;
}
