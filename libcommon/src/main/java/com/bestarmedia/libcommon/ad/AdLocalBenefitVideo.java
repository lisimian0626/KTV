package com.bestarmedia.libcommon.ad;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bestarmedia.libcommon.R;
import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.model.vod.PublicBroadcasting;
import com.bestarmedia.libcommon.model.vod.PublicBroadcastingV4;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2017/12/27.
 */

public class AdLocalBenefitVideo implements HttpRequestListener {

    volatile private static AdLocalBenefitVideo mAdLocalBenefitVideo;

    private Context mContext;

    private List<PublicBroadcasting> mPublicBroadcastingAll = new ArrayList<>();

    private List<PublicBroadcasting> mPublicBroadcastingRoomOpen = new ArrayList<>();

    private List<PublicBroadcasting> mPublicBroadcastingRoomClose = new ArrayList<>();

    private int mPreALLCallbackPs = 0;

    private int mPreOpenCallbackPs = 0;

    private int mPreCloseCallbackPs = 0;

    private boolean isPlayLocalOnly = false;

    private AdLocalBenefitVideoListener mListener;

    public static AdLocalBenefitVideo getInstance(Context context) {
        if (mAdLocalBenefitVideo == null) {
            synchronized (AdLocalBenefitVideo.class) {
                if (mAdLocalBenefitVideo == null)
                    mAdLocalBenefitVideo = new AdLocalBenefitVideo(context.getApplicationContext());
            }
        }
        return mAdLocalBenefitVideo;
    }

    private AdLocalBenefitVideo(Context context) {
        this.mContext = context;
    }

    public boolean getIsPlayLocalOnly() {
        return isPlayLocalOnly;
    }

    public void setPlayLocalOnly(boolean isPlayLocalOnly) {
        this.isPlayLocalOnly = isPlayLocalOnly;
        Toast.makeText(mContext, isPlayLocalOnly ? R.string.check_mode_enable : R.string.check_mode_disable, Toast.LENGTH_LONG).show();
    }

    public void setAdLocalBenefitVideoListener(AdLocalBenefitVideoListener listener) {
        mListener = listener;
    }

//    public void getAdLocalBetweenT1Ad() {
//        Logger.d(getClass().getSimpleName(), "PublicServiceAd playPublicServiceAdBetweenT1 Callback ====================>");
//        getAdLocalVideo();
//    }

    public void getAdLocalVideo() {
        if (getIsPlayLocalOnly()) {
            if (mPublicBroadcastingAll == null || mPublicBroadcastingAll.size() <= 0) {//没有公益广告视频
                Logger.d(getClass().getSimpleName(), "PublicServiceAd getAdLocalVideo requestAd open room  ====================>");
                if (mListener != null)
                    mListener.onAdLocalBenefitVideoCallback(AdModelDefault.getPublicServiceAd().get(0));
            } else {
                Logger.d(getClass().getSimpleName(), "PublicServiceAd getAdLocalVideo callAdLocalBenefit ====================>");
                callAdLocalBenefit();
            }
        } else {
            if (NodeRoomInfo.getInstance().isRoomOpen()) {
                if (mPublicBroadcastingRoomOpen == null || mPublicBroadcastingRoomOpen.size() <= 0) {//没有公益广告视频
                    Logger.d(getClass().getSimpleName(), "PublicServiceAd getAdLocalVideo requestAd open room  ====================>");
                    if (mListener != null)
                        mListener.onAdLocalBenefitVideoCallback(AdModelDefault.getPublicServiceAd().get(0));
                } else {
                    Logger.d(getClass().getSimpleName(), "PublicServiceAd getAdLocalVideo callAdLocalBenefit ====================>");
                    callAdLocalBenefit();
                }
            } else {
                if (mPublicBroadcastingRoomClose == null || mPublicBroadcastingRoomClose.size() <= 0) {//没有公益广告视频
                    Logger.d(getClass().getSimpleName(), "PublicServiceAd getAdLocalVideo requestAd  close room ====================>");
                    if (mListener != null)
                        mListener.onAdLocalBenefitVideoCallback(AdModelDefault.getPublicServiceAd().get(0));
                } else {
                    Logger.d(getClass().getSimpleName(), "PublicServiceAd getAdLocalVideo callAdLocalBenefit ====================>");
                    callAdLocalBenefit();
                }
            }
        }
        requestAd();
    }

