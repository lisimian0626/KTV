package com.bestarmedia.libcommon.data;

import com.bestarmedia.libcommon.model.erp.ShopCart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2016/5/3.
 */
public class GiftData {

    private static Map<String, ShopCart> mData = new HashMap<>();


    public void addGood(ShopCart shopCart) {
        mData.put(shopCart.GoodsID, shopCart);
    }

    public ShopCart getGoodById(String id) {
        if (mData.containsKey(id)) {
            return mData.get(id);
        }
        return null;
    }


    public List<ShopCart> getGoodList() {
        List<ShopCart> goods = new ArrayList<>();
        for (Map.Entry<String, ShopCart> entry : mData.entrySet()) {
//            ShopCart good = entry.getValue();
//            if (good != null && good.count > 0) {
            goods.add(entry.getValue());
//            }
        }
        return goods;
    }

    public void cleanData() {
        mData.clear();
    }

    public void removeGood(String goodId) {
        mData.remove(goodId);
    }
}
