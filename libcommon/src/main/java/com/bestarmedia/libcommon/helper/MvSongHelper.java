package com.bestarmedia.libcommon.helper;

import android.content.Context;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.vod.MvDetailV4;
import com.bestarmedia.libcommon.model.vod.MvInfo;
import com.bestarmedia.libcommon.util.Logger;

/**
 * Created by J Wong on 2018/8/24.
 */

public class MvSongHelper implements HttpRequestListener {

    private Context mContext;
    private int mMvId;
    private OnMvSongDetailListener mOnMvSongDetailListener;


    public MvSongHelper(Context context, int mvId) {
        this.mContext = context;
        this.mMvId = mvId;
    }

    public void getMvSongDetail() {
        HttpRequestV4 request = initRequest(RequestMethod.V4.CLOUD_USER_MV + "/" + mMvId);
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
        Logger.d(getClass().getSimpleName(), "onSuccess method:" + method);
        MvDetailV4 detailV4;
        if (object != null && object instanceof MvDetailV4 && (detailV4 = (MvDetailV4) object) != null && detailV4.song != null) {
            if (mOnMvSongDetailListener != null) {
                mOnMvSongDetailListener.onMvSongDetail(detailV4.song);
            }
        } else {
            if (mOnMvSongDetailListener != null) {
                mOnMvSongDetailListener.onMvSongFail("model数据不对!");
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        String error="";
        if(object instanceof BaseModelV4){
            BaseModelV4 baseModelV4= (BaseModelV4) object;
            error=baseModelV4.tips;
        }else if(object instanceof String){
            error=object.toString();
        }
        Logger.d(getClass().getSimpleName(), "onFailed error:" + error);
        if (mOnMvSongDetailListener != null) {
            mOnMvSongDetailListener.onMvSongFail(error);
        }
    }

    @Override
    public void onError(String method, String error) {

    }


    public void setOnMvSongDetailListener(OnMvSongDetailListener listener) {
        mOnMvSongDetailListener = listener;
    }

    public interface OnMvSongDetailListener {
        void onMvSongDetail(MvInfo mvInfo);

        void onMvSongFail(String error);
    }
}
