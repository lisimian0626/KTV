package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.interf.OnPreviewSongListener;
import com.beidousat.karaoke.interf.OnSongSelectListener;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.WidgetKeyboard;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSingerPager;
import com.beidousat.karaoke.widget.viewpager.WidgetSongPagerV4;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.model.v4.StartSimplePage;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libskin.SkinManager;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/10/9 11:19.
 */
public class FmSearch extends BaseFragment implements KeyboardListener, OnPageScrollListener, OnPreviewSongListener, OnSongSelectListener, WidgetPage.OnPageChangedListener {

    private int mTabPs = 0;
    private String mSearchKeyword;
    private View mRootView;

    private WidgetKeyboard mWidgetKeyboard;

    private WidgetSongPagerV4 mWidgetSongPager;
    private WidgetSingerPager mWidgetSingerPager;
    private WidgetPage mWidgetPage;

    private Map<String, String> mRequestSongParam;
    private Map<String, String> mRequestSingerParam;

    private int mWordCount = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fm_search, null);
        mWidgetSongPager = mRootView.findViewById(R.id.songPager);
        mWidgetSongPager.setOnPreviewSongListener(this);
        mWidgetSongPager.setOnSongSelectListener(this);

        mWidgetSingerPager = mRootView.findViewById(R.id.singerPager);
        WidgetTopTabs mWidgetTopTabs = mRootView.findViewById(R.id.topTab);
        mWidgetTopTabs.setRightTabShow(false);

        mWidgetKeyboard = mRootView.findViewById(R.id.keyboard);
        mWidgetKeyboard.setInputTextChangedListener(this);

        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);

        mWidgetTopTabs.setLeftTabClickListener(leftOnTabClickListener);
        mWidgetSongPager.setOnPagerScrollListener(this);
        mWidgetSingerPager.setOnPagerScrollListener(this);
        mWidgetTopTabs.setLeftTabs(R.array.search_types_movie_pwd);

        SkinManager.getInstance().register(mRootView);

        requestSongs();

        return mRootView;
    }


    @Override
    public void onDestroyView() {
        SkinManager.getInstance().unregister(mRootView);
        super.onDestroyView();
    }

    private void requestSongs() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_SONG);
        r.addParam("per_page", String.valueOf(8));
        r.addParam("current_page", String.valueOf(1));
        if (!TextUtils.isEmpty(mSearchKeyword)) {
            r.addParam("search", mSearchKeyword);
        }
        if (mWordCount > 0) {
            r.addParam("word_count", String.valueOf(mWordCount));
        }
        mRequestSongParam = r.getParams();
        r.setConvert2Class(SongSimplePage.class);
        r.get();
    }

    private void requestSingers() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_MUSICIAN);
        r.addParam("current_page", String.valueOf(1));
        r.addParam("per_page", String.valueOf(10));
        if (!TextUtils.isEmpty(mSearchKeyword)) {
            r.addParam("search", mSearchKeyword);
        }
        if (mWordCount > 0) {
            r.addParam("word_count", String.valueOf(mWordCount));
        }
        mRequestSingerParam = r.getParams();
        r.setConvert2Class(StartSimplePage.class);
        r.get();
    }

    public void initSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        Logger.i(getClass().getSimpleName(), "Current total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mWidgetSongPager.initPager(totalPage, firstPageSong, params);
    }

    public void initSingerPager(int totalPage, List<Start> starInfos, Map<String, String> params) {
        Logger.i(getClass().getSimpleName(), "Current total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mWidgetSingerPager.initPager(totalPage, starInfos, params);
    }

    @Override
    public void onPrePageClick(int before, int current) {
        if (mTabPs == 0) {
            mWidgetSongPager.setCurrentItem(current);
        } else if (mTabPs == 1) {
            mWidgetSingerPager.setCurrentItem(current);
        }
    }

    @Override
    public void onNextPageClick(int before, int current) {
        if (mTabPs == 0) {
            mWidgetSongPager.setCurrentItem(current);
        } else if (mTabPs == 1) {
            mWidgetSingerPager.setCurrentItem(current);
        }
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        if (mTabPs == 0) {
            mWidgetSongPager.setCurrentItem(current);
        } else if (mTabPs == 1) {
            mWidgetSingerPager.setCurrentItem(current);
        }
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        if (mTabPs == 0) {
            mWidgetPage.setPageCurrent(position);
            mWidgetSongPager.runLayoutAnimation(isLeft);
        } else if (mTabPs == 1) {
            mWidgetPage.setPageCurrent(position);
        } else if (mTabPs == 2) {
            mWidgetPage.setPageCurrent(position);
        }
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

    private WidgetTopTabs.OnTabClickListener leftOnTabClickListener = this::onLeftTabClick;

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.V4_API_SONG.equalsIgnoreCase(method)) {
                try {
                    if (object instanceof SongSimplePage) {
                        SongSimplePage songs = (SongSimplePage) object;
                        if (songs.song != null && songs.activeWord != null) {
                            if ((TextUtils.isEmpty(mSearchKeyword) && TextUtils.isEmpty(songs.activeWord.search)) || songs.activeWord.search.equals(mSearchKeyword)) {
//                                int totalPage = (songs.song.total % songs.song.per_page == 0) ? (songs.song.total / songs.song.per_page) : ((songs.song.total / songs.song.per_page) + 1);
                                initSongPager(songs.song.last_page, songs.song.data, mRequestSongParam);
                                mWidgetKeyboard.setWords(songs.activeWord.nextWord);
                                mWidgetKeyboard.setKeyboardKeyEnableText(songs.activeWord.enableLetter);
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.e(getClass().getSimpleName(), e.toString());
                }
            } else if (RequestMethod.V4.V4_API_MUSICIAN.equalsIgnoreCase(method)) {
                StartSimplePage singers = (StartSimplePage) object;
                if (singers != null && singers.musician != null && singers.musician.data != null) {
                    initSingerPager(singers.musician.last_page, singers.musician.data, mRequestSingerParam);
                    mWidgetKeyboard.setWords(singers.activeWord.nextWord);
                    mWidgetKeyboard.setKeyboardKeyEnableText(singers.activeWord.enableLetter);

                }
            }
        }
        super.onSuccess(method, object);
    }

    public void onLeftTabClick(int position) {
        mTabPs = position;
        switch (mTabPs) {
            case 0:
                mWidgetKeyboard.setCleanText(false);
                mWidgetKeyboard.setText("");
                mWidgetKeyboard.setHintText(getString(R.string.search_song));
                mWidgetKeyboard.showLackSongButton(true);
                mWidgetSongPager.setVisibility(View.VISIBLE);
                mWidgetSingerPager.setVisibility(View.GONE);
                requestSongs();
                break;
            case 1:
                mWidgetKeyboard.setCleanText(false);
                mWidgetKeyboard.setText("");
                mWidgetKeyboard.setHintText(getString(R.string.search_singer));
                mWidgetKeyboard.showLackSongButton(false);
                mWidgetSongPager.setVisibility(View.GONE);
                mWidgetSingerPager.setVisibility(View.VISIBLE);
                requestSingers();
                break;
        }
    }

    @Override
    public void onInputTextChanged(String text) {
        mSearchKeyword = text;
        switch (mTabPs) {
            case 0:
                requestSongs();
                break;
            case 1:
                requestSingers();
                break;
            default:
                break;
        }
    }

    @Override
    public void onWordCountChanged(int count) {
        mWordCount = count;
        switch (mTabPs) {
            case 0:
                requestSongs();
                break;
            case 1:
                requestSingers();
                break;

            default:
                break;
        }
    }


    @Override
    public void onPreviewSong(SongSimple song, int ps) {
        new PreviewDialog(getContext(), song, String.valueOf(ps)).show();
    }

    @Override
    public void onSongSelectListener(SongSimple song) {
        mSearchKeyword = "";
        mWidgetKeyboard.setCleanText(true);
    }
}
