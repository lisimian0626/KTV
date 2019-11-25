package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.model.erp.Consume;

import java.util.List;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetConsumePager extends WidgetBasePager {

    private int mPageSize = 7;
    private SongPagerAdapter mAdapter;

    public WidgetConsumePager(Context context) {
        super(context);
    }

    public WidgetConsumePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public int initPager(List<Consume> consumes) {
        int pages = consumes.size() / mPageSize;
        if (consumes.size() % mPageSize > 0) {
            pages = pages + 1;
        }
        mAdapter = new SongPagerAdapter(mContext, pages, consumes);
        setAdapter(mAdapter);
        return pages;
    }

    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetConsumePage> sparseArray = new SparseArray<>();
        private int mPageCount;
        private SparseArray<List<Consume>> mSongs = new SparseArray<>();

        private SongPagerAdapter(Context context, int pages, List<Consume> consumes) {
            mContext = context;
            for (int i = 0; i < pages; i++) {
                int e = mPageSize * (i + 1) > consumes.size() ? consumes.size() : mPageSize * (i + 1);
                List<Consume> subList = consumes.subList(i * mPageSize, e);
                mSongs.put(i, subList);
            }
            this.mPageCount = pages;
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, Object obj) {
            return view == obj;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            WidgetConsumePage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetConsumePage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setConsumes(mSongs.get(position));
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
            WidgetConsumePage view = (WidgetConsumePage) object;
            container.removeView(view);
            sparseArray.delete(position);
//            sparseArray.put(position, view);
        }
    }
}