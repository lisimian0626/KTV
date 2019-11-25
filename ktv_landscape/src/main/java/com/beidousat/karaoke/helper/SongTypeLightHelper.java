package com.beidousat.karaoke.helper;

import android.content.Context;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.SongTypeLight;

/**
 * 根据曲种获取灯光
 */
public class SongTypeLightHelper implements HttpRequestListener {

    private Context mContext;
    private OnSongTypeLightCallback onSongTypeLightCallback;

    public SongTypeLightHelper(Context context) {
        this.mContext = context;
    }

    public void setOnSongTypeLightCallback(OnSongTypeLightCallback callback) {
        this.onSongTypeLightCallback = callback;
    }

    public void getSongTypeLight(int songTypeId) {
        HttpRequestV4 r = initRequest(RequestMethod.V4.SONG_TYPE_LIGHT);
        r.addParam("song_type_id", String.valueOf(songTypeId));
        r.setConvert2Class(SongTypeLight.class);
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
        SongTypeLight songTypeLight;
        if (RequestMethod.V4.SONG_TYPE_LIGHT.equalsIgnoreCase(method) && object instanceof SongTypeLight
                && (songTypeLight = (SongTypeLight) object) != null && songTypeLight.songTypeLight != null) {
            if (onSongTypeLightCallback != null) {
                onSongTypeLightCallback.onSongTypeLight(songTypeLight);
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {

    }

    @Override
    public void onError(String method, String error) {

    }

    public interface OnSongTypeLightCallback {
        void onSongTypeLight(SongTypeLight light);
    }
}
