package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.interf.OnPreviewSongListener;
import com.beidousat.karaoke.interf.OnSongSelectListener;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.WidgetKeyboard;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSongPagerV4;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.Language;
import com.bestarmedia.libcommon.model.v4.LanguageList;
import com.bestarmedia.libcommon.model.v4.Singer;
import com.bestarmedia.libcommon.model.v4.SingerInfo;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.util.GlideUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/19 14:06.
 */
public class FmSingerDetail extends BaseFragment implements OnPageScrollListener, OnPreviewSongListener, KeyboardListener, OnSongSelectListener, WidgetPage.OnPageChangedListener {
    private View mRootView;
    private WidgetSongPagerV4 mWidgetSongPager;
    private ImageView mIvSinger;
    private WidgetTopTabs mWidgetTopTabs;
    private TextView mTvSingerName;
    private WidgetKeyboard mWidgetKeyboard;
    private WidgetPage mWidgetPage;
    private Start mStarInfo;
    private Map<String, String> mRequestSongParam;
    private String mSearchKeyword;
    private int mWordCount = -1;
    private Language currentLanguage;
    private List<Language> languageListList;

    public static FmSingerDetail newInstance(Start info) {
        FmSingerDetail fragment = new FmSingerDetail();
        Bundle args = new Bundle();
        args.putSerializable("info", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStarInfo = (Start) getArguments().getSerializable("info");
        }
    }


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_singer_detail, null);
        mTvSingerName = mRootView.findViewById(R.id.tv_singer);
        mWidgetKeyboard = mRootView.findViewById(R.id.keyboard);
        mWidgetKeyboard.setInputTextChangedListener(this);
        mWidgetKeyboard.showLackSongButton(true);
        mWidgetSongPager = mRootView.findViewById(R.id.songPager);
        mWidgetTopTabs = mRootView.findViewById(R.id.topTab);
        mWidgetTopTabs.setRightTabShow(false);
        mWidgetTopTabs.setLeftTabClickListener(position -> {
            if (languageListList != null && languageListList.size() > position) {
                currentLanguage = languageListList.get(position);
                requestSingerSong();
            }
        });
        mIvSinger = mRootView.findViewById(R.id.iv_singer);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mWidgetSongPager.setOnPagerScrollListener(this);
        mWidgetSongPager.setOnPreviewSongListener(this);
        mWidgetSongPager.setOnSongSelectListener(this);
        if (mStarInfo != null) {
            requestSingerSong();
            requestLanguage(String.valueOf(mStarInfo.id));
            requestSingerDetail(String.valueOf(mStarInfo.id));
        }
        return mRootView;
    }


    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.V4_API_SONG.equalsIgnoreCase(method)) {
                try {
                    if (object instanceof SongSimplePage) {
                        SongSimplePage songs = (SongSimplePage) object;
                        if (songs.song != null && songs.song.data != null && songs.song.data.size() > 0) {
                            initSingerSongPager(songs.song.last_page, songs.song.data, mRequestSongParam);
                        } else {
                            initSingerSongPager(0, null, mRequestSongParam);
                        }
                    } else {
                        initSingerSongPager(0, null, mRequestSongParam);
                    }
                } catch (Exception e) {
                    Logger.e(getClass().getSimpleName(), e.toString());
                }
            } else if ((RequestMethod.V4.V4_API_MUSICIAN + "/" + mStarInfo.id).equals(method)) {
                if (object instanceof SingerInfo) {
                    SingerInfo singerInfo = (SingerInfo) object;
                    if (singerInfo.musician != null) {
                        Singer singer = singerInfo.musician;
                        setSingerInfo(singer);
                    } else {
                        setSingerInfo(null);
                    }
                }
            } else if (RequestMethod.V4.V4_API_LANGUAGE.equals(method)) {
                if (object instanceof LanguageList) {
                    LanguageList languageList = (LanguageList) object;
                    if (languageList.languages != null && languageList.languages.size() > 0) {
                        languageListList = languageList.languages;
                        String[] text = new String[languageListList.size()];
                        for (int i = 0; i < text.length; i++) {
                            text[i] = languageListList.get(i).languageName;
                        }
                        mWidgetTopTabs.setLeftTabs(text);
                        currentLanguage = languageListList.get(0);
                    }
                }
            }
        }
        super.onSuccess(method, object);
    }


    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
        if ((RequestMethod.V4.V4_API_MUSICIAN + "/" + mStarInfo.id).equals(method)) {
            setSingerInfo(null);
        }
    }

    @Override
    public void onError(String method, String error) {
        super.onError(method, error);
        if ((RequestMethod.V4.V4_API_MUSICIAN + "/" + mStarInfo.id).equals(method)) {
            setSingerInfo(null);
        }
    }

    private void setSingerInfo(Singer singer) {
        if (singer == null) {
            mIvSinger.setImageResource(R.drawable.star_default);
        } else {
            mTvSingerName.setText(singer.musicianName);
            GlideUtils.LoadCornersImage(this, !TextUtils.isEmpty(singer.imgFileUrl) ? singer.imgFileUrl : singer.imgFilePath, R.drawable.star_default
                    , R.drawable.star_default, 5, false, mIvSinger);
        }
    }

    public void initSingerSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mWidgetSongPager.initPager(totalPage, firstPageSong, params);
    }


    private void requestSingerSong() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_SONG);
        r.addParam("per_page", String.valueOf(8));
        r.addParam("current_page", String.valueOf(1));
        r.addParam("musician_id", String.valueOf(mStarInfo.id));
        if (currentLanguage != null) {
            r.addParam("language_id", currentLanguage.id + "");
        }
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


    @Override
    public void onPreviewSong(SongSimple song, int ps) {
        new PreviewDialog(getActivity(), song, String.valueOf(ps)).show();
    }

    @Override
    public void onPrePageClick(int before, int current) {
        mWidgetSongPager.setCurrentItem(current);
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mWidgetSongPager.setCurrentItem(current);
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mWidgetSongPager.setCurrentItem(current);
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
        mWidgetSongPager.runLayoutAnimation(isLeft);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onWordCountChanged(int count) {
        mWordCount = count;
        requestSingerSong();
    }

    @Override
    public void onInputTextChanged(String text) {
        mSearchKeyword = text;
        requestSingerSong();
    }

    @Override
    public void onSongSelectListener(SongSimple song) {
        mSearchKeyword = "";
        mWidgetKeyboard.setCleanText(true);
    }


    private void requestSingerDetail(String id) {
        HttpRequestV4 request = initRequestV4(RequestMethod.V4.V4_API_MUSICIAN + "/" + id);
        request.setConvert2Class(SingerInfo.class);
        request.get();
    }

    private void requestLanguage(String singerID) {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_LANGUAGE);
        if (!TextUtils.isEmpty(singerID)) {
            r.addParam("musician_id", singerID);
        }
        r.setConvert2Class(LanguageList.class);
        r.get();
    }

}
