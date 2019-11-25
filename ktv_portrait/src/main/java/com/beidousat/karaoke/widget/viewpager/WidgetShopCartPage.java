package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.widget.adapter.AdtShopCart;
import com.bestarmedia.libcommon.model.erp.Good;
import com.bestarmedia.libcommon.model.erp.ShopCart;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;


/**
 * Created by J Wong on 2015/12/17 18:01.
 */
public class WidgetShopCartPage extends RecyclerView implements AdtShopCart.OnShopCartPriceChangedListener {

    private AdtShopCart mAdapter;

    public WidgetShopCartPage(Context context) {
        super(context);
        init();
    }

    public WidgetShopCartPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetShopCartPage(Context context, AttributeSet attrs, int defStyleAttr) {
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
//
//        VerticalDividerItemDecoration2 verDivider = new VerticalDividerItemDecoration2.Builder(getContext())
//                .color(Color.TRANSPARENT).size(6).margin(6).build();


        setLayoutManager(new LinearLayoutManager(getContext()));

        addItemDecoration(horDivider);
//        addItemDecoration(verDivider);

        mAdapter = new AdtShopCart(getContext());
        mAdapter.setOnShopCartPriceChangedListener(this);
        setAdapter(mAdapter);
    }

    public void runLayoutAnimation() {
        if (mAdapter != null) {
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
            setLayoutAnimation(controller);
            getAdapter().notifyDataSetChanged();
            scheduleLayoutAnimation();
        }
    }


    public void setGoods(List<ShopCart> goods) {
        mAdapter.setData(goods);
    }

    public List<ShopCart> getGoods() {
        return mAdapter.getData();
    }

    public void removeGoodById(String goodId) {
        mAdapter.removeDataByGoodId(goodId);
    }

    private AdtShopCart.OnShopCartPriceChangedListener mOnShopCartPriceChangedListener;

    public void setOnShopCartPriceChangedListener(AdtShopCart.OnShopCartPriceChangedListener listener) {
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

