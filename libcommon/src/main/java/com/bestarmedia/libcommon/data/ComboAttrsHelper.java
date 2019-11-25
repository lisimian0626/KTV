package com.bestarmedia.libcommon.data;

import android.content.Context;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.erp.ComboAttr;
import com.bestarmedia.libcommon.model.erp.ComboAttrsV4;
import com.bestarmedia.libcommon.model.erp.ComboItemsV4;
import com.bestarmedia.libcommon.model.erp.Good;

import java.util.List;

/**
 * Created by J Wong on 2019/1/8.
 */

public class ComboAttrsHelper implements HttpRequestListener {

    private Context mContext;
    private String mGoodId;
    private int mQuantity = 1;
    private OnComboListener mOnComboListener;

    public ComboAttrsHelper(Context context) {
        this.mContext = context;
    }

    public void loadCombos(String goodId, int quantity) {
        this.mGoodId = goodId;
        this.mQuantity = quantity;
        requestAttr();
    }


    public HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext, method);
        request.setHttpRequestListener(this);
        return request;
    }

    private void requestAttr() {
        HttpRequestV4 request = initRequest(RequestMethod.NODE_PURCHASE_COMBO);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("goods_id", mGoodId);
        request.addParam("quantity", String.valueOf(mQuantity));
        request.setConvert2Class(ComboAttrsV4.class);
        request.get();
    }

    private void requestGoods(String comboId) {
        HttpRequestV4 request = initRequest(RequestMethod.NODE_PURCHASE_COMBO_ITEM);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("combo_id", comboId);
        request.setConvert2Class(ComboItemsV4.class);
        request.get();
    }


    @Override
    public void onStart(String method) {

    }

    private List<ComboAttr> mComboAttr;

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_PURCHASE_COMBO.equalsIgnoreCase(method)) {
            ComboAttrsV4 comboAttrsV4;
            if (object instanceof ComboAttrsV4 && (comboAttrsV4 = (ComboAttrsV4) object).combo != null
                    && comboAttrsV4.combo.data != null && comboAttrsV4.combo.data.size() > 0) {
                mComboAttr = comboAttrsV4.combo.data;
                requestGoods(mComboAttr.get(0).ComboID);
            }
        } else if (RequestMethod.NODE_PURCHASE_COMBO_ITEM.equalsIgnoreCase(method)) {
            ComboItemsV4 comboItemsV4;
            if (object instanceof ComboItemsV4 && (comboItemsV4 = (ComboItemsV4) object).combo_item != null
                    && comboItemsV4.combo_item.data != null && comboItemsV4.combo_item.data.size() > 0) {
                if (mOnComboListener != null) {
                    mOnComboListener.onComboAttrsGoods(mComboAttr, comboItemsV4.combo_item.data);
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (RequestMethod.NODE_PURCHASE_COMBO.equalsIgnoreCase(method)) {
            Log.w(getClass().getSimpleName(), "读取例送方案失败了：" + obj);
        } else if (RequestMethod.NODE_PURCHASE_COMBO_ITEM.equalsIgnoreCase(method)) {
            Log.w(getClass().getSimpleName(), "读取例送方案内物品列表失败了：" + obj);
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    public void setOnComboListener(OnComboListener listener) {
        this.mOnComboListener = listener;
    }

    public interface OnComboListener {
        void onComboAttrsGoods(List<ComboAttr> comboAttrs, List<Good> goods);
    }
}
