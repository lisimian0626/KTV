package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.model.vod.MvInfo;

import java.util.List;


/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetMvPager extends WidgetBasePager {

    private int mPageSize = 8;

    private SongPagerAdapter mAdapter;

    public WidgetMvPager(Context context) {
        super(context);
    }

    public WidgetMvPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public int initPager(List<MvInfo> songs, int pageSize) {
        this.mPageSize = pageSize;
        int pages = songs.size() % mPageSize == 0 ? (songs.size() / mPageSize) : (songs.size() / mPageSize + 1);
        mAdapter = new SongPagerAdapter(mContext, songs, pages);
        setAdapter(mAdapter);
//        runLayoutAnimation(true);
        return pages;
    }

    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetMvPage view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }


    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetMvPage> sparseArray = new SparseArray<WidgetMvPage>();
        private int mPageCount;
        private SparseArray<List<MvInfo>> mSongs = new SparseArray<List<MvInfo>>();

        public SongPagerAdapter(Context context, int pages) {
            mContext = context;
            this.mPageCount = pages;
        }

        public SongPagerAdapter(Context context, List<MvInfo> songs, int pages) {
            mContext = context;
            for (int i = 0; i < pages; i++) {
                int e = mPageSize * (i + 1) > songs.size() ? songs.size() : mPageSize * (i + 1);
                List<MvInfo> subList = songs.subList(i * mPageSize, e);
                mSongs.put(i, subList);
            }
            this.mPageCount = pages;
        }

        public WidgetMvPage getCurrentView() {
            return sparseArray.get(WidgetMvPager.this.getCurrentItem());
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
            WidgetMvPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetMvPage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setMv(mSongs.get(position));
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
            WidgetMvPage view = (WidgetMvPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }
    }
}