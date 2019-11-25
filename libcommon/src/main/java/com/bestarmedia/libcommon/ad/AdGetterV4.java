package com.bestarmedia.libcommon.ad;

import android.content.Context;
import android.text.TextUtils;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.AdsRequestListenerV4;
import com.bestarmedia.libcommon.model.ad.ADV4;
import com.bestarmedia.libcommon.util.DeviceUtil;

/**
 * Created by J Wong on 2015/12/12 16:10.
 */
public class AdGetterV4 implements HttpRequestListener {

    Context mContext;
    AdsRequestListenerV4 mAdsRequestListener;

    public AdGetterV4(Context context, AdsRequestListenerV4 listener) {
        this.mContext = context;
        this.mAdsRequestListener = listener;
    }


    public void getADbyPos(String adPosition) {
        getAd(adPosition, "", 0, "");
    }

    public void getADbyPos(String adPosition, int adId) {
        getAd(adPosition, "", adId, "");
    }

    public void getADbyPosAndID(String adPosition, String singerId) {
        getAd(adPosition, singerId, 0, "");
    }

    public void getAd(String adPosition, String singerId, int adId, String songId) {
        HttpRequestV4 requestV4 = initRequestV4(RequestMethod.NODE_AD + (adId > 0 ? ("/" + adId) : ""));
        requestV4.setConvert2Class(ADV4.class);
        requestV4.addParam("ad_position", adPosition);
        requestV4.addParam("room_code", TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode()) ? "B1" : VodConfigData.getInstance().getRoomCode());
        requestV4.addParam("serial_no", DeviceUtil.getCupSerial());
        if (!TextUtils.isEmpty(singerId)) {
            requestV4.addParam("singer_id", singerId.replace("|", ","));
        }
        if (!TextUtils.isEmpty(songId)) {
            requestV4.addParam("song_id", songId);
        }
        requestV4.get();
    }

    public void getAdBySongId(String adPosition, String songId) {
        getAd(adPosition, "", 0, songId);
    }

    public HttpRequestV4 initRequestV4(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_AD.equals(method)) {
            ADV4 ad = (ADV4) object;
            if (ad != null && ad.adModels != null && ad.adModels.size() > 0) {
                if (mAdsRequestListener != null)
                    mAdsRequestListener.onAdsRequestSuccess(ad.adModels);
            } else {
                if (mAdsRequestListener != null)
                    mAdsRequestListener.onAdsRequestFail();
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (mAdsRequestListener != null)
            mAdsRequestListener.onAdsRequestFail();
    }

    @Override
    public void onError(String method, String error) {
        if (mAdsRequestListener != null)
            mAdsRequestListener.onAdsRequestFail();
    }
}
