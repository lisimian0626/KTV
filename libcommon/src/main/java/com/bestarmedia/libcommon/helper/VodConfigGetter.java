package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.vod.ProfileDetail;
import com.bestarmedia.libcommon.model.vod.ProfileDetailV4;
import com.bestarmedia.libcommon.util.Logger;


/**
 * Created by J Wong on 2017/5/15.
 */

public class VodConfigGetter implements HttpRequestListener {

    private static VodConfigGetter mServerConfigHelper;
    private Context mContext;
    private OnServerCallback mOnServerCallback;
    private Handler handler = new Handler();
    private final static long INTERVAL = 5 * 1000;

    public static VodConfigGetter getInstance(Context context) {
        if (mServerConfigHelper == null) {
            mServerConfigHelper = new VodConfigGetter(context);
        }
        return mServerConfigHelper;
    }

    public void setOnServerCallback(OnServerCallback callback) {
        this.mOnServerCallback = callback;
    }

    public void getConfig() {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.NODE_PROFILE);
        request.setHttpRequestListener(this);
        request.setConvert2Class(ProfileDetailV4.class);
        request.get();
    }

    private VodConfigGetter(Context context) {
        this.mContext = context;
    }


    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        Logger.d("VodConfigGetter", "onSuccess ");
        try {
            ProfileDetailV4 profileDetailV4;
            if (object != null && object instanceof ProfileDetailV4 && (profileDetailV4 = (ProfileDetailV4) object) != null
                    && profileDetailV4.nodeProfileDetail != null && profileDetailV4.nodeProfileDetail.configuration != null) {
                VodConfigData.getInstance().setConfigData(profileDetailV4.nodeProfileDetail.configuration);
                VodConfigData.getInstance().setProfileDetailV4(profileDetailV4);
                if (!TextUtils.isEmpty(profileDetailV4.nodeProfileDetail.configuration.password)) {
                    PrefData.setMngPassword(mContext, profileDetailV4.nodeProfileDetail.configuration.password);
                } else {
                    PrefData.setMngPassword(mContext, "666666");
                }
                if (mOnServerCallback != null) {
                    mOnServerCallback.onSererCallback(profileDetailV4.nodeProfileDetail);
                }
            } else {
                Logger.d("VodConfigGetter", "onSuccess VodConfig config = null ");
                Toast.makeText(mContext, "连接服务返回数据错误！", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getConfig();
                    }
                }, INTERVAL);
            }
        } catch (Exception e) {
            Logger.d("VodConfigGetter", "onSuccess VodConfig Object exception");
            Toast.makeText(mContext, "连接服务返回异常！", Toast.LENGTH_SHORT).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getConfig();
                }
            }, INTERVAL);
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (obj instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) obj;
            Logger.d("VodConfigGetter", "onFailed error:" + baseModelV4.tips);
            Toast.makeText(mContext, baseModelV4.tips, Toast.LENGTH_SHORT).show();
        } else if (obj instanceof String) {
            Logger.d("VodConfigGetter", "onFailed error:" + obj.toString());
            Toast.makeText(mContext, obj.toString(), Toast.LENGTH_SHORT).show();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getConfig();
            }
        }, INTERVAL);
    }

    @Override
    public void onError(String method, String error) {

    }

    public interface OnServerCallback {
        void onSererCallback(ProfileDetail profileDetail);
    }
}
