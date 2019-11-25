package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.vod.Pk;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetPkPager extends WidgetBasePager {


    private SongPagerAdapter mAdapter;

    private Map<String, String> mRequestParams = new HashMap<String, String>();


    private String mMethod;

    private int mPageSize = 8;

    private int horizontalMargin, verticalMargin;

    private Map<Integer, List<Pk>> mIndexPage;


    public WidgetPkPager(Context context) {
        super(context);
//        init();
    }


    public WidgetPkPager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
        readAttr(attrs);
    }


    private void readAttr(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WidgetSongPage);
            this.horizontalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_horizontalMargin, 6);
            this.verticalMargin = a.getDimensionPixelSize(R.styleable.WidgetSongPage_verticalMargin, 6);
        }
    }

//    private void init() {
//        EventBus.getDefault().register(this);
//    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.Id.CHOOSE_SONG_CHANGED:
                notifyCurrentPage();
                break;
            default:
                break;
        }
    }


    public void initPager(int totalPage, List<Pk> firstPageSongs, Map<String, String> requestParams) {
        mIndexPage = new HashMap<Integer, List<Pk>>();
        mIndexPage.put(1, firstPageSongs);
        mRequestParams = requestParams;
        if (mRequestParams.containsKey("Nums")) {
            mPageSize = Integer.valueOf(mRequestParams.get("Nums"));
        }
        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);
    }

    public void initPager(String method, int totalPage, List<Pk> firstPageSongs, Map<String, String> requestParams) {
        mIndexPage = new HashMap<Integer, List<Pk>>();
        mIndexPage.put(1, firstPageSongs);
        mRequestParams = requestParams;
        if (mRequestParams.containsKey("Nums")) {
            mPageSize = Integer.valueOf(mRequestParams.get("Nums"));
        }
        mMethod = method;
        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);
    }

    public void initPager(List<Pk> songs) {
        mAdapter = new SongPagerAdapter(mContext, songs);
        setAdapter(mAdapter);
    }


    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetPkPage view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetPkPage> sparseArray = new SparseArray<WidgetPkPage>();
        private int mPageCount;
        private SparseArray<List<Pk>> mSongs = new SparseArray<List<Pk>>();

        public WidgetPkPage getCurrentView() {
            return sparseArray.get(WidgetPkPager.this.getCurrentItem());
        }


        public SongPagerAdapter(Context context, int pageCount) {
            mContext = context;
            this.mPageCount = pageCount;
        }

        public SongPagerAdapter(Context context, List<Pk> songs) {
            mContext = context;
            int pages = songs.size() / mPageSize;
            if (songs.size() % mPageSize > 0) {
                pages = pages + 1;
            }

            for (int i = 0; i < pages; i++) {
                int e = mPageSize * (i + 1) > songs.size() ? songs.size() : mPageSize * (i + 1);
                List<Pk> subList = songs.subList(i * mPageSize, e);
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
            WidgetPkPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetPkPage(mContext, verticalMargin, horizontalMargin);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setSong(mSongs.get(position));
                } else if (mIndexPage.containsKey(position + 1)) {
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
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            mCurrentView = (WidgetSongPage) object;
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            WidgetPkPage view = (WidgetPkPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }

    }
}