package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.data.Constant;
import com.bestarmedia.libcommon.model.erp.Good;

import java.util.List;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetGiftPager extends WidgetBasePager {


    private ShopPagerAdapter mAdapter;

    public WidgetGiftPager(Context context) {
        super(context);
    }


    public WidgetGiftPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private String cateID;

    public int initPager(String cateId, List<Good> goods) {
        cateID = cateId;
        int pages = goods.size() / Constant.PAGE_SIZE;
        if (goods.size() % Constant.PAGE_SIZE > 0) {
            pages = pages + 1;
        }
        mAdapter = new ShopPagerAdapter(mContext, pages, goods);
        setAdapter(mAdapter);
        return pages;
    }


    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetGiftPage view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null) {
            WidgetGiftPage view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation(fromRight);
        }
    }


    private class ShopPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetGiftPage> sparseArray = new SparseArray<>();
        private int mPageCount;
        private SparseArray<List<Good>> mGoods = new SparseArray<>();

        private ShopPagerAdapter(Context context, int pages, List<Good> goods) {
            mContext = context;
            for (int i = 0; i < pages; i++) {
                int e = Constant.PAGE_SIZE * (i + 1) > goods.size() ? goods.size() : Constant.PAGE_SIZE * (i + 1);
                List<Good> subList = goods.subList(i * Constant.PAGE_SIZE, e);
                mGoods.put(i, subList);
            }
            this.mPageCount = pages;
        }

        public WidgetGiftPage getCurrentView() {
            return sparseArray.get(WidgetGiftPager.this.getCurrentItem());
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            WidgetGiftPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetGiftPage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                if (mGoods.get(position) != null) {
                    page.setGoods(cateID, mGoods.get(position));
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
        public void setPrimaryItem(@NonNull ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
            WidgetGiftPage view = (WidgetGiftPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }
    }
}