package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.data.Constant;
import com.bestarmedia.libcommon.data.ShopCartData;
import com.bestarmedia.libcommon.model.erp.ShopCart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetShopCartPager extends WidgetBasePager {

    private ShopPagerAdapter mAdapter;

    private Map<Integer, List<ShopCart>> mIndexPage;

    public WidgetShopCartPager(Context context) {
        super(context);
    }


    public WidgetShopCartPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public int initPager() {
        ShopCartData mShopCartData = new ShopCartData();

        List<ShopCart> goods = mShopCartData.getGoodList();

        int pages = goods.size() / Constant.PAGE_SIZE;
        if (goods.size() % Constant.PAGE_SIZE > 0) {
            pages = pages + 1;
        }
        mIndexPage = new HashMap<Integer, List<ShopCart>>();
        for (int i = 0; i < pages; i++) {
            int e = Constant.PAGE_SIZE * (i + 1) > goods.size() ? goods.size() : Constant.PAGE_SIZE * (i + 1);
            List<ShopCart> subList = goods.subList(i * Constant.PAGE_SIZE, e);
            mIndexPage.put(i, subList);
        }
        mAdapter = new ShopPagerAdapter(mContext, pages);
        setAdapter(mAdapter);

//        runLayoutAnimation();

        return pages;
    }


    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetShopCartPage view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    public void runLayoutAnimation() {
        if (mAdapter != null) {
            WidgetShopCartPage view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation();
        }
    }

    private class ShopPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetShopCartPage> sparseArray = new SparseArray<WidgetShopCartPage>();
        private int mPageCount;

        public ShopPagerAdapter(Context context, int pageCount) {
            mContext = context;
            this.mPageCount = pageCount;
        }

        public WidgetShopCartPage getCurrentView() {
            return sparseArray.get(WidgetShopCartPager.this.getCurrentItem());
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            WidgetShopCartPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetShopCartPage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                if (mIndexPage.containsKey(position)) {
                    page.setGoods(mIndexPage.get(position));
                }
                sparseArray.put(position, page);
            } else {
                page = sparseArray.get(position);
            }
            ViewGroup parent = (ViewGroup) page.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(page);
            return page;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            WidgetShopCartPage view = (WidgetShopCartPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }

    }
}