    private void requestAd() {
        HttpRequestV4 r = initRequest(RequestMethod.NODE_PUBLIC_BROADCASTING);
        r.setConvert2Class(PublicBroadcastingV4.class);
        r.get();
    }

    private void callAdLocalBenefit() {
        ADModel ad;
        if (getIsPlayLocalOnly()) {
            Logger.d(getClass().getSimpleName(), "PublicServiceAd callAdLocalBenefit getIsPlayLocalOnly ====================>");
            if (mPublicBroadcastingAll != null && mPublicBroadcastingAll.size() > 0) {
                int ps = mPreALLCallbackPs % mPublicBroadcastingAll.size();
                if (mPublicBroadcastingAll.size() > ps) {
                    PublicBroadcasting broadcasting = mPublicBroadcastingAll.get(ps);
                    ad = new ADModel();
                    ad.setAdContent(broadcasting.file_path);
                    mPreALLCallbackPs++;
                } else {
                    ad = AdModelDefault.getPublicServiceAd().get(0);
                }
            } else {
                ad = AdModelDefault.getPublicServiceAd().get(0);
            }
        } else {
            if (NodeRoomInfo.getInstance().isRoomOpen()) {
                Logger.d(getClass().getSimpleName(), "PublicServiceAd callAdLocalBenefit is Room Open====================>");
                if (mPublicBroadcastingRoomOpen != null && mPublicBroadcastingRoomOpen.size() > 0) {
                    int ps = mPreOpenCallbackPs % mPublicBroadcastingRoomOpen.size();
                    if (mPublicBroadcastingRoomOpen.size() > ps) {
                        PublicBroadcasting broadcasting = mPublicBroadcastingRoomOpen.get(ps);
                        ad = new ADModel();
                        ad.setAdContent(broadcasting.file_path);
                        mPreOpenCallbackPs++;
                    } else {
                        ad = AdModelDefault.getPublicServiceAd().get(0);
                    }
                } else {
                    ad = AdModelDefault.getPublicServiceAd().get(0);
                }
            } else {
                Logger.d(getClass().getSimpleName(), "PublicServiceAd callAdLocalBenefit is Room Close====================>");

                if (mPublicBroadcastingRoomClose != null && mPublicBroadcastingRoomClose.size() > 0) {
                    int ps = mPreCloseCallbackPs % mPublicBroadcastingRoomClose.size();
                    if (mPublicBroadcastingRoomClose.size() > ps) {
                        PublicBroadcasting broadcasting = mPublicBroadcastingRoomClose.get(ps);
                        ad = new ADModel();
                        ad.setAdContent(broadcasting.file_path);
                        mPreCloseCallbackPs++;
                    } else {
                        ad = AdModelDefault.getPublicServiceAd().get(0);
                    }
                } else {
                    ad = AdModelDefault.getPublicServiceAd().get(0);
                }
            }
        }
        if (mListener != null)
            mListener.onAdLocalBenefitVideoCallback(ad);
    }

    public void cleanCache() {
        Logger.d(getClass().getSimpleName(), "cleanCache >>>>>>>>>>>>>>>>>>>>>>");
        mPublicBroadcastingAll.clear();
        mPublicBroadcastingRoomOpen.clear();
        mPublicBroadcastingRoomClose.clear();
    }

    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_PUBLIC_BROADCASTING.equals(method)) {
            PublicBroadcastingV4 publicBroadcastingV4;
            if (object instanceof PublicBroadcastingV4 && (publicBroadcastingV4 = (PublicBroadcastingV4) object).broadcasting != null
                    && publicBroadcastingV4.broadcasting.size() > 0) {
                mPublicBroadcastingAll.clear();
                mPublicBroadcastingRoomOpen.clear();
                mPublicBroadcastingRoomClose.clear();
                mPublicBroadcastingAll.addAll(publicBroadcastingV4.broadcasting);
                for (PublicBroadcasting broadcasting : mPublicBroadcastingAll) {
                    if (broadcasting.is_play_room_open == 1) {//
                        mPublicBroadcastingRoomOpen.add(broadcasting);
                    }
                    if (broadcasting.is_play_room_close == 1) {
                        mPublicBroadcastingRoomClose.add(broadcasting);
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (RequestMethod.NODE_PUBLIC_BROADCASTING.equals(method)) {
            Log.w(getClass().getSimpleName(), "获取本地公播失败！");
        }
    }

    @Override
    public void onError(String method, String error) {

    }


    public interface AdLocalBenefitVideoListener {
        void onAdLocalBenefitVideoCallback(ADModel ad);
    }

}
