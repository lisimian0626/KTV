package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetGameSongPager extends WidgetBasePager {


    private SongPagerAdapter mAdapter;

    private Map<String, String> mRequestParams = new HashMap<String, String>();

    private String mMethod;

    private int mPageSize = 8;

    private int horizontalMargin, verticalMargin;

    private Map<Integer, List<SongSimple>> mIndexPage;


    public WidgetGameSongPager(Context context) {
        super(context);
    }


    public WidgetGameSongPager(Context context, AttributeSet attrs) {
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


    public void initPager(int totalPage, List<SongSimple> firstPageSongs, Map<String, String> requestParams) {
        mIndexPage = new HashMap<>();
        mIndexPage.put(1, firstPageSongs);
        mRequestParams = requestParams;
        if (mRequestParams.containsKey("per_page")) {
            mPageSize = Integer.valueOf(mRequestParams.get("per_page"));
        }
        setOffscreenPageLimit(3);
        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);
    }

    public void initPager(List<SongSimple> songs) {
        mAdapter = new SongPagerAdapter(mContext, songs);
        setAdapter(mAdapter);
    }

    public void cleanSelected() {
        if (mAdapter != null) {
            SparseArray<WidgetGameSong> pages = mAdapter.getPages();
            if (pages != null) {
                for (int i = 0; i < pages.size(); i++) {
                    try {
                        if (pages.get(i) != null) {
                            pages.get(i).setSelectedSong(-1);
                        }
                    } catch (Exception ex) {
                        Logger.w("WidgetGameSongPager", "cleanSelected ex:" + ex.toString());
                    }
                }
            }
        }
    }

    public void setCurSelectedSong(int ps) {
        if (mAdapter != null) {
            WidgetGameSong view = mAdapter.getCurrentView();
            if (view != null)
                view.setSelectedSong(ps);
        }
    }


    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetGameSong view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    public int getSelectedPs() {
        if (mAdapter != null) {
            WidgetGameSong view = mAdapter.getCurrentView();
            if (view != null)
                return view.getSelectedPosition();
        }
        return -1;
    }

    public SongSimple getCurSelectedSong() {
        if (mAdapter != null) {
            WidgetGameSong view = mAdapter.getCurrentView();
            if (view != null)
                return view.getSelectedSong();
        }
        return null;
    }

    public void setCheckable(boolean checkable) {
        if (mAdapter != null) {
            SparseArray<WidgetGameSong> pages = mAdapter.getPages();
            if (pages != null) {
                for (int i = 0; i < pages.size(); i++) {
                    if (pages.get(i) != null)
                        pages.get(i).setCheckable(checkable);
                }
            }
        }
    }


    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetGameSong> sparseArray = new SparseArray<>();
        private int mPageCount;
        private SparseArray<List<SongSimple>> mSongs = new SparseArray<>();

        public WidgetGameSong getCurrentView() {
            return sparseArray.get(WidgetGameSongPager.this.getCurrentItem());
        }

        public SparseArray<WidgetGameSong> getPages() {
            return sparseArray;
        }

        private SongPagerAdapter(Context context, int pageCount) {
            mContext = context;
            this.mPageCount = pageCount;
        }

        private SongPagerAdapter(Context context, List<SongSimple> songs) {
            mContext = context;
            int pages = songs.size() / mPageSize;
            if (songs.size() % mPageSize > 0) {
                pages = pages + 1;
            }

            for (int i = 0; i < pages; i++) {
                int e = mPageSize * (i + 1) > songs.size() ? songs.size() : mPageSize * (i + 1);
                List<SongSimple> subList = songs.subList(i * mPageSize, e);
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

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            WidgetGameSong page;
            if (sparseArray.get(position) == null) {
                page = new WidgetGameSong(mContext, verticalMargin, horizontalMargin);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setSong(mSongs.get(position));
                } else if (mIndexPage.containsKey(position + 1)) {
                    page.setSong(mIndexPage.get(position + 1));
                } else {
                    page.loadSong(position + 1, mRequestParams);
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
            WidgetGameSong view = (WidgetGameSong) object;
            container.removeView(view);
            sparseArray.delete(position);
        }

    }
}