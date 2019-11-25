package com.bestarmedia.libwidget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libwidget.R;
import com.bestarmedia.libwidget.image.RecyclerImageView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by J Wong on 2017/5/19.
 */

public class BannerBase extends RelativeLayout implements HttpRequestListener, View.OnClickListener {

    private static final int VIEW_PAGER_SCROLL_DURATION = 1000;
    protected Context mContext;
    View mRootView;
    protected JazzyViewPager mPager;
    protected RecyclerImageView mRivNext, mRivPre;

    public BannerBase(Context context) {
        super(context);
        initView(context);
    }

    public BannerBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.ad_supporter_player, this);
        mPager = mRootView.findViewById(R.id.jViewPager);

        ViewPagerScroller.setViewPagerScrollDuration(mPager, VIEW_PAGER_SCROLL_DURATION);
        String[] effects = getResources().getStringArray(R.array.jazzy_effects);
        JazzyViewPager.TransitionEffect effect = JazzyViewPager.TransitionEffect.valueOf(effects[effects.length - 1]);
        mPager.setTransitionEffect(effect);

        mRivNext = mRootView.findViewById(R.id.riv_pre);
        mRivNext.setOnClickListener(this);
        mRivPre = mRootView.findViewById(R.id.riv_next);
        mRivPre.setOnClickListener(this);

    }


    HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(getContext().getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    private boolean mIsPlaying;

    private ScheduledExecutorService mScheduledExecutorService;

    public void startPlayer() {
        if (mIsPlaying || (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()))
            return;
        mScheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduleAtFixedRate(mScheduledExecutorService);
        mIsPlaying = true;
    }

    public void stopPlayer() {
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdownNow();
            mScheduledExecutorService = null;
            mIsPlaying = false;
        }
    }

    private void scheduleAtFixedRate(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() ->
                        BannerBase.this.post(() -> toNextPage())
                , 0, 5, TimeUnit.SECONDS);
    }

    public void toPrePage() {

    }

    public void toNextPage() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.riv_pre) {
            toPrePage();
        } else if (i == R.id.riv_next) {
            toNextPage();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;

    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {

    }

    @Override
    public void onFailed(String method, Object obj) {

    }

    @Override
    public void onError(String method, String error) {

    }
}
