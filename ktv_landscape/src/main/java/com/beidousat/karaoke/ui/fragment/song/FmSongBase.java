package com.beidousat.karaoke.ui.fragment.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.interf.KeyboardListener;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.interf.OnPreviewSongListener;
import com.beidousat.karaoke.interf.OnSongSelectListener;
import com.beidousat.karaoke.ui.dialog.PromptDialogBig;
import com.beidousat.karaoke.ui.dialog.vod.PreviewDialog;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.WidgetKeyboard;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.beidousat.karaoke.widget.viewpager.WidgetSongPagerV4;
import com.beidousat.karaoke.widget.viewpager.WidgetTopTabs;
import com.bestarmedia.libcommon.model.v4.SongSimple;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libskin.SkinManager;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/16 16:43.
 */
public class FmSongBase extends BaseFragment implements KeyboardListener, OnPageScrollListener, OnSongSelectListener, OnPreviewSongListener, WidgetPage.OnPageChangedListener {

    View mRootView;

    String mSearchKeyword;

    WidgetSongPagerV4 mSongPager;

    WidgetTopTabs mWidgetTopTabs;

    WidgetKeyboard mWidgetKeyboard;

    WidgetPage mWPager;

    View mEmptyView;


//    @Override
//    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
////        return CubeAnimation.create(CubeAnimation.LEFT, enter, 200);
//        //   return FlipAnimation.create(FlipAnimation.LEFT, enter, 200);
//        //   return MoveAnimation.create(MoveAnimation.LEFT, enter, 200);
//        return PushPullAnimation.create(PushPullAnimation.LEFT, enter, 200);
//        //     return SidesAnimation.create(SidesAnimation.LEFT, enter, 200);
////        return SidesAnimation.create(SidesAnimation.LEFT, enter, 200);
////        return super.onCreateAnimation(transit, enter, nextAnim);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fm_song_list_v4, null);
        mSongPager = mRootView.findViewById(R.id.songPager);
        mWPager = mRootView.findViewById(R.id.w_page);

        mWidgetTopTabs = mRootView.findViewById(R.id.topTab);
        mWidgetKeyboard = mRootView.findViewById(R.id.keyboard);

        mEmptyView = mRootView.findViewById(R.id.rl_empty);
        mEmptyView.findViewById(R.id.tv_feedback).setOnClickListener(view ->
                SongFeedback()
        );
        mWidgetTopTabs.setLeftTabClickListener(leftOnTabClickListener);
        mWidgetTopTabs.setRightTabClickListener(rightOnTabClickListener);

        mWidgetKeyboard.setInputTextChangedListener(this);
        mWidgetKeyboard.showLackSongButton(true);

        mWidgetTopTabs.setRightTabShow(false);
        mSongPager.setOnPagerScrollListener(this);

        mSongPager.setOnPreviewSongListener(this);

        mSongPager.setOnSongSelectListener(this);

        mWPager.setOnPageChangedListener(this);

        SkinManager.getInstance().register(mRootView);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        SkinManager.getInstance().unregister(mRootView);
        super.onDestroyView();
    }

    @Override
    public void onPrePageClick(int pageBefore, int pageCurrent) {
        Logger.d("FmSongBase", "onPrePageClick current:" + pageCurrent);
        mSongPager.setCurrentItem(pageCurrent);
    }

    @Override
    public void onNextPageClick(int pageBefore, int pageCurrent) {
        Logger.d("FmSongBase", "onNextPageClick current:" + pageCurrent);
        mSongPager.setCurrentItem(pageCurrent);
    }

    @Override
    public void onFirstPageClick(int pageBefore, int pageCurrent) {
        Logger.d("FmSongBase", "onNextPageClick current:" + pageCurrent);
        mSongPager.setCurrentItem(pageCurrent);
        mWPager.setPrePressed(false);
        mWPager.setNextPressed(false);
    }

    @Override
    public void onWordCountChanged(int count) {

    }

    @Override
    public void onInputTextChanged(String text) {
        mSearchKeyword = text;
    }

    private WidgetTopTabs.OnTabClickListener leftOnTabClickListener = this::onLeftTabClick;

    private WidgetTopTabs.OnTabClickListener rightOnTabClickListener = this::onRightTabClick;


    public void onLeftTabClick(int position) {
    }

    public void onRightTabClick(int position) {
    }

    public void initSongPager(int totalPage, List<SongSimple> firstPageSong, Map<String, String> params) {
        Logger.i(getClass().getSimpleName(), "Current total page:" + totalPage);
        mWPager.setPageCurrent(0);
        mWPager.setPageTotal(totalPage);
        mSongPager.initPager(totalPage, firstPageSong, params);
        showEmptyView(firstPageSong == null || firstPageSong.isEmpty());
    }

    @Override
    public void onPreviewSong(SongSimple song, int ps) {
        new PreviewDialog(getActivity(), song, String.valueOf(ps)).show();
    }

    @Override
    public void onSongSelectListener(SongSimple song) {
        mSearchKeyword = "";
        mWidgetKeyboard.setCleanText(true);
    }

    public void showEmptyView(boolean show) {
        mEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        mSongPager.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPageScrollLeft() {
        mWPager.setPrePressed(true);
        mWPager.setNextPressed(false);
    }

    @Override
    public void onPageScrollRight() {
        mWPager.setPrePressed(false);
        mWPager.setNextPressed(true);
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
        Logger.d("FmSongBase", "onPagerSelected >>>>>>>>>> " + position);
        mWPager.setPageCurrent(position);
        mSongPager.runLayoutAnimation(isLeft);
        mWPager.setPrePressed(false);
        mWPager.setNextPressed(false);
    }

    private void SongFeedback() {
        PromptDialogBig promptDialogBig = new PromptDialogBig(getContext());
        promptDialogBig.setTitle(R.string.lack_song);
        promptDialogBig.setTips(R.string.lack_song_tips);
        promptDialogBig.setQrcode(R.drawable.qrcode);
        promptDialogBig.setOkButton(false, "", null);
        promptDialogBig.setCancleButton(false, "", null);
        promptDialogBig.show();
    }
}
