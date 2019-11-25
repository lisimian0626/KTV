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
import com.beidousat.karaoke.ui.adapter.AdtSongTypeMore;
import com.beidousat.karaoke.ui.dialog.MyPopWindow;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongCate;
import com.bestarmedia.libcommon.model.v4.SongCategories;
import com.bestarmedia.libcommon.model.v4.SongSimplePage;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmSongCategory extends FmSongBase {

    private Map<String, String> mRequestParam;
    private SongTypeEnum songTypeEnum;
    private SongCate songCate;
    private int mWordCount;
    private List<SongCate> songShowCate;
    private List<SongCate> songHideCate;
    private MyPopWindow myPopWindow;

    public enum SongTypeEnum {
        NORMAL(1, "通俗"),
        DRAMA(2, "戏曲"),
        FOLK(3, "民歌"),
        RED_SONG(4, "红歌"),
        CHILDREN(5, "儿歌"),
        CLASSIC(6, "古典"),
        DISCO(7, "的士高"),
        DAMCE(8, "舞曲"),
        MUSIC(9, "纯音乐"),
        OTHER(10, "其他");

        public int id;
        public String name;

        SongTypeEnum(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public static FmSongCategory newInstance(SongTypeEnum songTypeEnum) {
        FmSongCategory fragment = new FmSongCategory();
        Bundle args = new Bundle();
        args.putSerializable("song_type", songTypeEnum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songTypeEnum = (SongTypeEnum) getArguments().getSerializable("song_type");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        init();
        return mRootView;
    }

    @Override
    public void onLeftTabClick(int position) {
        if (songShowCate != null && songShowCate.size() > position) {
            SongCate cate = songShowCate.get(position);
            if (cate.id == -1) {
                showPopListView();
            } else {
                songCate = cate;
                requestSongs(songCate);
            }
        }
        super.onLeftTabClick(position);
    }


    private void init() {
        requestCategory(songTypeEnum.id);
    }


    private void requestCategory(int songTypeId) {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_SONG_SONG_TYPE + songTypeId);
        r.setConvert2Class(SongCategories.class);
        r.get();
    }

    private void requestSongs(SongCate songCate) {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_SONG);
        r.addParam("per_page", String.valueOf(8));
        r.addParam("current_page", String.valueOf(1));
        if (songCate != null) {
            r.addParam("song_type_id", String.valueOf(songCate.id));
        }
        if (songCate != null) {
            r.addParam("song_type_parent_id", String.valueOf(songCate.parentId));
        }
        if (!TextUtils.isEmpty(mSearchKeyword)) {
            r.addParam("search", mSearchKeyword);
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
        Logger.d(getClass().getSimpleName(), "onInputTextChanged >>>>>>" + text);
        mSearchKeyword = text;
        requestSongs(songCate);
        super.onInputTextChanged(text);
    }

    @Override
    public void onWordCountChanged(int count) {
        mWordCount = count;
        requestSongs(songCate);
        super.onWordCountChanged(count);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.V4_API_SONG.equalsIgnoreCase(method)) {
                try {
                    if (object instanceof SongSimplePage) {
                        SongSimplePage songs = (SongSimplePage) object;
                        if (songs.song != null && songs.song.data != null && songs.song.data.size() > 0) {
                            initSongPager(songs.song.last_page, songs.song.data, mRequestParam);
                        } else {
                            initSongPager(0, null, mRequestParam);
                        }
                    } else {
                        initSongPager(0, null, mRequestParam);
                    }
                } catch (Exception e) {
                    Logger.e(getClass().getSimpleName(), e.toString());
                }
            } else if ((RequestMethod.V4.V4_API_SONG_SONG_TYPE + songTypeEnum.id).equalsIgnoreCase(method)) {
                if (object instanceof SongCategories) {
                    SongCategories songCategories = (SongCategories) object;
                    if (songCategories.song_type != null && songCategories.song_type.size() > 0) {
                        List<SongCate> songCateList = songCategories.song_type;
                        songShowCate = new ArrayList<>();
                        songHideCate = new ArrayList<>();
                        boolean ismore = false;
                        for (SongCate songCate : songCateList) {
                            if (songCate.isShow == 1) {
                                songShowCate.add(songCate);
                            } else if (songCate.isShow == 0) {
                                ismore = true;
                                songHideCate.add(songCate);
                            }
                        }
                        if (ismore) {
                            songShowCate.add(new SongCate(-1, 0, "更多", -1, 1));
                        }
                        if (songShowCate.size() > 0) {
                            String[] text = new String[songShowCate.size()];
                            for (int i = 0; i < text.length; i++) {
                                text[i] = songShowCate.get(i).name;
                            }
                            mWidgetTopTabs.setLeftTabs(text);
                            songCate = songCateList.get(0);
                            requestSongs(songCate);
                            mWidgetTopTabs.setLeftTabs(text);
                        }
                    }
                }
            }
        }
        super.onSuccess(method, object);
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
        AdtSongTypeMore adapter = new AdtSongTypeMore(getContext());
        adapter.setOnItemClickListener((view, object) -> {
            try {
                SongCate cate = (SongCate) object;
                if (songShowCate.get(songShowCate.size() - 2).isShow == 0) {
                    SongCate remove = songShowCate.remove(songShowCate.size() - 2);
                    songHideCate.add(remove);
                }
                songHideCate.remove(cate);
                songShowCate.add(songShowCate.size() - 1, cate);
                if (songShowCate.size() > 0) {
                    if ((songHideCate == null || songHideCate.isEmpty())
                            && (songShowCate.get(songShowCate.size() - 1).id == -1)) {//更多无内容,移除更多
                        songShowCate.remove(songShowCate.size() - 1);
                    }
                    String[] text = new String[songShowCate.size()];
                    for (int i = 0; i < text.length; i++) {
                        text[i] = songShowCate.get(i).name;
                    }
                    mWidgetTopTabs.setLeftTabs(text);
                    mWidgetTopTabs.setLeftTabFocus(songShowCate.size() - 2);
                }
                songCate = cate;
                requestSongs(songCate);
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "点击“更多”歌曲分类出错了：", e);
            }
        });
        adapter.setData(songHideCate);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
