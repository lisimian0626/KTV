package com.beidousat.karaoke.ui.fragment.song;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.Logger;
import com.bestarmedia.libcommon.util.ServerFileUtil;
import com.bestarmedia.libwidget.banner.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmBannerDetail extends BaseFragment {
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    private ADModel mAdBanner;
    private View mRootView;
    private Banner mIvBanner;


    public static FmBannerDetail newInstance(ADModel adBanner) {
        FmBannerDetail fragment = new FmBannerDetail();
        Bundle args = new Bundle();
        args.putSerializable("banner", adBanner);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdBanner = (ADModel) getArguments().getSerializable("banner");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fm_banner_detail, null);
        mIvBanner =  mRootView.findViewById(R.id.banner);
        List<String> list_img = new ArrayList<>();
        if (mAdBanner != null && !TextUtils.isEmpty(mAdBanner.getAdContent())) {
            try {
                String[] urls= mAdBanner.getAdContent().split("\\|");
                Uri uri = ServerFileUtil.getImageUrl(urls[1]);
                list_img.add(uri.toString());
            }catch (Exception e){
                e.printStackTrace();
                Logger.w(getClass().getSimpleName(),"解析图片路径出错"+"  "+e.toString());
            }

        }
        mIvBanner.setImageLoader(new GlideImageLoader());
        mIvBanner.setImages(list_img);
        mIvBanner.start();

//        Glide.with(this).load(ServerFileUtil.getImageUrl(mAdBanner.ADContent1)).override(1200, 500).centerCrop().skipMemoryCache(true).into(mIvBanner);

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIvBanner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIvBanner.stopAutoPlay();
    }
}
