package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.v4.SongSimple;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetDownloadPager extends WidgetBasePager {

    private SongPagerAdapter mAdapter;

    private final static int PAGE_SIZE = 8;

    public WidgetDownloadPager(Context context) {
        super(context);
    }

    public WidgetDownloadPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onAttachedToWindow();
    }

    @Subscribe
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.Id.SONG_DOWNLOAD_CHANGED) {
            notifyCurrentPage();
        }
    }

    public void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetDownloadPage view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    public int initPager(List<SongSimple> data) {
        int pages = data.size() % PAGE_SIZE == 0 ? (data.size() / PAGE_SIZE) : (data.size() / PAGE_SIZE + 1);
        mAdapter = new SongPagerAdapter(mContext, data);
        setAdapter(mAdapter);
        return pages;
    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null) {
            WidgetDownloadPage view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation(fromRight);
        }
    }


    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetDownloadPage> sparseArray = new SparseArray<>();
        private int mPageCount;
        private SparseArray<List<SongSimple>> mSongs = new SparseArray<>();

        private SongPagerAdapter(Context context, List<SongSimple> songs) {
            mContext = context;
            int pages = songs.size() % PAGE_SIZE == 0 ? (songs.size() / PAGE_SIZE) : (songs.size() / PAGE_SIZE + 1);
            if (pages > 0) {
                for (int i = 0; i < pages; i++) {
                    int e = PAGE_SIZE * (i + 1) > songs.size() ? songs.size() : PAGE_SIZE * (i + 1);
                    List<SongSimple> subList = songs.subList(i * PAGE_SIZE, e);
                    mSongs.put(i, subList);
                }
            }
            this.mPageCount = pages;
        }

        public WidgetDownloadPage getCurrentView() {
            return sparseArray.get(WidgetDownloadPager.this.getCurrentItem());
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
            WidgetDownloadPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetDownloadPage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mSongs.get(position) != null) {
                    page.setSong(mSongs.get(position));
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
            WidgetDownloadPage view = (WidgetDownloadPage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }
    }
}