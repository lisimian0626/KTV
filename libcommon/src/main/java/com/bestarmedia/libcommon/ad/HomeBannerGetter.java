package com.bestarmedia.libcommon.ad;

import android.content.Context;
import android.text.TextUtils;

import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.ad.Recommend;


/**
 * Created by J Wong on 2015/12/11 17:58.
 */
public class HomeBannerGetter implements HttpRequestListener {
    private Context mContext;
    private HomeBannerRequestListenerV4 homeBannerRequestListenerV4;

    public HomeBannerGetter(Context context, HomeBannerRequestListenerV4 listener) {
        this.mContext = context;
        this.homeBannerRequestListenerV4 = listener;
    }

    public void getRecommend() {
        HttpRequestV4 requestV4 = initRequestV4();
        requestV4.setConvert2Class(Recommend.class);
        requestV4.get();
    }

    @Override
    public void onStart(String method) {

    }

    private HttpRequestV4 initRequestV4() {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), RequestMethod.V4.VOD_RECOMMEND);
        request.addParam("room_code", TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode()) ? "B1" : VodConfigData.getInstance().getRoomCode());
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.V4.VOD_RECOMMEND.equals(method)) {
            Recommend recommend = (Recommend) object;
            if (recommend != null && recommend.recommendInfoList != null && recommend.recommendInfoList.size() > 0) {
                if (homeBannerRequestListenerV4 != null)
                    homeBannerRequestListenerV4.onRequestSuccess(recommend.recommendInfoList);
            } else {
                if (homeBannerRequestListenerV4 != null)
                    homeBannerRequestListenerV4.onRequestFail();
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (homeBannerRequestListenerV4 != null)
            homeBannerRequestListenerV4.onRequestFail();
    }

    @Override
    public void onError(String method, String error) {

    }
}
