package com.beidousat.karaoke.ui.fragment.play;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetLivePager;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.Live;
import com.bestarmedia.libcommon.model.vod.Lives;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/10/9 11:19.
 */
public class FmLives extends BaseFragment implements OnPageScrollListener, WidgetPage.OnPageChangedListener {

    private View mRootView;
    private WidgetLivePager mSongPager;
    private WidgetTopTabs mWidgetTopTabs;
    private WidgetPage mWidgetPage;

    private int mLiveStatus = 3;
    private Map<String, String> mRequestParam;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_lives, null);
        mSongPager = mRootView.findViewById(R.id.livePager);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mWidgetTopTabs = mRootView.findViewById(R.id.topTab);
        mWidgetTopTabs.setLeftTabs(R.array.lives_tabs);
        mWidgetTopTabs.setLeftTabClickListener(leftOnTabClickListener);
        mWidgetTopTabs.setRightTabShow(false);
        mSongPager.setOnPagerScrollListener(this);
        init();
        return mRootView;
    }

    private WidgetTopTabs.OnTabClickListener leftOnTabClickListener = this::onLeftTabClick;

    private void init() {
        requestLives();
    }


    private void requestLives() {
        HttpRequestV4 requestV4 = initRequestV4(RequestMethod.NODE_LIVE);
        requestV4.addParam("per_page", String.valueOf(8));
        requestV4.addParam("current_page", String.valueOf(1));
        requestV4.addParam("live_status", String.valueOf(mLiveStatus));
        mRequestParam = requestV4.getParams();
        requestV4.setConvert2Class(Lives.class);
        requestV4.get();
    }


    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
    }

    @Override
    public void onStart(String method) {
        super.onStart(method);
    }

    public void initLivePager(int totalPage, List<Live> lives, Map<String, String> params) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(totalPage, lives, params);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.NODE_LIVE.equalsIgnoreCase(method)) {
                Lives lives = (Lives) object;
                if (lives != null && lives.live != null && lives.live.data != null) {
                    initLivePager(lives.live.last_page, lives.live.data, mRequestParam);
                }
            }
        }
        super.onSuccess(method, object);
    }

    public void onLeftTabClick(int position) {
        switch (position) {
            case 0:
                mLiveStatus = 3;
                requestLives();
                break;
            case 1:
                mLiveStatus = 1;
                requestLives();
                break;
            case 2:
                mLiveStatus = 2;
                requestLives();
                break;
        }
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
        mSongPager.runLayoutAnimation(isLeft);
    }

    @Override
    public void onPageScrollRight() {
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(true);
    }

    @Override
    public void onPageScrollLeft() {
        mWidgetPage.setPrePressed(true);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPrePageClick(int before, int current) {
        mSongPager.setCurrentItem(current);
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mSongPager.setCurrentItem(current);
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mSongPager.setCurrentItem(current);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }
}
