package com.beidousat.karaoke.ui.fragment.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.dialog.DlgAccountPassword;
import com.beidousat.karaoke.ui.dialog.PromptDialogSmall;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.widget.viewpager.WidgetConsumePager;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.erp.Charge;
import com.bestarmedia.libcommon.model.erp.PaymentsV4;
import com.bestarmedia.libcommon.model.erp.PurchaseDetailsV4;
import com.bestarmedia.libcommon.util.Logger;

/**
 * Created by J Wong on 2015/10/9 12:06.
 */
public class FmBill extends BaseFragment implements View.OnClickListener, OnPageScrollListener, WidgetPage.OnPageChangedListener {

    private View mRootView;
    private WidgetConsumePager mWidgetConsumePager;
    private TextView mTvRoom, mTvRoomType, mTvDiscount, mTvMin, mTvCur, mTvMinBalance;
    private WidgetPage mWidgetPage;
    private PromptDialogSmall mPromptDialog;
    private int mTotalPage;
    private int mCurPage = 0;

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_bill, null);
        mWidgetConsumePager = mRootView.findViewById(R.id.pager);
        mWidgetConsumePager.setOnPagerScrollListener(this);
        mTvRoom = mRootView.findViewById(R.id.tv_room);
        mTvRoomType = mRootView.findViewById(R.id.tv_room_type);
        mTvDiscount = mRootView.findViewById(R.id.tv_discount);
        mTvMin = mRootView.findViewById(R.id.tv_min);
        mTvCur = mRootView.findViewById(R.id.tv_cur);
        mTvMinBalance = mRootView.findViewById(R.id.tv_min_balance);
        mRootView.findViewById(R.id.btn_shop).setOnClickListener(this);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mWidgetPage.setPageNumTextColor(R.drawable.selector_page_text_gray);
        mWidgetPage.setPrePageTextColor(R.drawable.selector_page_text_gray);
        mWidgetPage.setNextPageTextColor(R.drawable.selector_page_text_gray);
        requestCharge();
        try {
            mTvRoom.setText(NodeRoomInfo.getInstance().getRoomName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mRootView;
    }

    @Override
    public void onPrePageClick(int before, int current) {
        mWidgetConsumePager.setCurrentItem(current);
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mWidgetConsumePager.setCurrentItem(current);
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mWidgetConsumePager.setCurrentItem(current);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollLeft() {
        mWidgetPage.setPrePressed(true);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollRight() {
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(true);
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
        if (RequestMethod.NODE_PURCHASE_BILL.equalsIgnoreCase(method)) {
            requestBills();
        }
    }

    @Override
    public void onSuccess(String method, Object object) {
        super.onSuccess(method, object);
        if (isAdded()) {
            if (RequestMethod.NODE_PURCHASE_BILL.equalsIgnoreCase(method)) {
                requestBills();
                PaymentsV4 paymentsV4;
                if (object instanceof PaymentsV4 && (paymentsV4 = (PaymentsV4) object).payment != null && paymentsV4.payment.data != null) {
                    Charge charge = paymentsV4.payment.data;
                    mTvRoomType.setText(getString(R.string.open_room_time_x, charge.CheckInTime));
                    mTvDiscount.setText(getString(R.string.discount_x, charge.DrinksDiscountRate));
                    mTvMin.setText(getString(R.string.min_consume_x, charge.MinConsume));
                    mTvCur.setText(getString(R.string.cur_consume_x, charge.CurrentConsume));
                    mTvMinBalance.setText(getString(R.string.min_balance_x, charge.MinBalance));
                    try {
                        float min = Float.valueOf(charge.MinConsume);
                        float cur = Float.valueOf(charge.CurrentConsume);
                        float minBalance = Float.valueOf(charge.MinBalance);
                        if (cur >= min && minBalance > 0) {//部分消费不纳入低消
                            mTvCur.setText(getString(R.string.cur_consume_x, charge.CurrentConsume) + getString(R.string.include_no_min_consume));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else if (RequestMethod.NODE_PURCHASE_BILL_DETAIL.equalsIgnoreCase(method)) {
                PurchaseDetailsV4 purchaseDetailsV4;
                if (object instanceof PurchaseDetailsV4 && (purchaseDetailsV4 = (PurchaseDetailsV4) object).purchase_detail != null
                        && purchaseDetailsV4.purchase_detail.data != null && purchaseDetailsV4.purchase_detail.data.size() > 0) {
                    mTotalPage = mWidgetConsumePager.initPager(purchaseDetailsV4.purchase_detail.data);
                    mWidgetPage.setPageCurrent(mCurPage);
                    mWidgetPage.setPageTotal(mTotalPage);
                }
            }
        }
    }

    private void requestCharge() {
        HttpRequestV4 request = initRequestV4(RequestMethod.NODE_PURCHASE_BILL);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.setConvert2Class(PaymentsV4.class);
        request.get();
    }

    private void requestBills() {
        if (getContext() != null) {
            HttpRequestV4 request = initRequestV4(RequestMethod.NODE_PURCHASE_BILL_DETAIL);
            request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
            request.addParam("query_type", String.valueOf(0));
            request.setConvert2Class(PurchaseDetailsV4.class);
            request.get();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_shop) {
            if (NodeRoomInfo.getInstance().isPurchaseEnabled()) {
                if (VodConfigData.getInstance().verifyPasswordEnterWine()) {
                    DlgAccountPassword orderSubmit = new DlgAccountPassword(getContext(), 0);
                    orderSubmit.setOnAccountPwdListener((account, pwd, isVerifyPwd) ->
                            FragmentUtil.addFragment(FmShop.newInstance(account, pwd, isVerifyPwd), false, false, false, false));
                    orderSubmit.show();
                } else {
                    FragmentUtil.addFragment(FmShop.newInstance(null, null, true), false, false, false, false);
                }
            } else {
                tipMessage(R.string.is_purchase_not_enabled);
            }
        }
    }

    private void tipMessage(int resId) {
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

}
