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
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.service.EvacuationV4;
import com.bestarmedia.libwidget.banner.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/12/17 17:34.
 */
public class FmFireDiagram extends BaseFragment {

    private View mRootView;
    private Banner mIvBanner;

    public static FmFireDiagram newInstance() {
        return new FmFireDiagram();
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
        HttpRequestV4 requestV4 = initRequestV4(RequestMethod.NODE_EVACUATION);
        requestV4.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        requestV4.setConvert2Class(EvacuationV4.class);
        requestV4.get();
    }


    @Override
    public void onStart(String method) {
        super.onStart(method);
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (isAdded()) {
            if (RequestMethod.NODE_EVACUATION.equals(method)) {
                EvacuationV4 evacuationV4 = (EvacuationV4) object;
                if (evacuationV4 != null && evacuationV4.evacuation_lan != null && !TextUtils.isEmpty(evacuationV4.evacuation_lan.evacuation_lan)) {
                    List<String> list_url = new ArrayList<>();
                    list_url.add(evacuationV4.evacuation_lan.evacuation_lan);
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
