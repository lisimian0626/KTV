package com.beidousat.karaoke.ui.fragment.topic;

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
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.helper.StatisticsHelper;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.model.v4.TopicDetailV4;
import com.bestarmedia.libcommon.model.vod.Topic;
import com.bestarmedia.libcommon.model.vod.TopicRequestBody;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmMoodDetail extends BaseFragment implements OnPageScrollListener, OnPreviewSongListener, WidgetPage.OnPageChangedListener, OnSongSelectListener, WidgetTopTabs.OnTabClickListener {

    private Topic mTopic;
    private View mRootView;
    private WidgetSongPagerV4 mSongPager;
    private TextView mTvTopicName, mTvSupporter;
    private WidgetPage mWidgetPage;
    private Map<String, String> mRequestParam;
    private WidgetTopTabs widgetTopTabs;
    private List<String> stringList = new ArrayList<>();

    public static FmMoodDetail newInstance(Topic topic) {
        FmMoodDetail fragment = new FmMoodDetail();
        Bundle args = new Bundle();
        args.putSerializable("topic", topic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTopic = (Topic) getArguments().get("topic");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fm_mood_detail, null);
        mSongPager = mRootView.findViewById(R.id.songPager);
        mTvTopicName = mRootView.findViewById(R.id.tv_ranking_name);
        mTvSupporter = mRootView.findViewById(R.id.tv_supporter);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        widgetTopTabs = mRootView.findViewById(R.id.topTab);
        widgetTopTabs.setLeftTabShow(false);
        widgetTopTabs.setRightTabClickListener(this);
        mWidgetPage.setOnPageChangedListener(this);
        mSongPager.setOnPagerScrollListener(this);
        mSongPager.setOnPreviewSongListener(this);
        mSongPager.setOnSongSelectListener(this);

        if (!TextUtils.isEmpty(mTopic.id)) {
            requestTopicsDetail();
            recordTopicClick();
        }
        if (mTopic != null) {
            mTvTopicName.setText(mTopic.name == null ? "" : mTopic.name);
            mTvSupporter.setText(!TextUtils.isEmpty(mTopic.name) ? mTopic.name : getText(R.string.company_name));

            if (mTopic.isWordsSort == 1) {
                stringList.add(getString(R.string.songlist_words));
            }
            if (mTopic.isSpellSort == 1) {
                stringList.add(getString(R.string.songlist_spell));
            }
            if (stringList.size() > 0) {
                stringList.add(0, getString(R.string.songlist_default));
                widgetTopTabs.setRightTabs(stringList);
                widgetTopTabs.setRightTabFocus(0);
            }
            if (!TextUtils.isEmpty(mTopic.id)) {
                requestTopicsSong(0);
                requestTopicsDetail();
                recordTopicClick();
            }
        }
        return mRootView;
    }

    private void recordTopicClick() {
        TopicRequestBody body = new TopicRequestBody(mTopic.id, mTvSupporter.getText().toString());
        StatisticsHelper.getInstance(getContext().getApplicationContext()).recordTopicClick(body);
    }

    private void requestTopicsDetail() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.SONG_LIST + "/" + mTopic.id);
        r.setConvert2Class(TopicDetailV4.class);
        r.get();
    }

    private void requestTopicsSong(int sortType) {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.SONG_LIST_SONG);
        r.addParam("menu_id", mTopic.id);
        r.addParam("per_page", String.valueOf(14));
        r.addParam("current_page", String.valueOf(1));
        r.addParam("sort_type", String.valueOf(sortType));
        r.setConvert2Class(SongSimplePage.class);
        mRequestParam = r.getParams();
        r.get();
    }

    public void initSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(RequestMethod.V4.SONG_LIST_SONG, totalPage, firstPageSong, params);
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
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.SONG_LIST_SONG.equalsIgnoreCase(method)) {
                if (object instanceof SongSimplePage) {
                    SongSimplePage songSimplePage = (SongSimplePage) object;
                    if (songSimplePage.song != null && songSimplePage.song.data != null) {
                        initSongPager(songSimplePage.song.last_page, songSimplePage.song.data, mRequestParam);
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
    public void onTabClick(int position) {
        if (stringList != null && stringList.size() > 0) {
            if (getText(R.string.songlist_default).equals(stringList.get(position))) {
                requestTopicsSong(0);
            } else if (getText(R.string.songlist_words).equals(stringList.get(position))) {
                requestTopicsSong(1);
            } else if (getText(R.string.songlist_spell).equals(stringList.get(position))) {
                requestTopicsSong(2);
            }
        }
    }
}
