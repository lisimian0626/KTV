package com.beidousat.karaoke.ui.fragment.play;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.VodApplication;
import com.beidousat.karaoke.interf.OnPageScrollListener;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.beidousat.karaoke.widget.viewpager.WidgetHyunPager;
import com.beidousat.karaoke.widget.viewpager.WidgetPage;
import com.bestarmedia.libcommon.eventbus.BusEvent;
import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.play.CoolScreen;
import com.bestarmedia.libcommon.model.vod.play.Hyun;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.image.RecyclerImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

/**
 * Created by J Wong on 2015/12/16 16:43.
 */
public class FmHyun extends BaseFragment implements OnPageScrollListener, WidgetPage.OnPageChangedListener, View.OnClickListener {

    private View mRootView;
    private WidgetHyunPager mWidgetHyunPager;
    private WidgetPage mWidgetPage;
    private Map<String, String> mRequestParam;
    private RecyclerImageView rivSwitcher, rivRandom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fm_hyun_setting, null);
            mWidgetHyunPager = mRootView.findViewById(R.id.hyunPager);
            mWidgetPage = mRootView.findViewById(R.id.w_page);
            rivSwitcher = mRootView.findViewById(R.id.riv_switch);
            rivRandom = mRootView.findViewById(R.id.riv_random);
            rivSwitcher.setOnClickListener(this);
            rivRandom.setOnClickListener(this);
            mWidgetPage.setOnPageChangedListener(this);
            mWidgetHyunPager.setOnPagerScrollListener(this);
            requestHyun();
        }

        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        refreshView();

        EventBus.getDefault().register(this);

        return mRootView;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BusEvent event) {
        if (event.id == EventBusId.ImId.COOL_SCREEN_CHANGED)
            refreshView();
    }

    private void refreshView() {
        rivSwitcher.setImageResource(VodApplication.getKaraokeController().getCoolScreenStatus().isOpen == 1 ? 0 : R.drawable.play_scenes_s);
        rivRandom.setImageResource(VodApplication.getKaraokeController().getCoolScreenStatus().isRandom == 1 ? R.drawable.play_scenes_s : 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.riv_switch:
                VodApplication.getKaraokeController().setCoolScreenStatus(0, 0);
                break;
            case R.id.riv_random:
                VodApplication.getKaraokeController().setCoolScreenStatus(1, 1);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }


    private void requestHyun() {
        HttpRequestV4 r = initRequestV4(RequestMethod.V4.COOL_SCREEN);
        r.addParam("current_page", String.valueOf(1));
        r.addParam("per_page", String.valueOf(4));
        mRequestParam = r.getParams();
        r.setConvert2Class(CoolScreen.class);
        r.get();
    }


    public void initHyunPager(int totalPage, List<Hyun> hyuns, Map<String, String> params) {
        Logger.i(getClass().getSimpleName(), "Current total page:" + totalPage);
        mWidgetPage.setPageCurrent(0);
        mWidgetPage.setPageTotal(totalPage);
        mWidgetHyunPager.initPager(totalPage, hyuns, params);
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
        if (isAdded()) {
            if (RequestMethod.V4.COOL_SCREEN.equalsIgnoreCase(method)) {
                initHyunPager(0, null, mRequestParam);
            }
        }
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.V4.COOL_SCREEN.equalsIgnoreCase(method)) {
                CoolScreen coolScreen = (CoolScreen) object;
                if (coolScreen != null && coolScreen.hyunList != null && coolScreen.hyunList.data != null && coolScreen.hyunList.data.size() > 0) {
                    initHyunPager(coolScreen.hyunList.last_page, coolScreen.hyunList.data, mRequestParam);
                } else {
                    initHyunPager(0, null, mRequestParam);
                }
            }
        }
    }

    @Override
    public void onPageScrollLeft() {
        mWidgetPage.setPrePressed(true);
        mWidgetPage.setNextPressed(false);
        mWidgetHyunPager.notifyCurrentPage();
    }

    @Override
    public void onPageScrollRight() {
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(true);
        mWidgetHyunPager.notifyCurrentPage();
    }

    @Override
    public void onPagerSelected(int position, boolean isLeft) {
//        mSingerPager.runLayoutAnimation();
        mWidgetPage.setPageCurrent(position);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
        mWidgetHyunPager.notifyCurrentPage();
    }

    @Override
    public void onPrePageClick(int before, int current) {
        mWidgetHyunPager.setCurrentItem(current);
        mWidgetHyunPager.notifyCurrentPage();
    }

    @Override
    public void onNextPageClick(int before, int current) {
        mWidgetHyunPager.setCurrentItem(current);
        mWidgetHyunPager.notifyCurrentPage();
    }

    @Override
    public void onFirstPageClick(int before, int current) {
        mWidgetHyunPager.setCurrentItem(current);
        mWidgetPage.setPrePressed(false);
        mWidgetPage.setNextPressed(false);
        mWidgetHyunPager.notifyCurrentPage();
    }

}
