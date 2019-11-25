package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.interf.OnPreviewSongListener;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSongPagerV4;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.model.vod.NewSongInfo;
import com.bestarmedia.libcommon.util.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmNewSong extends BaseFragment implements OnPageScrollListener, OnPreviewSongListener, WidgetPage.OnPageChangedListener {
    private View mRootView;
    private WidgetSongPagerV4 mSongPager;
    private TextView mTvRankingName2, tv_tips;
    private WidgetPage mWidgetPage;
    private Map<String, String> mRequestParam;
    private NewSongInfo newSongInfo;

    public static FmNewSong newInstance(NewSongInfo newSongInfo) {
        FmNewSong fragment = new FmNewSong();
        Bundle args = new Bundle();
        args.putSerializable("newSongInfo", newSongInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newSongInfo = (NewSongInfo) getArguments().getSerializable("newSongInfo");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fm_song_ranking, null);
        mSongPager = mRootView.findViewById(R.id.songPager);
        mTvRankingName2 = mRootView.findViewById(R.id.tv_supporter);
        tv_tips = mRootView.findViewById(R.id.tv_tips);
        tv_tips.setVisibility(View.GONE);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mSongPager.setOnPagerScrollListener(this);
        mSongPager.setOnPreviewSongListener(this);
        if (newSongInfo != null && !TextUtils.isEmpty(newSongInfo.id)) {
            requestNewSongList(newSongInfo.id);
            mTvRankingName2.setText(TextUtils.isEmpty(newSongInfo.name) ? "" : newSongInfo.name);
        }
        return mRootView;
    }


    private void requestNewSongList(String id) {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.VOD_NEW_SONG + "/" + id);
        r.addParam("per_page", String.valueOf(14));
        r.addParam("current_page", String.valueOf(1));
        mRequestParam = r.getParams();
        r.setConvert2Class(SongSimplePage.class);
        r.get();
    }

    public void initSongPager(int totalPage, String id,List<SongSimple> firstPageSong, Map<String, String> params) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(RequestMethod.V4.VOD_NEW_SONG+ "/" + id, totalPage, firstPageSong, params);
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

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        mWidgetPage.setPageCurrent(position);
        mSongPager.runLayoutAnimation(isLeft);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
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
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if ((RequestMethod.V4.VOD_NEW_SONG + "/" + newSongInfo.id).equals(method)) {
                try {
                    if (object instanceof SongSimplePage) {
                        SongSimplePage songs = (SongSimplePage) object;
                        if (songs.song != null) {
                            initSongPager(songs.song.last_page, newSongInfo.id,songs.song.data, mRequestParam);
                        } else {
                            initSongPager(0, "",null, mRequestParam);
                        }
                    } else {
                        initSongPager(0, "",null, mRequestParam);
                    }
                } catch (Exception e) {
                    Logger.e(getClass().getSimpleName(), e.toString());
                }
            }
        }
        super.onSuccess(method, object);
    }

    @Override
    public void onPreviewSong(SongSimple song, int position) {
        new PreviewDialog(getActivity(), song, String.valueOf(position)).show();
    }


}
