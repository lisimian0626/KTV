package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.util.Log;

import com.bestarmedia.libcommon.eventbus.EventBusId;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.PayApiListener;
import com.bestarmedia.libcommon.model.ad.ResourceRequestBody;
import com.bestarmedia.libcommon.model.ad.ResourceRequestResult;
import com.bestarmedia.libcommon.model.vod.Pay;


/**
 * Created by J Wong on 2017/5/15.
 */
public class PayApiHelper implements HttpRequestListener {

    private static String Tag = PayApiHelper.class.getSimpleName();
    private Context mContext;
    private PayApiListener mPayApiListener;

    public PayApiHelper(Context context) {
        this.mContext = context;
    }

    public void setPayApiListener(PayApiListener payApiListener) {
        this.mPayApiListener = payApiListener;
    }

    public void checkPay(String roomCode, String session) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.PAY);
        request.addParam("room_code", roomCode);
        request.addParam("room_session", session);
        request.setHttpRequestListener(this);
        request.setConvert2Class(Pay.class);
        request.get();
    }

    public void record(int action, String mediaName) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.PAY_RESOURCE);
        ResourceRequestBody body = new ResourceRequestBody(action, mediaName);
        request.setHttpRequestListener(this);
        request.setConvert2Class(ResourceRequestResult.class);
        request.postJson(body.toJson());
    }


    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.V4.PAY.equalsIgnoreCase(method)) {
            Pay pay;
            if (object instanceof Pay && (pay = (Pay) object).payInfo != null) {
                if (mPayApiListener != null) {
                    mPayApiListener.onPayInfo(pay.payInfo);
                }
            }
        } else if (RequestMethod.V4.PAY_RESOURCE.equals(method)) {
            if (object instanceof ResourceRequestResult) {
                ResourceRequestResult result = (ResourceRequestResult) object;
                Log.d(Tag, "上报资源展示次数：" + (result.code == 1 ? "成功" : "失败"));
            }
        }
        EventBusUtil.postSticky(EventBusId.Id.PAY_INTERFACE_ERROR, 0);
    }

    @Override
    public void onFailed(String method, Object object) {
        Log.e(Tag, "onFailed：" + object.toString());
        EventBusUtil.postSticky(EventBusId.Id.PAY_INTERFACE_ERROR, 1);
    }

    @Override
    public void onError(String method, String error) {
        Log.e(Tag, "onError：" + error);
        EventBusUtil.postSticky(EventBusId.Id.PAY_INTERFACE_ERROR, 1);
    }
}
