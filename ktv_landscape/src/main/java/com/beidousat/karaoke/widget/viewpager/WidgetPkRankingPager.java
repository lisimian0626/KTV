package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.vod.PkRanking;

import java.util.List;


/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetPkRankingPager extends WidgetBasePager {


    private SongPagerAdapter mAdapter;

//    private Map<String, String> mRequestParams = new HashMap<String, String>();

    private int horizontalMargin, verticalMargin;

//    private Map<Integer, List<PkRanking>> mIndexPage;

    private int mPageSize = 8;


    public WidgetPkRankingPager(Context context) {
        super(context);
    }


    public WidgetPkRankingPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttr(attrs);
    }


    private void readAttr(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WidgetSongPage);
            this.horizontalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_horizontalMargin, 6);
            this.verticalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_verticalMargin, 6);
        }
    }


//    public void initPager(int totalPage, List<PkRanking> firstPageSongs, Map<String, String> requestParams) {
//        mIndexPage = new HashMap<Integer, List<PkRanking>>();
//        mIndexPage.put(1, firstPageSongs);
//        mRequestParams = requestParams;
//        mAdapter = new SongPagerAdapter(mContext, totalPage);
//        setAdapter(mAdapter);
//    }


    public int initPager(List<PkRanking> songs, int pageSize) {
        this.mPageSize = pageSize;
        int pages = songs.size() / mPageSize;
        if (songs.size() % mPageSize > 0) {
            pages = pages + 1;
        }
        mAdapter = new SongPagerAdapter(mContext, songs);
        setAdapter(mAdapter);
//        runLayoutAnimation(true);
        return pages;
    }

    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetPkRankingPage> sparseArray = new SparseArray<WidgetPkRankingPage>();
        private int mPageCount;
        private SparseArray<List<PkRanking>> mSongs = new SparseArray<List<PkRanking>>();

//        public SongPagerAdapter(Context context, int pageCount) {
//            mContext = context;
//            this.mPageCount = pageCount;
//        }

        public SongPagerAdapter(Context context, List<PkRanking> songs) {
            mContext = context;
            int pages = songs.size() / mPageSize;
            if (songs.size() % mPageSize > 0) {
                pages = pages + 1;
            }
            for (int i = 0; i < pages; i++) {
                int e = mPageSize * (i + 1) > songs.size() ? songs.size() : mPageSize * (i + 1);
                List<PkRanking> subList = songs.subList(i * mPageSize, e);
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
            WidgetPkRankingPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetPkRankingPage(mContext, verticalMargin, horizontalMargin);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setSong(mSongs.get(position), position + 1, mPageSize);
                }
//                else if (mIndexPage.containsKey(position + 1)) {
//                    page.setSong(mIndexPage.get(position + 1), position + 1);
//                }
//                else {
//                    page.loadSong(position + 1, mRequestParams, position + 1);
//                }
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
//            mCurrentView = (WidgetSongPage) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            WidgetPkRankingPage view = (WidgetPkRankingPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }
    }
}