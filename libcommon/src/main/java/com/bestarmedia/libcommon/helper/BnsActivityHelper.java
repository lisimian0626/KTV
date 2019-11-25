package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.util.Log;

import com.bestarmedia.libcommon.data.BnsActivityCache;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.eventbus.EventBusUtil;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.activity.BnsActivity;

import static com.bestarmedia.libcommon.eventbus.EventBusId.Id.BNS_ACTIVITY_CHANGED;


/**
 * Created by J Wong on 2019/11/22.
 * 获取活动列表
 */
public class BnsActivityHelper implements HttpRequestListener {

    private Context mContext;

    public BnsActivityHelper(Context context) {
        this.mContext = context;
    }


    public void check() {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.CLOUD.BNS_ACTIVITY);
        request.setApiPath("http://192.168.1.99:5000");
        request.addParam("ktvnetcode", VodConfigData.getInstance().getKtvNetCode());
        request.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        request.addParam("room_session", NodeRoomInfo.getInstance().getRoomSession());
        request.setHttpRequestListener(this);
        request.setConvert2Class(BnsActivity.class);
        request.get();
    }


    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.CLOUD.BNS_ACTIVITY.equalsIgnoreCase(method)) {
            if (object instanceof BnsActivity) {
                BnsActivityCache.getInstance().setBnsActivity((BnsActivity) object);
            } else {
                Log.d(getClass().getSimpleName(), "当前无活动 >>>>>>>>>>>>>>>>> ");
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {

    }

    @Override
    public void onError(String method, String error) {

    }
}
