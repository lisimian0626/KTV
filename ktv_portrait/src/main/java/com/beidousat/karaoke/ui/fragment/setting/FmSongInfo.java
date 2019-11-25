package com.beidousat.karaoke.ui.fragment.setting;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.adapter.AdtSongInfo;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetUpdateLogPager;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.SongInfo;
import com.bestarmedia.libcommon.model.vod.SongUpdateLog;
import com.bestarmedia.libwidget.recycler.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by J Wong on 2016/8/24.
 */
public class FmSongInfo extends BaseFragment implements OnPageScrollListener, WidgetPage.OnPageChangedListener {

    private View mRootView;
    private WidgetUpdateLogPager mPager;
    private WidgetPage mWidgetPage;
    private RecyclerView lv_songInfo;
    private AdtSongInfo adtSongInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_song_info, null);
        mRootView.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusUtil.postSticky(EventBusId.Id.BACK_FRAGMENT, "");
            }
        });
        lv_songInfo = mRootView.findViewById(R.id.lv_songinfo);
        HorizontalDividerItemDecoration horDivider = new HorizontalDividerItemDecoration.Builder(getContext())
                .color(Color.TRANSPARENT).size(1).build();

        lv_songInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        lv_songInfo.addItemDecoration(horDivider);
        adtSongInfo = new AdtSongInfo(getActivity());
        lv_songInfo.setAdapter(adtSongInfo);

        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mPager = mRootView.findViewById(R.id.pager);
        mPager.setOnPagerScrollListener(this);

        requestUpdateLog();

        requestSongInfo();

        return mRootView;
    }

    private int initPager(List<SongUpdateLog.SongLog> logs) {
        int mTotalPage = mPager.initPager(logs);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(mTotalPage);
        return mTotalPage;
    }

    @Override
    public void onPrePageClick(int before, int current) {
        mPager.setCurrentItem(current);
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mPager.setCurrentItem(current);
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mPager.setCurrentItem(current);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollLeft() {
        mWidgetPage.setPrePressed(true);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPageScrollRight() {
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(true);
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }


    private void requestUpdateLog() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.SONG_UPDATE_LOG);
        r.addParam("weeks", String.valueOf(8));
        r.setConvert2Class(SongUpdateLog.class);
        r.get();
    }

    private void requestSongInfo() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.VOD_SONG_STATISTICS);
        r.setConvert2Class(SongInfo.class);
        r.get();
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.VOD_SONG_STATISTICS.equals(method)) {
                SongInfo songInfo = (SongInfo) object;
                if (songInfo != null && songInfo.statistic != null && songInfo.statistic.size() > 0) {
                    adtSongInfo.setData(songInfo.statistic);
                    adtSongInfo.notifyDataSetChanged();
                }
            } else if (RequestMethod.V4.SONG_UPDATE_LOG.equals(method)) {
                SongUpdateLog songUpdateLog;
                if (object != null && object instanceof SongUpdateLog && (songUpdateLog = (SongUpdateLog) object) != null
                        && songUpdateLog.log != null && songUpdateLog.log.size() > 0) {
                    initPager(songUpdateLog.log);
                }
            }
        }
        super.onSuccess(method, object);
    }
}
