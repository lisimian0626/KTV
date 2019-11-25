package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;

import com.beidousat.karaoke.widget.adapter.AdtGiftCart;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;
import java.util.Objects;

/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetGiftCartPage extends RecyclerView implements AdtGiftCart.OnShopCartPriceChangedListener {

    private AdtGiftCart mAdapter;

    public WidgetGiftCartPage(Context context) {
        super(context);
        init();
    }

    public WidgetGiftCartPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetGiftCartPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void notifyAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void init() {
        setHasFixedSize(true);

        ((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);

        setOverScrollMode(OVER_SCROLL_NEVER);

        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(8).margin(8).build();

        setLayoutManager(new LinearLayoutManager(getContext()));

        addItemDecoration(horDivider);

        mAdapter = new AdtGiftCart(getContext());
        mAdapter.setOnShopCartPriceChangedListener(this);
        setAdapter(mAdapter);
    }


    public void setGoods(List<ShopCart> goods) {
        mAdapter.setData(goods);
        mAdapter.notifyDataSetChanged();
    }

    public List<ShopCart> getGoods() {
        return mAdapter.getData();
    }

    public void removeGoodById(String goodId) {
        mAdapter.removeDataByGoodId(goodId);
    }


    private AdtGiftCart.OnShopCartPriceChangedListener mOnShopCartPriceChangedListener;

    public void setOnShopCartPriceChangedListener(AdtGiftCart.OnShopCartPriceChangedListener listener) {
        mOnShopCartPriceChangedListener = listener;
    }

    @Override
    public void onPriceChanged(double totalPrice) {
        if (mOnShopCartPriceChangedListener != null)
            mOnShopCartPriceChangedListener.onPriceChanged(totalPrice);
    }

    @Override
    public void onGiftClickListener(int position, List<Good> goods, int max) {
        if (mOnShopCartPriceChangedListener != null)
            mOnShopCartPriceChangedListener.onGiftClickListener(position, goods, max);
    }

    @Override
    public void onRemarkClickListener(int position, String[] remarks) {
        if (mOnShopCartPriceChangedListener != null)
            mOnShopCartPriceChangedListener.onRemarkClickListener(position, remarks);
    }
}

