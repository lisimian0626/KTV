package com.beidousat.karaoke.ui.fragment.service;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.util.FragmentUtil;
import com.beidousat.karaoke.widget.adapter.AdtShopType;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetShopPager;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.GoodCategory;
import com.bestarmedia.libcommon.model.erp.PurchaseCategories;
import com.bestarmedia.libcommon.model.erp.Purchases;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmShop extends BaseFragment implements OnPageScrollListener, View.OnClickListener, AdtShopType.OnShopTypeClickListener, WidgetPage.OnPageChangedListener {

    private View mRootView;
    private WidgetShopPager mPager;
    private WidgetPage mWidgetPage;

    private RecyclerView mRvTypes;
    private AdtShopType mAdtShopType;

    private String mAccount, mPassword;
    private boolean mVerifyPwd = true;
    private String mCateID;

    public static FmShop newInstance(String account, String password, boolean verifyPwd) {
        FmShop fragment = new FmShop();
        Bundle args = new Bundle();
        args.putString("account", account);
        args.putString("password", password);
        args.putBoolean("verifyPwd", verifyPwd);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccount = getArguments().getString("account");
            mPassword = getArguments().getString("password");
            mVerifyPwd = getArguments().getBoolean("verifyPwd", true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            mPager.runLayoutAnimation(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_shop, null);
        mRvTypes = mRootView.findViewById(R.id.rv_types);
        mRvTypes.setHasFixedSize(true);
        if (mRvTypes.getItemAnimator() != null)
            ((SimpleItemAnimator) mRvTypes.getItemAnimator()).setSupportsChangeAnimations(false);
        mRvTypes.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mPager = mRootView.findViewById(R.id.pager);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mRootView.findViewById(R.id.tv_shopping_car).setOnClickListener(this);
        mPager.setOnPagerScrollListener(this);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(VodApplication.getVodApplicationContext())
                .color(Color.TRANSPARENT).size(8).margin(8).build();
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext());
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        mRvTypes.setLayoutManager(layoutManager3);
        mRvTypes.addItemDecoration(horDivider);
        mAdtShopType = new AdtShopType(VodApplication.getVodApplicationContext());
        mAdtShopType.setOnShopTypeClickListener(this);
        mRvTypes.setAdapter(mAdtShopType);
        requestCategory();
        return mRootView;
    }

    @Override
    public void onSuccess(String method, Object object) {
        super.onSuccess(method, object);
        if (isAdded()) {
            if (RequestMethod.NODE_PURCHASE_CATEGORY.equalsIgnoreCase(method)) {
                PurchaseCategories purchaseCategories;
                if (object instanceof PurchaseCategories && (purchaseCategories = (PurchaseCategories) object).purchase_category != null
                        && purchaseCategories.purchase_category.size() > 0) {
                    Logger.d(getClass().getSimpleName(), "category:" + purchaseCategories.purchase_category.get(0).Name);
                    mAdtShopType.setData(purchaseCategories.purchase_category);
                    mAdtShopType.notifyDataSetChanged();
                    requestGoods(purchaseCategories.purchase_category.get(0).CateID);
                }
            } else if (RequestMethod.NODE_PURCHASE.equalsIgnoreCase(method)) {
                Purchases purchases;
                if (object instanceof Purchases && (purchases = (Purchases) object).purchase != null &&
                        purchases.purchase.size() > 0) {
                    initShopPager(purchases.purchase);
                } else {
                    List<Good> goods1 = new ArrayList<>();
                    initShopPager(goods1);
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
    }

    private void requestCategory() {
        HttpRequestV4 request = initRequestV4(RequestMethod.NODE_PURCHASE_CATEGORY);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("type", String.valueOf(0));
        request.setConvert2Class(PurchaseCategories.class);
        request.get();
    }

    private void requestGoods(String cateId) {
        if (cateId != null && mCateID != null && !cateId.equals(mCateID)) {
            //获取新分类时先清空分类酒水
            List<Good> goods1 = new ArrayList<>();
            initShopPager(goods1);
        }
        mCateID = cateId;
        HttpRequestV4 request = initRequestV4(RequestMethod.NODE_PURCHASE);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("type", String.valueOf(0));
        request.addParam("cate_id", cateId);
        request.setConvert2Class(Purchases.class);
        request.get();

    }


    private void initShopPager(List<Good> goods) {
        int total = mPager.initPager(mCateID, goods);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(total);
    }


    @Override
    public void onPrePageClick(int before, int current) {
        mPager.setCurrentItem(current);
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mPager.setCurrentItem(current);
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mPager.setCurrentItem(current);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
        mPager.runLayoutAnimation(true);
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
        mPager.runLayoutAnimation(isLeft);
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
    public void onClick(View v) {
        if (v.getId() == R.id.tv_shopping_car) {
            FragmentUtil.addFragment(FmShopCart.newInstance(mAccount, mPassword, mVerifyPwd),
                    false, false, false, false);
        }
    }

    @Override
    public void onShopTypeClick(int position, GoodCategory category) {
        requestGoods(category.CateID);
    }
}
