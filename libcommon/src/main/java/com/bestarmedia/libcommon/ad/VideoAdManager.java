package com.bestarmedia.libcommon.ad;

import android.content.Context;
import android.text.TextUtils;

import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.interf.AdsRequestListenerV4;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class VideoAdManager implements AdLocalBenefitVideo.AdLocalBenefitVideoListener, AdsRequestListenerV4 {

    private static VideoAdManager videoAdManager;
    private Context context;
    private List<OnAdVideoListener> onAdVideoListeners = new ArrayList<>();
    private int playAdCount = 0;//累计播放广告次数
    private final static String TAG = "VideoAdManager";

    public static VideoAdManager getInstance(Context context) {
        if (videoAdManager == null) {
            synchronized (VideoAdManager.class) {
                if (videoAdManager == null) {
                    videoAdManager = new VideoAdManager(context.getApplicationContext());
                }
            }
        }
        return videoAdManager;
    }

    private VideoAdManager(Context context) {
        this.context = context;
        AdLocalBenefitVideo.getInstance(this.context).setAdLocalBenefitVideoListener(this);
    }

    public interface OnAdVideoListener {
        void onAdModel(ADModel model);
    }

    public void addOnAdVideoListener(OnAdVideoListener listener) {
        onAdVideoListeners.add(listener);
    }

    private void playPublicServiceAd() {
        AdLocalBenefitVideo.getInstance(context).getAdLocalVideo();
    }

    private void callbackAdModel(ADModel adModel) {
        if (onAdVideoListeners != null) {
            for (OnAdVideoListener listener : onAdVideoListeners) {
                listener.onAdModel(adModel);
            }
        }
    }

    /**
     * 获取公播视频，已点歌曲列表为空时调用
     */
    public void getPublicVideo() {
        if (AdLocalBenefitVideo.getInstance(context).getIsPlayLocalOnly()) {//检查模式只播放本地广告
            playPublicServiceAd();
        } else {
            if (NodeRoomInfo.getInstance().isRoomOpen()) {
                if (playAdCount % 10 == 0) {//每播放10次播放一次公益广告
                    playPublicServiceAd();
                } else {
                    Logger.d(TAG, "PublicServiceAd playAds T1 ====================>");
                    AdGetterV4 adGetterV4 = new AdGetterV4(context, this);
                    adGetterV4.getADbyPos("T1");
                }
            } else {
                Logger.d(TAG, " play public  ad  playAds");
                playPublicServiceAd();
            }
        }
        playAdCount++;
    }

    @Override
    public void onAdsRequestSuccess(List<ADModel> adModelList) {
        if (adModelList != null && adModelList.size() > 0 && !TextUtils.isEmpty(adModelList.get(0).getAdContent())) {
            callbackAdModel(adModelList.get(0));
        } else {//无广告，播放本地公益广告
            playPublicServiceAd();
        }
    }

    @Override
    public void onAdsRequestFail() {
        playPublicServiceAd();
    }

    @Override
    public void onAdLocalBenefitVideoCallback(ADModel ad) {
        Logger.d(TAG, "PublicServiceAd onAdLocalBenefitVideoCallback ====================>");
        if (ad != null && !TextUtils.isEmpty(ad.getAdContent())) {
            callbackAdModel(ad);
        } else {
            callbackAdModel(AdModelDefault.getPublicServiceAd().get(0));
        }
    }
}
