package com.beidousat.karaoke.helper;

import android.content.Context;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.play.CoolScreen;
import com.bestarmedia.libcommon.model.vod.play.Hyun;

import java.util.Random;

/**
 * 炫屏
 */
public class CoolScreenHelper implements HttpRequestListener {

    private Context mContext;
    private OnCoolScreenCallback onCoolScreenCallback;

    public CoolScreenHelper(Context context) {
        this.mContext = context;
    }

    public void setOnCoolScreenCallback(OnCoolScreenCallback callback) {
        this.onCoolScreenCallback = callback;
    }

    public void get() {
        HttpRequestV4 r = initRequest(RequestMethod.V4.COOL_SCREEN);
        r.addParam("current_page", String.valueOf(1));
        r.addParam("per_page", String.valueOf(1000));
        r.setConvert2Class(CoolScreen.class);
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
        if (RequestMethod.V4.COOL_SCREEN.equalsIgnoreCase(method)) {
            if (object instanceof CoolScreen) {
                CoolScreen page = (CoolScreen) object;
                if (page.hyunList != null && !page.hyunList.data.isEmpty()) {
                    if (onCoolScreenCallback != null) {
                        Random random = new Random();//返回随机样式
                        onCoolScreenCallback.onRandomCoolScreen(page.hyunList.data.get(random.nextInt(page.hyunList.data.size())));
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {

    }

    @Override
    public void onError(String method, String error) {

    }

    public interface OnCoolScreenCallback {
        void onRandomCoolScreen(Hyun coolScreen);
    }
}
