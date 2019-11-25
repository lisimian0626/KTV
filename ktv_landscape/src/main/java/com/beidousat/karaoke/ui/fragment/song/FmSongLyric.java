package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.adapter.AdtSpinner;
import com.beidousat.karaoke.ui.dialog.MyPopWindow;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.widget.WidgetKeyboard;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSongLyricPager;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.Language;
import com.bestarmedia.libcommon.model.v4.LanguageList;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libskin.SkinManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmSongLyric extends FmSongBase implements OnPageScrollListener, KeyboardListener, WidgetPage.OnPageChangedListener {

    private View mRootView;
    private WidgetSongLyricPager mSongPager;
    private WidgetKeyboard mWidgetKeyboard;
    private WidgetTopTabs mWidgetTopTabs;
    private WidgetPage mWidgetPage;
    private String mSearchKeyword;
    private int mSortType = 0;
    private Map<String, String> mRequestParam;
    private int mWordCount = -1;
    private Language curLanguage;
    private List<Language> languageListShow;
    private List<Language> languageListHide;
    private MyPopWindow myPopWindow;

    public static FmSongLyric newInstance() {
        FmSongLyric fragment = new FmSongLyric();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fm_song_lyric, null);
        mSongPager = mRootView.findViewById(R.id.songPager);
        mWidgetTopTabs = mRootView.findViewById(R.id.topTab);
        mWidgetKeyboard = mRootView.findViewById(R.id.keyboard);
        mWidgetPage = mRootView.findViewById(R.id.w_page);
        mWidgetPage.setOnPageChangedListener(this);
        mWidgetTopTabs.setLeftTabClickListener(leftOnTabClickListener);
        mWidgetKeyboard.setInputTextChangedListener(this);
        mWidgetTopTabs.setRightTabShow(false);
        mWidgetTopTabs.setRightTabFocus(0);
        mWidgetTopTabs.setRightTabClickListener(rightOnTabClickListener);
        mSongPager.setOnPagerScrollListener(this);
        SkinManager.getInstance().register(mRootView);
        requestSongs();
        requestLanguage();
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        SkinManager.getInstance().unregister(mRootView);
        super.onDestroyView();
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

    private WidgetTopTabs.OnTabClickListener leftOnTabClickListener = this::onLeftTabClick;

    private WidgetTopTabs.OnTabClickListener rightOnTabClickListener = this::onRightTabClick;

    public void onLeftTabClick(int position) {
        Language language = languageListShow.get(position);
        if (language != null && language.id != -1) {
            curLanguage = language;
        }
        if (language != null && language.id != -1) {
            requestSongs();
        } else {
            showPopListView();
        }
    }


    private void requestLanguage() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_LANGUAGE);
        r.setConvert2Class(LanguageList.class);
        r.get();
    }

    private void requestSongs() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_SONG);
        r.addParam("per_page", String.valueOf(8));
        r.addParam("current_page", String.valueOf(1));
        r.addParam("sort_type", String.valueOf(mSortType));
        if (curLanguage != null && curLanguage.id > 0) {
            r.addParam("language_id", curLanguage.id + "");
        }
        if (!TextUtils.isEmpty(mSearchKeyword)) {
            r.addParam("lyric", mSearchKeyword);
        }
        if (mWordCount > 0) {
            r.addParam("word_count", String.valueOf(mWordCount));
        }

        mRequestParam = r.getParams();
        r.setConvert2Class(SongSimplePage.class);
        r.get();
    }


    @Override
    public void onInputTextChanged(String text) {
        mSearchKeyword = text;
        requestSongs();
    }

    @Override
    public void onWordCountChanged(int count) {
        mWordCount = count;
        requestSongs();
    }

    public void initSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSongPager.initPager(totalPage, firstPageSong, params);
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
        if (isAdded()) {
            if (RequestMethod.V4.V4_API_SONG.equalsIgnoreCase(method)) {
                initSongPager(0, null, mRequestParam);
            }
        }
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.V4_API_SONG.equalsIgnoreCase(method)) {
                try {
                    if (object instanceof SongSimplePage) {
                        SongSimplePage songs = (SongSimplePage) object;
                        if (songs.song != null && songs.song.data != null) {
                            initSongPager(songs.song.last_page, songs.song.data, mRequestParam);
                            mWidgetKeyboard.setWords(songs.activeWord.nextWord);
                            mWidgetKeyboard.setKeyboardKeyEnableText(songs.activeWord.enableLetter);
                        }
                    }
                } catch (Exception e) {
                    Logger.e(getClass().getSimpleName(), e.toString());
                }
            } else if (RequestMethod.V4.V4_API_LANGUAGE.equalsIgnoreCase(method)) {
                if (object instanceof LanguageList) {
                    LanguageList languageList = (LanguageList) object;
                    if (languageList.languages != null && languageList.languages.size() > 0) {
                        languageListShow = new ArrayList<>();
                        languageListHide = new ArrayList<>();
                        boolean ismore = false;
                        for (Language language : languageList.languages) {
                            if (language.isShow == 1) {
                                languageListShow.add(language);
                            } else if (language.isShow == 0) {
                                ismore = true;
                                languageListHide.add(language);
                            }
                        }
                        if (ismore) {
                            languageListShow.add(new Language(-1, "更多", -1, 1));
                        }
                        if (languageListShow.size() > 0) {
                            String[] text = new String[languageListShow.size()];
                            for (int i = 0; i < text.length; i++) {
                                text[i] = languageListShow.get(i).languageName;
                            }
                            mWidgetTopTabs.setLeftTabs(text);
                            curLanguage = languageListShow.get(0);
                        }
                    }
                }
            }
        }
        super.onSuccess(method, object);
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

    private void showPopListView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.spiner_main, null);
        //处理popWindow 显示内容
        handleListView(contentView);
        //创建并显示popWindow
        int[] location = new int[2];
        View v = mWidgetTopTabs.getWidgetTopLastView() == null ? mWidgetTopTabs : mWidgetTopTabs.getWidgetTopLastView();
        v.getLocationOnScreen(location);
        myPopWindow = new MyPopWindow.PopupWindowBuilder(getContext())
                .setView(contentView)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBgDarkAlpha(1.0f)
                .create()
                .showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] + v.getHeight());
    }

    private void handleListView(View contentView) {
        RecyclerView recyclerView = contentView.findViewById(R.id.spinner_rv);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        AdtSpinner adapter = new AdtSpinner(getContext());
        adapter.setOnItemClickListener((view, object) -> {
            try {
                Language language = (Language) object;
                if (languageListShow != null && languageListShow.get(languageListShow.size() - 2).isShow == 0) {
                    Language remove = languageListShow.remove(languageListShow.size() - 2);
                    languageListHide.add(remove);
                }
                languageListHide.remove(language);
                languageListShow.add(languageListShow.size() - 1, language);
                if (languageListShow.size() > 0) {
                    if ((languageListHide == null || languageListHide.isEmpty())
                            && (languageListShow.get(languageListShow.size() - 1).id == -1)) {//更多无内容,移除更多
                        languageListShow.remove(languageListShow.size() - 1);
                    }
                    String[] text = new String[languageListShow.size()];
                    for (int i = 0; i < text.length; i++) {
                        text[i] = languageListShow.get(i).languageName;
                    }
                    mWidgetTopTabs.setLeftTabs(text);
                    mWidgetTopTabs.setLeftTabFocus(languageListShow.size() - 2);
                }
                curLanguage = language;
                requestSongs();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "点击“更多”语种出错了：", e);
            }
        });
        adapter.setData(languageListHide);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
