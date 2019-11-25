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
import com.beidousat.karaoke.ui.adapter.AdtPartMore;
import com.beidousat.karaoke.ui.dialog.MyPopWindow;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.WidgetKeyboard;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSingerPager;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.Areas;
import com.bestarmedia.libcommon.model.v4.Part;
import com.bestarmedia.libcommon.model.v4.Start;
import com.bestarmedia.libcommon.model.v4.StartSimplePage;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libskin.SkinManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/16 16:43.
 */
public class FmSinger extends BaseFragment implements KeyboardListener, OnPageScrollListener, WidgetPage.OnPageChangedListener {

    private View mRootView;

    private WidgetSingerPager mSingerPager;
    private WidgetTopTabs mWidgetTopTabs;
    private WidgetKeyboard mWidgetKeyboard;
    private WidgetPage mWidgetPage;
    private int mSex_Band = 0;
    private String mSearchKeyword;
    private Map<String, String> mRequestParam;
    private int mWordCount = -1;
    private int mArea;
    private List<Part> partsShow;
    private List<Part> partsHide;
    private MyPopWindow myPopWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fm_singer, null);
            mSingerPager = mRootView.findViewById(R.id.singerPager);
            mWidgetTopTabs = mRootView.findViewById(R.id.topTab);
            mWidgetKeyboard = mRootView.findViewById(R.id.keyboard);
            mWidgetTopTabs.setLeftTabClickListener(leftOnTabClickListener);
            mWidgetTopTabs.setRightTabClickListener(rightOnTabClickListener);
            mWidgetKeyboard.setInputTextChangedListener(this);
            mWidgetPage = mRootView.findViewById(R.id.w_page);
            mWidgetPage.setOnPageChangedListener(this);
            mSingerPager.setOnPagerScrollListener(this);
            init();
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        SkinManager.getInstance().register(mRootView);
        return mRootView;
    }

    @Override
    public void onDestroyView() {
        SkinManager.getInstance().unregister(mRootView);
        super.onDestroyView();
    }

    private void init() {
        String[] mTabRightArrays = getResources().getStringArray(R.array.page_star_top_tabs_right);
        mWidgetTopTabs.setRightTabs(mTabRightArrays);
        mWidgetTopTabs.setRightTabFocus(-1);
        mWidgetTopTabs.setRightTabShow(true);
        requestArea();
        requestSingers();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onInputTextChanged(String text) {
        mSearchKeyword = text;
        requestSingers();
    }

    private WidgetTopTabs.OnTabClickListener leftOnTabClickListener = this::onLeftTabClick;

    private WidgetTopTabs.OnTabClickListener rightOnTabClickListener = this::onRightTabClick;


    public void onLeftTabClick(int position) {
        if (partsShow != null && partsShow.size() > position) {
            Part part = partsShow.get(position);
            if (part.id == -1) {
                showPopListView();
            } else {
                mArea = partsShow.get(position).id;
                mWidgetTopTabs.setRightTabFocus(-1);
                mSex_Band = 0;
                requestSingers();
            }
        }
    }

    public void onRightTabClick(int position) {
        mSex_Band = position + 1;
        requestSingers();
    }


    private void requestArea() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_PART);
        r.setConvert2Class(Areas.class);
        r.get();
    }

    private void requestSingers() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.V4_API_MUSICIAN);
        r.addParam("current_page", String.valueOf(1));
        r.addParam("per_page", String.valueOf(10));
        if (mSex_Band > 0) {
            r.addParam("sex_band", String.valueOf(mSex_Band));
        }
        if (mArea != 0) {
            r.addParam("part_id", String.valueOf(mArea));
        }
        if (!TextUtils.isEmpty(mSearchKeyword)) {
            r.addParam("search", mSearchKeyword);
        }
        if (mWordCount > 0) {
            r.addParam("word_count", String.valueOf(mWordCount));
        }
        mRequestParam = r.getParams();
        r.setConvert2Class(StartSimplePage.class);
        r.get();
    }


    public void initSingerPager(int totalPage, List<Start> starInfos, Map<String, String> params) {
        Logger.i(getClass().getSimpleName(), "Current total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mSingerPager.initPager(totalPage, starInfos, params);
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
        if (isAdded()) {
            if (RequestMethod.V4.V4_API_MUSICIAN.equalsIgnoreCase(method)) {
                initSingerPager(0, null, mRequestParam);
            }
        }
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.V4_API_MUSICIAN.equalsIgnoreCase(method)) {
                StartSimplePage singers = (StartSimplePage) object;
                if (singers.musician != null && singers.activeWord != null) {
                    if ((TextUtils.isEmpty(mSearchKeyword) && TextUtils.isEmpty(singers.activeWord.search)) || singers.activeWord.search.equals(mSearchKeyword)) {
                        initSingerPager(singers.musician.last_page, singers.musician.data, mRequestParam);
                        mWidgetKeyboard.setWords(singers.activeWord.nextWord);
                        mWidgetKeyboard.setKeyboardKeyEnableText(singers.activeWord.enableLetter);
                    } else {
                        initSingerPager(0, null, mRequestParam);
                    }
                } else {
                    initSingerPager(0, null, mRequestParam);
                }
            } else if (RequestMethod.V4.V4_API_PART.equalsIgnoreCase(method)) {
                Areas areas = (Areas) object;
                if (areas != null && areas.part != null && areas.part.size() > 0) {
                    List<Part> partList = areas.part;
                    partsShow = new ArrayList<>();
                    partsHide = new ArrayList<>();
                    boolean ismore = false;
                    for (Part part : partList) {
                        if (part.isShow == 1) {
                            partsShow.add(part);
                        } else if (part.isShow == 0) {
                            ismore = true;
                            partsHide.add(part);
                        }
                    }
                    if (ismore) {
                        partsShow.add(new Part(-1, "更多", -1, 1));
                    }
                    if (partsShow.size() > 0) {
                        String[] text = new String[partsShow.size()];
                        for (int i = 0; i < text.length; i++) {
                            text[i] = partsShow.get(i).name;
                        }
                        mWidgetTopTabs.setLeftTabs(text);
                        mArea = partsShow.get(0).id;
                        requestSingers();
                        mWidgetTopTabs.setLeftTabs(text);
                    }
                }
            }
        }
    }

    @Override
    public void onWordCountChanged(int count) {
        mWordCount = count;
        requestSingers();
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
//        mSingerPager.runLayoutAnimation();
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
    }

    @Override
    public void onPrePageClick(int before, int current) {
        mSingerPager.setCurrentItem(current);
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mSingerPager.setCurrentItem(current);
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mSingerPager.setCurrentItem(current);
        mWidgetPage.setPrePressed(false);
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
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        AdtPartMore adapter = new AdtPartMore(getContext());
        adapter.setOnItemClickListener((view, object) -> {
            try {
                Part part = (Part) object;
                if (partsShow.get(partsShow.size() - 2).isShow == 0) {
                    Part remove = partsShow.remove(partsShow.size() - 2);
                    partsHide.add(remove);
                }
                partsHide.remove(part);
                partsShow.add(partsShow.size() - 1, part);
                if (partsShow.size() > 0) {
                    if ((partsHide == null || partsHide.isEmpty()) && (partsShow.get(partsShow.size() - 1).id == -1)) {//更多无内容,移除更多
                        partsShow.remove(partsShow.size() - 1);
                    }
                    String[] text = new String[partsShow.size()];
                    for (int i = 0; i < text.length; i++) {
                        text[i] = partsShow.get(i).name;
                    }
                    mWidgetTopTabs.setLeftTabs(text);
                    mWidgetTopTabs.setLeftTabFocus(partsShow.size() - 2);
                }
                mArea = part.id;
                mWidgetTopTabs.setRightTabFocus(-1);
                mSex_Band = 0;
                requestSingers();
                if (myPopWindow != null) {
                    myPopWindow.dissmiss();
                }
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "点击“更多”歌星地区出错了：", e);
            }
        });
        adapter.setData(partsHide);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
