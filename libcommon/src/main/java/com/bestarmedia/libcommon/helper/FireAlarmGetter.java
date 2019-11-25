package com.bestarmedia.libcommon.helper;

import android.content.Context;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.vod.service.FireAlarmInfo;
import com.bestarmedia.libcommon.model.vod.service.FireAlarmV4;


/**
 * Created by J Wong on 2017/1/13.
 */

public class FireAlarmGetter implements HttpRequestListener {

    private Context mContext;
    private OnFireAlarmListener mOnFireAlarmListener;

    public FireAlarmGetter(Context context, OnFireAlarmListener listener) {
        this.mContext = context;
        mOnFireAlarmListener = listener;
    }

    public void requestFireAlarm() {
        HttpRequestV4 requestV4 = initRequestV4(RequestMethod.NODE_FIRE_ALARM);
        requestV4.addParam("room_code", VodConfigData.getInstance().getRoomCode());
        requestV4.setConvert2Class(FireAlarmV4.class);
        requestV4.get();
    }


    public HttpRequestV4 initRequestV4(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext, method);
        request.setHttpRequestListener(this);
        return request;
    }

    @Override
    public void onFailed(String method, Object object) {
        if (mOnFireAlarmListener != null)
            mOnFireAlarmListener.onFireAlarmFail();
    }

    @Override
    public void onError(String method, String error) {
        if (mOnFireAlarmListener != null)
            mOnFireAlarmListener.onFireAlarmFail();
    }


    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_FIRE_ALARM.equals(method)) {
            FireAlarmV4 info = (FireAlarmV4) object;
            if (info != null) {
                if (mOnFireAlarmListener != null)
                    mOnFireAlarmListener.onFireAlarmSuccess(info.fire);
            } else {
                if (mOnFireAlarmListener != null)
                    mOnFireAlarmListener.onFireAlarmFail();
            }
        }
    }

    @Override
    public void onStart(String method) {

    }


    public interface OnFireAlarmListener {
        void onFireAlarmSuccess(FireAlarmInfo info);

        void onFireAlarmFail();
    }
}
