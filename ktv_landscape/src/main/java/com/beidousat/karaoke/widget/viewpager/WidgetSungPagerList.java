package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.model.vod.RoomSongItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetSungPagerList extends WidgetBasePager {

    private SongPagerAdapter mAdapter;
    private SparseArray<List<RoomSongItem>> mIndexPage;

    public WidgetSungPagerList(Context context) {
        super(context);
    }

    public WidgetSungPagerList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initPager(int totalPage, List<RoomSongItem> firstPageSongs, Map<String, String> requestParams) {
        mIndexPage = new SparseArray<>();
        mIndexPage.put(1, firstPageSongs);
        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);
//        runLayoutAnimation(true);
    }

    public void runLayoutAnimation() {
        if (mAdapter != null) {
            WidgetSungPageList view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation();
        }
    }

    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetSungPageList> sparseArray = new SparseArray<>();
        private int mPageCount;

        private SongPagerAdapter(Context context, int pageCount) {
            mContext = context;
            this.mPageCount = pageCount;
        }

        public WidgetSungPageList getCurrentView() {
            return sparseArray.get(WidgetSungPagerList.this.getCurrentItem());
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
            WidgetSungPageList page;
            if (sparseArray.get(position) == null) {
                page = new WidgetSungPageList(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mIndexPage.get(position + 1) != null) {
                    page.setSong(mIndexPage.get(position + 1));
                } else {
                    page.requestSongs(position + 1);
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
            WidgetSungPageList view = (WidgetSungPageList) object;
            container.removeView(view);
            sparseArray.delete(position);
        }

    }
}