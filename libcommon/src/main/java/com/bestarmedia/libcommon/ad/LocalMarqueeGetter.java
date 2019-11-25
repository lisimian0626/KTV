package com.bestarmedia.libcommon.ad;

import android.content.Context;
import android.text.TextUtils;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.ad.LocalMarquee;


/**
 * Created by J Wong on 2015/12/11 17:58.
 */
public class LocalMarqueeGetter implements HttpRequestListener {

    private Context mContext;
    private AdMarqueeRequestListener mAdMarqueeRequestListener;

    public interface AdMarqueeRequestListener {
        void onAdMarqueeRequest(LocalMarquee localMarquee);

        void onAdMarqueeRequestFail();
    }


    public LocalMarqueeGetter(Context context, AdMarqueeRequestListener listener) {
        mContext = context;
        mAdMarqueeRequestListener = listener;
    }

    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext.getApplicationContext(), method);
        request.setHttpRequestListener(this);
        return request;
    }

    public void getMarquee(String preAdID) {
        HttpRequestV4 r = initRequest(RequestMethod.NODE_RANGE_NOTICE);
        r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        r.addParam("last_id", TextUtils.isEmpty(preAdID) ? String.valueOf(0) : preAdID);
        r.addParam("total", String.valueOf(1));
        r.setConvert2Class(LocalMarquee.class);
        r.get();
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onFailed(String method, Object obj) {
        if (mAdMarqueeRequestListener != null) {
            mAdMarqueeRequestListener.onAdMarqueeRequestFail();
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_RANGE_NOTICE.equals(method)) {
            if (object instanceof LocalMarquee) {
                LocalMarquee localMarquee = (LocalMarquee) object;
                mAdMarqueeRequestListener.onAdMarqueeRequest(localMarquee);
            }
        }
    }
}
