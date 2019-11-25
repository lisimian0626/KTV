package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.model.vod.Live;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetLivePager extends WidgetBasePager {


    private SongPagerAdapter mAdapter;

    private Map<String, String> mRequestParams = new HashMap<String, String>();
    private Map<Integer, List<Live>> mIndexPage;


    public WidgetLivePager(Context context) {
        super(context);
    }

    public WidgetLivePager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initPager(int totalPage, List<Live> lives, Map<String, String> requestParams) {
        mIndexPage = new HashMap<>();
        mIndexPage.put(1, lives);
        mRequestParams = requestParams;
        mAdapter = new SongPagerAdapter(mContext, totalPage);
        setAdapter(mAdapter);

        runLayoutAnimation(true);

    }

    public void runLayoutAnimation(boolean fromRight) {
        if (mAdapter != null) {
            WidgetLivePage view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation(fromRight);
        }
    }


    private class SongPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetLivePage> sparseArray = new SparseArray<>();
        private int mPageCount;

        public SongPagerAdapter(Context context, int pageCount) {
            mContext = context;
            this.mPageCount = pageCount;
        }

        public WidgetLivePage getCurrentView() {
            return sparseArray.get(WidgetLivePager.this.getCurrentItem());
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
        public Object instantiateItem(ViewGroup container, final int position) {
            WidgetLivePage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetLivePage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mIndexPage.containsKey(position + 1)) {
                    page.setLives(mIndexPage.get(position + 1));
                } else {
                    page.loadLive(position + 1, mRequestParams);
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
            WidgetLivePage view = (WidgetLivePage) object;
            container.removeView(view);
            sparseArray.delete(position);
        }

    }
}