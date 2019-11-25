package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.model.vod.SongUpdateLog;

import java.util.List;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetUpdateLogPager extends WidgetBasePager {


    private SongPagerAdapter mAdapter;
    private final static int PAGE_SIZE = 5;

    public WidgetUpdateLogPager(Context context) {
        super(context);
    }

    public WidgetUpdateLogPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public int initPager(List<SongUpdateLog.SongLog> logs) {
        int pages = logs.size() / PAGE_SIZE;
        if (logs.size() % PAGE_SIZE > 0) {
            pages = pages + 1;
        }
        mAdapter = new SongPagerAdapter(mContext, pages, logs);
        setAdapter(mAdapter);
        return pages;
    }


    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetUpdateLogPage> sparseArray = new SparseArray<WidgetUpdateLogPage>();
        private int mPageCount;
        private SparseArray<List<SongUpdateLog.SongLog>> mSongs = new SparseArray<List<SongUpdateLog.SongLog>>();

        public SongPagerAdapter(Context context, int pages, List<SongUpdateLog.SongLog> logs) {
            mContext = context;
            for (int i = 0; i < pages; i++) {
                int e = PAGE_SIZE * (i + 1) > logs.size() ? logs.size() : PAGE_SIZE * (i + 1);
                List<SongUpdateLog.SongLog> subList = logs.subList(i * PAGE_SIZE, e);
                mSongs.put(i, subList);
            }
            this.mPageCount = pages;
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
            WidgetUpdateLogPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetUpdateLogPage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setLogs(mSongs.get(position));
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
            WidgetUpdateLogPage view = (WidgetUpdateLogPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }
    }
}