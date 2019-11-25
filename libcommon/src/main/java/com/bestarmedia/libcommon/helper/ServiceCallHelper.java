package com.bestarmedia.libcommon.helper;

import android.content.Context;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.erp.ErpAccessResult;
import com.bestarmedia.libcommon.util.Logger;

/**
 * Created by J Wong on 2016/7/5.
 */
public class ServiceCallHelper implements HttpRequestListener {

    private Context mContext;

    public ServiceCallHelper(Context context) {
        this.mContext = context;
    }

    /**
     * 呼叫服务
     *
     * @param serviceType 0：呼叫服务员；1：DJ呼叫；2：清洁呼叫；3：保安呼叫；4：买单呼叫；5：催单呼叫；
     *                    6：唛套呼叫；7：加冰呼叫；
     */
    public void callService(int serviceType) {
        HttpRequestV4 request = initRequest(RequestMethod.NODE_ERP_ROOM_SERVICE);
        request.setConvert2Class(ErpAccessResult.class);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("query_type", String.valueOf(serviceType));
        request.get();
    }

    /**
     * 取消服务
     */
    public void cancelService() {
        HttpRequestV4 request = initRequest(RequestMethod.NODE_ERP_ROOM_SERVICE_RESPONSE);
        request.setConvert2Class(ErpAccessResult.class);
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("card", "");
        request.addParam("password", "");
        request.addParam("verify_mode", String.valueOf(0));
        request.get();
    }

    @Override
    public void onStart(String method) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        ErpAccessResult result = null;
        if (RequestMethod.NODE_ERP_ROOM_SERVICE.equalsIgnoreCase(method)) {
            if (object instanceof ErpAccessResult && (result = (ErpAccessResult) object).result != null && result.result.status == 1) {
                Logger.d(getClass().getSimpleName(), "呼叫服务成功 》》》》》》》》》》 " + result.result.info);
            } else {
                String err = "";
                if (result != null && result.result != null) {
                    err = result.result.info;
                }
                Logger.d(getClass().getSimpleName(), "呼叫服务失败 原因：" + err);
            }
        } else if (RequestMethod.NODE_ERP_ROOM_SERVICE_RESPONSE.equalsIgnoreCase(method)) {
            if (object instanceof ErpAccessResult && (result = (ErpAccessResult) object).result != null && result.result.status == 1) {
                Logger.d(getClass().getSimpleName(), "取消/响应 服务成功 》》》》》》》》》》 " + result.result.info);
            } else {
                String err = "";
                if (result != null && result.result != null) {
                    err = result.result.info;
                }
                Logger.d(getClass().getSimpleName(), "取消/响应 服务失败 原因：" + err);
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        if (RequestMethod.NODE_ERP_ROOM_SERVICE.equalsIgnoreCase(method)) {
            if (object instanceof BaseModelV4) {
                Logger.d(getClass().getSimpleName(), "onFailed 呼叫服务失败 原因： " + ((BaseModelV4) object).tips);
            } else {
                Logger.d(getClass().getSimpleName(), "onFailed 呼叫服务失败 原因： " + object.toString());
            }
        } else if (RequestMethod.NODE_ERP_ROOM_SERVICE_RESPONSE.equalsIgnoreCase(method)) {
            if (object instanceof BaseModelV4) {
                Logger.d(getClass().getSimpleName(), "onFailed 取消/响应 服务失败 原因：" + ((BaseModelV4) object).tips);
            } else {
                Logger.d(getClass().getSimpleName(), "onFailed 取消/响应 服务失败 原因：" + object.toString());
            }
        }
    }

    @Override
    public void onError(String method, String error) {
        if (RequestMethod.NODE_ERP_ROOM_SERVICE.equalsIgnoreCase(method)) {
            Logger.d(getClass().getSimpleName(), "onFailed 呼叫服务失败 原因： " + error);
        } else if (RequestMethod.NODE_ERP_ROOM_SERVICE_RESPONSE.equalsIgnoreCase(method)) {
            Logger.d(getClass().getSimpleName(), "onFailed 取消/响应 服务失败 原因：" + error);
        }
    }

    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext, method);
        request.setHttpRequestListener(this);
        return request;
    }

}
