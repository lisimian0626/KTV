package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.v4.SongSimple;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetSongLyricPager extends WidgetBasePager {

    private SongPagerAdapter mAdapter;
    private Map<String, String> mRequestParams = new HashMap<>();
    private int mPageSize = 8;
    private String mLyricWord;
    private int horizontalMargin, verticalMargin;
    private SparseArray<List<SongSimple>> mIndexPage;

    public WidgetSongLyricPager(Context context) {
        super(context);
    }

    public WidgetSongLyricPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttr(attrs);
    }

    private void readAttr(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WidgetSongPage);
            this.horizontalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_horizontalMargin, 6);
            this.verticalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_verticalMargin, 6);
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(getClass().getSimpleName(), "onAttachedToWindow >>>>>>>>>>>>> ");
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(getClass().getSimpleName(), "onDetachedFromWindow >>>>>>>>>>>>> ");
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Id.CHOOSE_SONG_CHANGED:
            case EventBusId.Id.SONG_DOWNLOAD_CHANGED:
                notifyCurrentPage();
                break;
            default:
                break;
        }
    }


    public void initPager(int totalPage, List<SongSimple> firstPageSongs, Map<String, String> requestParams) {
        mIndexPage = new SparseArray<>();
        mIndexPage.put(1, firstPageSongs);
        mRequestParams = requestParams;
        if (mRequestParams != null && mRequestParams.containsKey("per_page")) {
            mPageSize = Integer.valueOf(mRequestParams.get("per_page"));
        }
        if (mRequestParams != null && mRequestParams.containsKey("lyric")) {
            mLyricWord = String.valueOf(mRequestParams.get("lyric"));
        }

        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);
        runLayoutAnimation(true);
    }


    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetSongLyricPage view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (getAdapter() != null) {
            WidgetSongLyricPage view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation(fromRight);
        }
    }

    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetSongLyricPage> sparseArray = new SparseArray<>();
        private int mPageCount;
        private SparseArray<List<SongSimple>> mSongs = new SparseArray<>();

        private WidgetSongLyricPage getCurrentView() {
            return sparseArray.get(WidgetSongLyricPager.this.getCurrentItem());
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
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }

        @Override
        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            WidgetSongLyricPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetSongLyricPage(mContext, verticalMargin, horizontalMargin);
                page.setLyricWord(mLyricWord);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setSong(mSongs.get(position));
                } else if (mIndexPage.get(position + 1) != null) {
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
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            WidgetSongLyricPage view = (WidgetSongLyricPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }

    }
}