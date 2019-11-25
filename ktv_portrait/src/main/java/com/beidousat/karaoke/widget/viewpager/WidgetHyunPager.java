package com.beidousat.karaoke.widget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.model.vod.play.Hyun;
import com.bestarmedia.libwidget.util.TransformUtil;
import com.bestarmedia.libwidget.viewpager.Flip3DTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by J Wong on 2015/12/18 08:42.
 */
public class WidgetHyunPager extends WidgetBasePager {

    private HyunPagerAdapter mAdapter;

    private Map<String, String> mRequestParams = new HashMap<>();

    private Map<Integer, List<Hyun>> mIndexPage;


    public WidgetHyunPager(Context context) {
        super(context);
        TransformUtil.reverse(this, new Flip3DTransform());
    }

    public WidgetHyunPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TransformUtil.reverse(this, new Flip3DTransform());
    }


    public void initPager(int totalPage, List<Hyun> firstPageHyun, Map<String, String> requestParams) {
        mIndexPage = new HashMap<>();
        mIndexPage.put(1, firstPageHyun);
        mRequestParams = requestParams;
        mAdapter = new HyunPagerAdapter(mContext, totalPage);
        setOffscreenPageLimit(1);
        setAdapter(mAdapter);
        runLayoutAnimation();
    }

    public void runLayoutAnimation() {
        if (mAdapter != null) {
            WidgetHyunPage view = mAdapter.getCurrentView();
            if (view != null)
                view.runLayoutAnimation();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(getClass().getSimpleName(), "onAttachedToWindow >>>>>>>>>>>>> ");
        EventBus.getDefault().register(this);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(getClass().getSimpleName(), "onDetachedFromWindow >>>>>>>>>>>>> ");
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.ImId.COOL_SCREEN_CHANGED:
                notifyCurrentPage();
                break;
            default:
                break;
        }
    }

    public  void notifyCurrentPage() {
        if (mAdapter != null) {
            WidgetHyunPage view = mAdapter.getCurrentView();
            if (view != null)
                view.notifyAdapter();
        }
    }

    private class HyunPagerAdapter extends PagerAdapter {

        private Context mContext;
        private SparseArray<WidgetHyunPage> sparseArray = new SparseArray<>();
        private int mPageCount;


        public HyunPagerAdapter(Context context, int pageCount) {
            mContext = context;
            this.mPageCount = pageCount;
        }

        public WidgetHyunPage getCurrentView() {
            return sparseArray.get(WidgetHyunPager.this.getCurrentItem());
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
            WidgetHyunPage page;
            if (sparseArray.get(position) == null) {
                page = new WidgetHyunPage(mContext);
                page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                if (mIndexPage.containsKey(position + 1)) {
                    page.setHyuns(mIndexPage.get(position + 1));
                } else {
                    page.loadHyun(position + 1, mRequestParams);
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
            WidgetHyunPage view = (WidgetHyunPage) object;
            container.removeView(view);
//            sparseArray.put(position, view);
            sparseArray.delete(position);
        }
    }
}