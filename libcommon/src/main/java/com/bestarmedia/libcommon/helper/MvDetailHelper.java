package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.util.Log;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.vod.MvDetailV4;
import com.bestarmedia.libcommon.model.vod.MvInfo;


/**
 * Created by J Wong on 2018/8/24.
 */

public class MvDetailHelper implements HttpRequestListener {

    private Context mContext;
    private int mvId;
    private OnMvDetailListener onMvDetailListener;


    public MvDetailHelper(Context context, int mvId) {
        this.mContext = context;
        this.mvId = mvId;
    }

    public void getDetail() {
        HttpRequestV4 request = initRequest(RequestMethod.V4.CLOUD_USER_MV + "/" + mvId);
        request.setConvert2Class(MvDetailV4.class);
        request.get();
    }


    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext, method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        Log.d(getClass().getSimpleName(), "onSuccess method:" + method);
        MvDetailV4 mvDetailV4;
        if (object != null && object instanceof MvDetailV4 && (mvDetailV4 = (MvDetailV4) object) != null && mvDetailV4.song != null) {
            if (onMvDetailListener != null) {
                onMvDetailListener.onMvDetail(mvDetailV4.song);
            }
        } else {
            if (onMvDetailListener != null) {
                onMvDetailListener.onFail("model数据不对!");
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (obj instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) obj;
            Log.d(getClass().getSimpleName(), "onFailed error:" + baseModelV4.tips);
            if (onMvDetailListener != null) {
                onMvDetailListener.onFail(baseModelV4.tips);
            }
        } else if (obj instanceof String) {
            Log.d(getClass().getSimpleName(), "onFailed error:" + obj.toString());
            if (onMvDetailListener != null) {
                onMvDetailListener.onFail(obj.toString());
            }
        }
    }

    @Override
    public void onError(String method, String error) {

    }

    public void setOnMvDetailListener(OnMvDetailListener listener) {
        onMvDetailListener = listener;
    }

    public interface OnMvDetailListener {
        void onMvDetail(MvInfo mvInfo);

        void onFail(String error);
    }
}
