package com.beidousat.karaoke.ui.fragment.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beidousat.karaoke.R;
import com.beidousat.karaoke.ui.fragment.BaseFragment;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.ad.BannerInfo;
import com.bestarmedia.libcommon.model.ad.BannersV4;
import com.bestarmedia.libwidget.banner.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmPromotionDetail extends BaseFragment {

    private View mRootView;
    private Banner mIvBanner;

    public static FmPromotionDetail newInstance() {
        return new FmPromotionDetail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.fm_banner_detail, null);
        mIvBanner = mRootView.findViewById(R.id.banner);
        request();
        return mRootView;
    }

    private void request() {
        HttpRequestV4 requestV4 = initRequestV4(RequestMethod.NODE_PROMOTION);
        requestV4.addParam("current_page", String.valueOf(1));
        requestV4.addParam("per_page", String.valueOf(50));
        requestV4.addParam("status", String.valueOf(1));
        requestV4.setConvert2Class(BannersV4.class);
        requestV4.get();
    }


    @Override
    public void onStart(String method) {
        super.onStart(method);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.NODE_PROMOTION.equals(method)) {
                BannersV4 bannersV4 = (BannersV4) object;
                if (bannersV4 != null && bannersV4.sale != null && bannersV4.sale.data != null && bannersV4.sale.data.size() > 0) {
                    List<String> list_url = new ArrayList<>();
                    for (BannerInfo banner : bannersV4.sale.data) {
                        if (!TextUtils.isEmpty(banner.image)) {
                            list_url.add(banner.image);
                        }
                    }
                    mIvBanner.setImageLoader(new GlideImageLoader());
                    mIvBanner.setImages(list_url);
                    mIvBanner.start();
                }
            }
        }
        super.onSuccess(method, object);
    }

    @Override
    public void onFailed(String method, Object object) {
        super.onFailed(method, object);
    }

}
