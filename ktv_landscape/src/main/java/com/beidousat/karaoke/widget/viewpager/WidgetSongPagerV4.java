package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beidousat.karaoke.interf.OnPreviewSongListener;
import com.beidousat.karaoke.interf.OnSongSelectListener;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libwidget.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetSongPagerV4 extends WidgetBasePager implements OnPreviewSongListener, OnSongSelectListener {

    private SongPagerAdapter mAdapter;
    private Map<String, String> mRequestParams = new HashMap<>();
    private OnPreviewSongListener mOnPreviewSongListener;
    private OnSongSelectListener mOnSongSelectListener;
    private String mMethod;
    private int mPageSize = 8;
    private int horizontalMargin, verticalMargin;
    private boolean mIsShowSingerButton;
    private SparseArray<List<SongSimple>> mIndexPage;

    public WidgetSongPagerV4(Context context) {
        super(context);
    }

    public WidgetSongPagerV4(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttr(attrs);
    }


    private void readAttr(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WidgetSongPage);
            this.horizontalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_horizontalMargin, 6);
            this.verticalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_verticalMargin, 6);
            this.mIsShowSingerButton = a.getBoolean(R.styleable.WidgetSongPage_showSingerButton, true);
            a.recycle();
        }
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

    public void initPager(int totalPage, List<SongSimple> firstPageSongs, Map<String, String> requestParams) {
        mIndexPage = new SparseArray<>();
        mIndexPage.put(1, firstPageSongs);
        mRequestParams = requestParams;
        if (mRequestParams != null && mRequestParams.get("per_page") != null) {
            mPageSize = Integer.valueOf(mRequestParams.get("per_page"));
        }
        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);
        runLayoutAnimation(true);
    }

    public void initPager(String method, int totalPage, List<SongSimple> firstPageSongs, Map<String, String> requestParams) {
        mIndexPage = new SparseArray<>();
        mIndexPage.put(1, firstPageSongs);
        mRequestParams = requestParams;
        if (mRequestParams != null && mRequestParams.get("per_page") != null) {
            mPageSize = Integer.valueOf(mRequestParams.get("per_page"));
        }
        mMethod = method;
        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);
//        setCurrentItem(mAdapter.getCount() / 2);
        runLayoutAnimation(true);

    }

    public void initPager(List<SongSimple> songs, int pageSize) {
        this.mPageSize = pageSize;
        mAdapter = new SongPagerAdapter(mContext, songs);
        setAdapter(mAdapter);
//        setCurrentItem(mAdapter.getCount() / 2);
        runLayoutAnimation(true);
    }


    public void setOnPreviewSongListener(OnPreviewSongListener listener) {
        this.mOnPreviewSongListener = listener;
    }

    public void setOnSongSelectListener(OnSongSelectListener listener) {
        mOnSongSelectListener = listener;
    }

    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetSongPageV4 view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null) {
            WidgetSongPageV4 view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation(fromRight);
        }
    }


    @Override
    public void onSongSelectListener(SongSimple song) {
        if (mOnSongSelectListener != null)
            mOnSongSelectListener.onSongSelectListener(song);
    }

    @Override
    public void onPreviewSong(SongSimple song, int ps) {
        if (mOnPreviewSongListener != null) {
            mOnPreviewSongListener.onPreviewSong(song, ps);
        }
    }

    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetSongPageV4> sparseArray = new SparseArray<>();
        private int mPageCount;
        private SparseArray<List<SongSimple>> mSongs = new SparseArray<>();

        private WidgetSongPageV4 getCurrentView() {
            return sparseArray.get(WidgetSongPagerV4.this.getCurrentItem());
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
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            position = mPageCount > 0 ? (position % mPageCount) : position;
            WidgetSongPageV4 page;
            if (sparseArray.get(position) == null) {
                page = new WidgetSongPageV4(mContext, verticalMargin, horizontalMargin, mIsShowSingerButton);
                page.setOnPreviewSongListener(WidgetSongPagerV4.this);
                page.setOnSongSelectListener(WidgetSongPagerV4.this);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setSong(mSongs.get(position));
                } else if (mIndexPage.get(position + 1) != null) {
                    page.setSong(mIndexPage.get(position + 1));
                } else if (TextUtils.isEmpty(mMethod)) {
                    page.loadSong(position + 1, mRequestParams);
                } else {
                    page.loadSong(mMethod, position + 1, mRequestParams);
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
            position = mPageCount > 0 ? (position % mPageCount) : position;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            WidgetSongPageV4 view = (WidgetSongPageV4) object;
            container.removeView(view);
            sparseArray.delete(mPageCount > 0 ? (position % mPageCount) : position);
        }

    }
}