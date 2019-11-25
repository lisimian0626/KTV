package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.SafetyListener;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.vod.login.RequestSuccess;
import com.bestarmedia.libcommon.model.vod.safety.RoomSafetyPage;
import com.bestarmedia.libcommon.model.vod.safety.SafetyUpload;
import com.bestarmedia.libcommon.model.vod.safety.StoreSafetyInfos;
import com.bestarmedia.libcommon.util.Logger;


/**
 * Created by J Wong on 2017/5/15.
 */
public class SafetyHelper implements HttpRequestListener {
    public static String Tag = SafetyHelper.class.getSimpleName();
    private static SafetyHelper mSafetyHelper;
    private Context mContext;
    private SafetyListener mSafetyListener;

    public static SafetyHelper getInstance(Context context) {
        if (mSafetyHelper == null) {
            mSafetyHelper = new SafetyHelper(context);
        }
        return mSafetyHelper;
    }

    public void setSafetyListener(SafetyListener safetyListener) {
        this.mSafetyListener = safetyListener;
    }


    public void getRoomSatety(String ket_net_code, String roomCode) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.ROOM_EQUIPMENT_SAFETY_INFO);
        request.addParam("ktv_net_code", ket_net_code);
        request.addParam("room_code", roomCode);
        request.setHttpRequestListener(this);
        request.setConvert2Class(RoomSafetyPage.class);
        request.get();
    }

    public void postRoomSatety(SafetyUpload safetyUpload) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.ROOM_EQUIPMENT_UPLOAD);
        request.setHttpRequestListener(this);
        request.setConvert2Class(RequestSuccess.class);
        request.postJson(safetyUpload.toString());
    }


    public void getStoreSatety() {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.STORE_EQUIPMENT);
        request.setHttpRequestListener(this);
        request.setConvert2Class(StoreSafetyInfos.class);
        request.get();
    }

    public SafetyHelper(Context context) {
        this.mContext = context;
    }


    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.V4.ROOM_EQUIPMENT_SAFETY_INFO.equalsIgnoreCase(method)) {
            if (object instanceof RoomSafetyPage) {
                RoomSafetyPage roomSafetyPage = (RoomSafetyPage) object;
                if (roomSafetyPage.roomSafetyList != null && roomSafetyPage.roomSafetyList.data != null && roomSafetyPage.roomSafetyList.data.size() > 0) {
                    getStoreSatety();
                } else {
                    if (mSafetyListener != null) {
                        mSafetyListener.onRoomSafeFail();
                    }
                }
            } else if (object instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) object;
                Toast.makeText(mContext, baseModelV4.tips, Toast.LENGTH_SHORT).show();
            }
        } else if (RequestMethod.V4.STORE_EQUIPMENT.equalsIgnoreCase(method)) {
            if (object != null && object instanceof StoreSafetyInfos) {
                StoreSafetyInfos storeSafetyInfos = (StoreSafetyInfos) object;
                if (storeSafetyInfos.safetyInfo != null && !TextUtils.isEmpty(storeSafetyInfos.safetyInfo.checkAt)) {
                    VodConfigData.getInstance().setSafetyPass(true);
                    if (mSafetyListener != null) {
                        mSafetyListener.onSafeSucceed();
                    }
                } else {
                    VodConfigData.getInstance().setSafetyPass(false);
                    if (mSafetyListener != null) {
                        mSafetyListener.onStoreSafeFail();
                    }
                }
            }

        } else if (RequestMethod.V4.ROOM_EQUIPMENT_UPLOAD.equalsIgnoreCase(method)) {
            Logger.d(Tag, "房间安全检查上报成功");
            if (mSafetyListener != null){
                mSafetyListener.onSafeUploadSucceed(true);
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        Log.e(Tag, object.toString());
        if(RequestMethod.V4.ROOM_EQUIPMENT_UPLOAD.equalsIgnoreCase(method)){
            if (mSafetyListener != null){
                mSafetyListener.onSafeUploadSucceed(false);
            }
        }
        if (object instanceof BaseModelV4) {
            BaseModelV4 baseModelV4 = (BaseModelV4) object;
            Toast.makeText(mContext, baseModelV4.tips, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String method, String error) {
        if(RequestMethod.V4.ROOM_EQUIPMENT_UPLOAD.equalsIgnoreCase(method)){
            if (mSafetyListener != null){
                mSafetyListener.onSafeUploadSucceed(false);
            }
        }
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

}
