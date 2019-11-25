package com.beidousat.karaoke.ui.fragment.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.dialog.DlgAccountPassword;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.adapter.AdtGiftCart;
import com.beidousat.karaoke.widget.viewpager.WidgetGiftCartPage;
import com.bestarmedia.libcommon.data.GiftData;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.erp.Combo;
import com.bestarmedia.libcommon.model.erp.ErpSubmitOrderResult;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;
import com.bestarmedia.libcommon.model.erp.SubmitOrder;
import com.bestarmedia.libcommon.model.erp.SubmitOrderItem;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmGiftCart extends BaseFragment implements View.OnClickListener, AdtGiftCart.OnShopCartPriceChangedListener {

    private View mRootView;
    private WidgetGiftCartPage mPager;
    private TextView mTvTotal;
    private String mAccount, mPassword;
    private Button mBtnPay, mBtnDel;

    public static FmGiftCart newInstance(String account, String password) {
        FmGiftCart fragment = new FmGiftCart();
        Bundle args = new Bundle();
        args.putString("account", account);
        args.putString("password", password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccount = getArguments().getString("account");
            mPassword = getArguments().getString("password");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_gift_cart, null);
        mPager = mRootView.findViewById(R.id.pager);
        mPager.setOnShopCartPriceChangedListener(this);
        mTvTotal = mRootView.findViewById(R.id.tv_total);
        mRootView.findViewById(R.id.btn_pay).setOnClickListener(this);
        mBtnPay = mRootView.findViewById(R.id.btn_pay);
        mBtnPay.setOnClickListener(this);
        mBtnDel = mRootView.findViewById(R.id.btn_del);
        mBtnDel.setOnClickListener(this);

        GiftData mShopCartData = new GiftData();
        List<ShopCart> goods = mShopCartData.getGoodList();
        mPager.setGoods(goods);

        boolean haveSelected = false;
        if (goods != null && goods.size() > 0)
            for (ShopCart cart : goods) {
                if (cart.isSelect) {
                    haveSelected = true;
                }
            }
        mBtnPay.setEnabled(goods != null && goods.size() > 0 && haveSelected);
        mBtnDel.setEnabled(goods != null && goods.size() > 0 && haveSelected);

        return mRootView;
    }

    @Override
    public void onRemarkClickListener(int position, String[] remarks) {

    }

    @Override
    public void onGiftClickListener(int position, List<Good> goods, int max) {
    }

    @Override
    public void onPriceChanged(double totalPrice) {
        if (isAdded()) {
            mTvTotal.setText(getString(R.string.rmb_x, totalPrice));
            GiftData mShopCartData = new GiftData();
            List<ShopCart> goods = mShopCartData.getGoodList();
            boolean haveSelected = false;
            if (goods != null && goods.size() > 0)
                for (ShopCart cart : goods) {
                    if (cart.isSelect) {
                        haveSelected = true;
                    }
                }
            mBtnPay.setEnabled(goods != null && goods.size() > 0 && haveSelected);
            mBtnDel.setEnabled(goods != null && goods.size() > 0 && haveSelected);
        }
    }

    private PromptDialogSmall mPromptDialog;

    public void tipMessage(int resId) {
        try {
            if (mPromptDialog == null || !mPromptDialog.isShowing()) {
                mPromptDialog = new PromptDialogSmall(getContext());
                mPromptDialog.setMessage(getString(resId));
                mPromptDialog.show();
            }
        } catch (Exception e) {
            Logger.d(getClass().getSimpleName(), "tipMessage ex:" + e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                if (NodeRoomInfo.getInstance().isPurchaseEnabled()) {
                    ShopCart cart = getNoSelectGiftGood();
                    if (cart == null) {//验证例送是否选择完
                        checkAccountPwdAndSubmit();
                    } else {
                        showSelectGiftTip(cart.Name);
                    }
                } else {
                    tipMessage(R.string.is_purchase_not_enabled);
                }
                break;
            case R.id.btn_del:
                PromptDialogBig dialog = new PromptDialogBig(getContext());
                dialog.setMessage(R.string.del_shop_cart_confirm2);
                dialog.setOkButton(true, getString(R.string.ok), v1 -> {
                    List<ShopCart> goods = mPager.getGoods();
                    if (goods != null && goods.size() > 0) {
                        int i;
                        boolean doDel = false;
                        for (i = goods.size() - 1; i >= 0; i--) {
                            ShopCart good = goods.get(i);
                            if (good.isSelect) {
                                mPager.removeGoodById(good.GoodsID);
                                doDel = true;
                            }
                        }
                        if (doDel) {
                            boolean haveSelected = false;
                            if (goods.size() > 0)
                                for (ShopCart cart : goods) {
                                    if (cart.isSelect) {
                                        haveSelected = true;
                                    }
                                }
                            mBtnPay.setEnabled(goods.size() > 0 && haveSelected);
                            mBtnDel.setEnabled(goods.size() > 0 && haveSelected);
                        }
                    }
                });
                dialog.setCancleButton(true, getString(R.string.cancel), null);
                dialog.show();
                break;
        }
    }

    private void checkAccountPwdAndSubmit() {
        DlgAccountPassword orderSubmit = new DlgAccountPassword(getContext(), 1);
        orderSubmit.setOnAccountPwdListener((account, pwd, isVerifyPwd) -> submitOrder(account, pwd, isVerifyPwd));
        orderSubmit.show();
    }

    private void showSelectGiftTip(String goodName) {//有例送，但未选择或未选满例送商品
        PromptDialogBig promptDialog = new PromptDialogBig(getContext());
        promptDialog.setMessage(getString(R.string.order_have_gift_no_select_tip, goodName));
        promptDialog.setOkButton(true, getString(android.R.string.yes), v -> checkAccountPwdAndSubmit());
        promptDialog.setCancleButton(true, getString(android.R.string.no), null);
        promptDialog.show();
    }

    private ShopCart getNoSelectGiftGood() {
        List<ShopCart> shopCarts = new GiftData().getGoodList();
        for (ShopCart cart : shopCarts) {
            if (cart.PointNum > 0 && cart.isSelect) {
                if ("Y".equals(cart.IsCombo) && (cart.haveGiftsNoSelect
//                        || cart.comboGoods == null || cart.comboGoods.size() <= 0
                )) {//有例送，但未选择或未选满例送商品
                    return cart;
                }
            }
        }
        return null;
    }

    private List<SubmitOrderItem> getSubmit() {
        List<SubmitOrderItem> submits = new ArrayList<>();
        List<ShopCart> shopCarts = new GiftData().getGoodList();
        for (ShopCart cart : shopCarts) {
            if (cart.PointNum > 0 && cart.isSelect) {
                SubmitOrderItem item = new SubmitOrderItem();
                item.cate_id = cart.CateID;
                item.goods_id = cart.GoodsID;
                item.point_num = cart.PointNum;
                item.point_type = 1;
                item.taste = TextUtils.isEmpty(cart.Taste) ? "" : cart.Taste;
                item.combo_cate_num = cart.ComboCateNum;
                item.combo = new ArrayList<>();
                submits.add(item);
                if (cart.comboGoods != null && cart.comboGoods.size() > 0) {//例送
                    Iterator iter = cart.comboGoods.entrySet().iterator();
                    while (iter.hasNext()) {
                        @SuppressWarnings("unchecked")
                        Map.Entry<String, List<Good>> entry = (Map.Entry) iter.next();
                        String comboId = entry.getKey();
                        List<Good> goods = entry.getValue();
                        if (goods != null && goods.size() > 0) {
                            for (Good gift : goods) {
                                if (gift.count > 0) {
                                    SubmitOrderItem item2 = new SubmitOrderItem();
                                    item2.cate_id = gift.CateID;
                                    item2.goods_id = gift.GoodsID;
                                    item2.point_num = gift.count;
                                    item2.point_type = 1;
                                    item2.taste = "";
                                    item2.combo_cate_num = cart.ComboCateNum;
                                    List<Combo> combos = new ArrayList<>();
                                    combos.add(new Combo(comboId, gift.count));
                                    item2.combo = combos;
                                    submits.add(item2);
                                }
                            }
                        }
                    }
                }
            }
        }
        return submits;
    }


    private void submitOrder(String account, String pwd, boolean isVerifyPwd) {
        List<SubmitOrderItem> submits = getSubmit();

        if (submits.size() > 0) {
            mBtnPay.setEnabled(false);
            mBtnDel.setEnabled(false);
            SubmitOrder submitOrder = new SubmitOrder();
            submitOrder.room_code = VodConfigData.getInstance().getRoomCode();
            submitOrder.card = account;
            submitOrder.password = pwd;
            submitOrder.verify_mode = isVerifyPwd ? 1 : 0;
            submitOrder.purchase_goods = submits;
            HttpRequestV4 request = initRequestV4(RequestMethod.NODE_PURCHASE);
            request.setConvert2Class(ErpSubmitOrderResult.class);
            request.postJson(submitOrder.toString());
        } else {
            PromptDialogSmall promptDialog = new PromptDialogSmall(getContext());
            promptDialog.setMessage(R.string.tip_shopping_car_empty);
            promptDialog.show();
        }

//        if (submits.size() > 0) {
//            SubmitOrder submitOrder = new SubmitOrder();
//            submitOrder.RoomCode = RoomInfo.getInstance().getRoomDetail().RoomCode;
//            submitOrder.Card = account;
//            submitOrder.Password = pwd;
//            submitOrder.VerifyMode = isVerifyPwd ? 1 : 0;
//
//            mBtnPay.setEnabled(false);
//            mBtnDel.setEnabled(false);
//            submitOrder.List = submits;
//            String json = submitOrder.toString();
//            HttpRequest zmqRequest = initRequest(RequestMethod.ZMQ_2_HTTP_LOCAL);
//            zmqRequest.addParam("sendData", json);
//            zmqRequest.doPost(0);
//        } else {
//            PromptDialog promptDialog = new PromptDialog(getActivity());
//            promptDialog.setMessage(R.string.tip_shopping_car_empty);
//            promptDialog.show();
//        }
    }

    @Override
    public void onFailed(String method, Object object) {
        String error = "";
        if (object instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) object;
            error = baseModelV4.tips;
        } else if (object instanceof String) {
            error = object.toString();
        }
        if (isAdded()) {
            if (RequestMethod.NODE_PURCHASE.equalsIgnoreCase(method)) {
                PromptDialogSmall dialog = new PromptDialogSmall(getContext());
                dialog.setMessage(error);
                dialog.show();
                mBtnPay.setEnabled(true);
                mBtnDel.setEnabled(true);
            }
        }
        super.onFailed(method, error);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.NODE_PURCHASE.equalsIgnoreCase(method)) {
                ErpSubmitOrderResult result = null;
                if (object instanceof ErpSubmitOrderResult && (result = (ErpSubmitOrderResult) object).purchase_result != null && result.purchase_result.status == 1) {
                    PromptDialogSmall dialog = new PromptDialogSmall(getContext());
                    dialog.setMessage(R.string.order_submit_success);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOkButton(true, getString(R.string.ok), v -> {
                        List<ShopCart> goods = mPager.getGoods();
                        if (goods != null && goods.size() > 0) {
                            int i;
                            boolean doDel = false;
                            for (i = goods.size() - 1; i >= 0; i--) {
                                ShopCart good = goods.get(i);
                                if (good.isSelect) {
                                    mPager.removeGoodById(good.GoodsID);
                                    doDel = true;
                                }
                            }
                            if (doDel) {
                                boolean haveSelected = false;
                                if (goods.size() > 0)
                                    for (ShopCart cart : goods) {
                                        if (cart.isSelect) {
                                            haveSelected = true;
                                        }
                                    }
                                mBtnPay.setEnabled(goods.size() > 0 && haveSelected);
                                mBtnDel.setEnabled(goods.size() > 0 && haveSelected);
                            }
                        }
                        goods = mPager.getGoods();
                        if (goods == null || goods.size() <= 0)
                            EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
                    });
                    dialog.show();
                } else {
                    String error = getString(R.string.order_submit_fail);
                    if (result != null && result.purchase_result != null) {
                        error = result.purchase_result.info;
                    }
                    PromptDialogSmall dialog = new PromptDialogSmall(getContext());
                    dialog.setMessage(error);
                    dialog.show();
                    mBtnPay.setEnabled(true);
                    mBtnDel.setEnabled(true);
                }
            }
        }
        super.onSuccess(method, object);
    }
}
