package com.beidousat.karaoke.helper;

import android.content.Context;

import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.CornerStylePage;

import java.util.Random;

/**
 * 获取角标样式
 */
public class CornerStyleHelper implements HttpRequestListener {

    private Context mContext;
    private CornerStyleCallback cornerStyleCallback;

    public CornerStyleHelper(Context context) {
        this.mContext = context;
    }

    public void setCornerStyleCallback(CornerStyleCallback callback) {
        this.cornerStyleCallback = callback;
    }

    public void getCornerStyle() {
        HttpRequestV4 r = initRequest(RequestMethod.V4.CORNER_STYLE);
        r.setConvert2Class(CornerStylePage.class);
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
        CornerStylePage page;
        if (RequestMethod.V4.CORNER_STYLE.equalsIgnoreCase(method) && object instanceof CornerStylePage
                && (page = (CornerStylePage) object) != null && page.corner != null && !page.corner.data.isEmpty()) {
            if (cornerStyleCallback != null) {
                Random random = new Random();//返回随机样式
                cornerStyleCallback.onCornerStyle(page.corner.data.get(random.nextInt(page.corner.data.size())));
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {

    }

    @Override
    public void onError(String method, String error) {

    }

    public interface CornerStyleCallback {
        void onCornerStyle(CornerStylePage.CornerStyle cornerStyle);
    }
}
