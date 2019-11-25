package com.beidousat.karaoke.helper;

import android.content.Context;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.v4.SongCate;
import com.bestarmedia.libcommon.model.v4.SongCategories;
import com.bestarmedia.libcommon.model.vod.SongTypeLight;

/**
 * 获取曲种信息
 */
public class SongTypeHelper implements HttpRequestListener {

    private Context mContext;
    private OnSongTypeCallback onSongTypeCallback;

    public SongTypeHelper(Context context) {
        this.mContext = context;
    }

    public void setOnSongTypeLightCallback(OnSongTypeCallback callback) {
        this.onSongTypeCallback = callback;
    }

    public void getSongType(int songTypeId) {
        HttpRequestV4 r = initRequest(RequestMethod.V4.V4_API_SONG_SONG_TYPE + "" + songTypeId);
        r.setConvert2Class(SongCategories.class);
        r.get();
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
        SongCategories songCategories;
        if (method.startsWith(RequestMethod.V4.V4_API_SONG_SONG_TYPE) && object instanceof SongCategories
                && (songCategories = (SongCategories) object) != null && songCategories.song_type != null && !songCategories.song_type.isEmpty()) {
            if (onSongTypeCallback != null) {
                onSongTypeCallback.onSongType(songCategories.song_type.get(0));
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {

    }

    @Override
    public void onError(String method, String error) {

    }

    public interface OnSongTypeCallback {
        void onSongType(SongCate songType);
    }
}
