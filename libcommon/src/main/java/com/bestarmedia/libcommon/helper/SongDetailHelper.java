package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.util.Log;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.v4.Song;
import com.bestarmedia.libcommon.model.vod.SongDetailsV4;


/**
 * Created by J Wong on 2018/8/24.
 */

public class SongDetailHelper implements HttpRequestListener {

    private Context mContext;
    private String mSongId;
    private OnSongDetailListener mOnSongDetailListener;


    public SongDetailHelper(Context context, String songId) {
        this.mContext = context;
        this.mSongId = songId;
    }

    public void getSongDetail() {
        HttpRequestV4 request = initRequest(RequestMethod.V4.V4_API_SONG + "/" + mSongId);
        request.setConvert2Class(SongDetailsV4.class);
        request.get();
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
        Log.d(getClass().getSimpleName(), "onSuccess method:" + method);
        SongDetailsV4 songV4;
        if (object != null && object instanceof SongDetailsV4 && (songV4 = (SongDetailsV4) object) != null && songV4.song != null) {
            if (mOnSongDetailListener != null) {
                mOnSongDetailListener.onSongDetail(songV4.song);
            }
        } else {
            if (mOnSongDetailListener != null) {
                mOnSongDetailListener.onFail("model数据不对!");
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (obj instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) obj;
            Log.d(getClass().getSimpleName(), "onFailed error:" + baseModelV4.tips);
            if (mOnSongDetailListener != null) {
                mOnSongDetailListener.onFail(baseModelV4.tips);
            }
        } else if (obj instanceof String) {
            Log.d(getClass().getSimpleName(), "onFailed error:" + obj.toString());
            if (mOnSongDetailListener != null) {
                mOnSongDetailListener.onFail(obj.toString());
            }
        }

    }

    @Override
    public void onError(String method, String error) {
        Log.d(getClass().getSimpleName(), "onFailed error:" + error);
        if (mOnSongDetailListener != null) {
            mOnSongDetailListener.onFail(error);
        }
    }


    public void setOnSongDetailListener(OnSongDetailListener listener) {
        mOnSongDetailListener = listener;
    }

    public interface OnSongDetailListener {
        void onSongDetail(Song songDetail);

        void onFail(String error);
    }
}
