package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libwidget.util.TransformUtil;
import com.bestarmedia.libwidget.viewpager.Flip3DTransform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetSingerPager extends WidgetBasePager {

    private SingerPagerAdapter mAdapter;
    private Map<String, String> mRequestParams = new HashMap<>();
    private SparseArray<List<Start>> mIndexPage;

    public WidgetSingerPager(Context context) {
        super(context);
        TransformUtil.reverse(this, new Flip3DTransform());
    }

    public WidgetSingerPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TransformUtil.reverse(this, new Flip3DTransform());
    }

    public void initPager(int totalPage, List<Start> firstPageSinger, Map<String, String> requestParams) {
        mIndexPage = new SparseArray<>();
        mIndexPage.put(1, firstPageSinger);
        mRequestParams = requestParams;
        mAdapter = new SingerPagerAdapter(mContext, totalPage);
        setOffscreenPageLimit(3);
        setAdapter(mAdapter);
        runLayoutAnimation();
    }

    public void runLayoutAnimation() {
        if (mAdapter != null) {
            WidgetSingerPage view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation();
        }
    }

    private class SingerPagerAdapter extends PagerAdapter {
        private Context mContext;
        private SparseArray<WidgetSingerPage> sparseArray = new SparseArray<>();
        private int mPageCount;

        private SingerPagerAdapter(Context context, int pageCount) {
            mContext = context;
            this.mPageCount = pageCount;
        }

        private WidgetSingerPage getCurrentView() {
            return sparseArray.get(WidgetSingerPager.this.getCurrentItem());
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            WidgetSingerPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetSingerPage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mIndexPage.get(position + 1) != null) {
                    page.setSinger(mIndexPage.get(position + 1));
                } else {
                    page.loadSinger(position + 1, mRequestParams);
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
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            WidgetSingerPage view = (WidgetSingerPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }
    }
}