package com.bestarmedia.libcommon.model.erp;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by J Wong on 2015/10/10 11:13.
 */
public class Good implements Serializable {

    @Expose
    public String GoodsID;//酒水编号

    @Expose
    public String Name;//酒水名称

    @Expose
    public String Unit;//酒水单位

    @Expose
    public float Price;//酒水单价

    @Expose
    public String IsCombo;//是否套餐

    @Expose
    public String Pic;//图片地址，零度才有

    @Expose
    public String CateID;//分类ID

    @Expose
    public int Number;//例送商品可选最大数量；通用协议Number>0

    @Expose
    public int count;//所选个数

    @Expose
    public int select = 0;//例送方案默认选中个数


    public ShopCart toShopCart(String cateId, int count, int pointType) {
        ShopCart shopCart = new ShopCart();
        shopCart.CateID = cateId;
        shopCart.GoodsID = GoodsID;
        shopCart.PointNum = count;
        shopCart.PointType = pointType;
        shopCart.Name = Name;
        shopCart.Unit = Unit;
        shopCart.Price = Price;
        shopCart.IsCombo = IsCombo;
        return shopCart;
    }
}
