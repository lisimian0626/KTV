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
import com.beidousat.karaoke.interf.OnSongSelectListener;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSongPagerV4;
import com.bestarmedia.libcommon.helper.StatisticsHelper;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.ad.RecommendInfo;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.vod.TopicRequestBody;
import com.bestarmedia.libcommon.model.vod.TopicsDetail;
import com.bestarmedia.libcommon.util.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmTopicDetail extends BaseFragment implements OnPageScrollListener, OnPreviewSongListener, WidgetPage.OnPageChangedListener, OnSongSelectListener {

    private RecommendInfo mRecommendInfo;
    private View mRootView;
    private WidgetSongPagerV4 mSongPager;
    private TextView mTvSupporter;
    private WidgetPage mWidgetPage;
    private Map<String, String> mRequestParam;

    public static FmTopicDetail newInstance(RecommendInfo recommendInfo) {
        FmTopicDetail fragment = new FmTopicDetail();
        Bundle args = new Bundle();
        args.putSerializable("recommendInfo", recommendInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecommendInfo = (RecommendInfo) getArguments().get("recommendInfo");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fm_song_ranking, null);
        mSongPager = mRootView.findViewById(R.id.songPager);
        mTvSupporter = mRootView.findViewById(R.id.tv_supporter);

        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);

        mTvSupporter = mRootView.findViewById(R.id.tv_supporter);

        mSongPager.setOnPagerScrollListener(this);
        mSongPager.setOnPreviewSongListener(this);
        mSongPager.setOnSongSelectListener(this);

        if (mRecommendInfo != null) {
            mTvSupporter.setText(mRecommendInfo.getName() == null ? "" : mRecommendInfo.getName());
            mTvSupporter.setText(!TextUtils.isEmpty(mRecommendInfo.getBrandName()) ? mRecommendInfo.getBrandName() : getText(R.string.company_name));
            if (!TextUtils.isEmpty(mRecommendInfo.getAdContent()))
                if (!TextUtils.isEmpty(mRecommendInfo.getId())) {
                    requestBannerDetail();
                    recordTopicClick();
                }
        }
        return mRootView;
    }

    private void recordTopicClick() {
        TopicRequestBody body = new TopicRequestBody(mRecommendInfo.getId(), mTvSupporter.getText().toString());
        StatisticsHelper.getInstance(getContext()).recordTopicClick(body);
    }

    private void requestBannerDetail() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.SONG_LIST_SONG);
        r.addParam("menu_id", mRecommendInfo.getId());
        r.addParam("per_page", String.valueOf(8));
        r.addParam("current_page", String.valueOf(1));
        r.setConvert2Class(TopicsDetail.class);
        mRequestParam = r.getParams();
        r.get();
    }


    public void initSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(RequestMethod.V4.SONG_LIST_SONG, totalPage, firstPageSong, params);
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
            if (RequestMethod.V4.SONG_LIST_SONG.equalsIgnoreCase(method)) {
                if (object instanceof TopicsDetail) {
                    TopicsDetail detail = (TopicsDetail) object;
                    if (detail.song != null && detail.song.data != null) {
                        initSongPager(detail.song.last_page, detail.song.data, mRequestParam);
                    }
                    if (detail.topic != null && detail.song != null && detail.song.data != null) {
                        mTvSupporter.setText(!TextUtils.isEmpty(detail.topic.name) ? detail.topic.name : getText(R.string.company_name));
                    }
                }
            }
        }
        super.onSuccess(method, object);
    }

    @Override
    public void onPreviewSong(SongSimple song, int position) {
        new PreviewDialog(getActivity(), song, String.valueOf(position)).show();
    }

    @Override
    public void onSongSelectListener(SongSimple song) {
        Logger.d(getClass().getSimpleName(), "从小编推荐中点歌：" + song.id);
        recordTopicClick();
    }
}
