package com.beidousat.karaoke.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.ad.AdGetterV4;
import com.bestarmedia.libcommon.ad.AdPlayRecorder;
import com.bestarmedia.libcommon.interf.AdsRequestListenerV4;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libwidget.banner.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2018/12/10.
 */

public class WidgetAdBannerPlayer extends LinearLayout implements AdsRequestListenerV4, ViewPager.OnPageChangeListener {

    private View mRootView;

    private Banner banner;

    private List<ADModel> mAdBanners = new ArrayList<>();

    private List<String> mImageUris = new ArrayList<>();


    public WidgetAdBannerPlayer(Context context) {
        super(context);
        initView();
    }

    public WidgetAdBannerPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WidgetAdBannerPlayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_ad_banner_player, this);
        banner = mRootView.findViewById(R.id.banner);
        banner.setOnPageChangeListener(this);
    }

    public void loadAd() {
        AdGetterV4 adGetterV4 = new AdGetterV4(getContext(), this);
        adGetterV4.getADbyPos("B1");
    }

    public void startPlay() {
        banner.startAutoPlay();
    }

    public void stopPlay() {
        banner.stopAutoPlay();
    }


    @Override
    public void onAdsRequestSuccess(List<ADModel> adModelList) {
        Logger.d(getClass().getSimpleName(), "onAdsRequestSuccess >>>>>>>>>>>>>>>>>>>>>>> ");
        mAdBanners.clear();
        mImageUris.clear();
        if (adModelList != null && adModelList.size() > 0) {
            for (ADModel ad : adModelList) {
                if (!TextUtils.isEmpty(ad.getAdContent())) {
                    try {
                        String[] urls = ad.getAdContent().split("\\|");
                        mImageUris.add(urls[1]);
                        mAdBanners.add(ad);
                    } catch (Exception e) {
                        Logger.w(getClass().getSimpleName(), "解析图片路径出错" + "  " + e.toString());
                    }
                }
            }
        }
        if (mImageUris != null && mImageUris.size() > 0) {
            banner.setImageLoader(new GlideImageLoader());
            banner.setImages(mImageUris);
            banner.start();
        }
    }

    @Override
    public void onAdsRequestFail() {
//        if (mImageUris.isEmpty()) {
//            ADModel ad = new ADModel();
//            ad.setId(0);
//            ad.setAdContent(getResourceUri(R.drawable.banner_default_v).toString());
//            mAdBanners.add(ad);
//            mImageUris.add(getResourceUri(R.drawable.banner_default_v));
//            banner.setImageLoader(new GlideImageLoader());
//            banner.setImages(mImageUris);
//            banner.start();
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Logger.d(getClass().getSimpleName(), "onPageSelected >>>>>>>>>> " + position);
        if (mAdBanners != null && mAdBanners.size() > position && 0 != mAdBanners.get(position).getId()) {
            new AdPlayRecorder(getContext()).recordAdPlay(mAdBanners.get(position));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
