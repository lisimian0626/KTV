package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodBoxForPhoneQrCode;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.vod.RoomQrCodeSimple;
import com.bestarmedia.libcommon.model.vod.RoomV4;
import com.bestarmedia.libcommon.util.DeviceHelper;


/**
 * Created by J Wong on 2016/7/4.
 */
public class QrCodeRequest implements HttpRequestListener {

    private Context mContext;
    private QrCodeRequestListener mQrCodeRequestListener;

    public interface QrCodeRequestListener {

        void onQrCode(RoomQrCodeSimple qrCodeSimple);

        void onQrCodeFail(String error);
    }

    public QrCodeRequest(Context context, QrCodeRequestListener l) {
        mContext = context;
        mQrCodeRequestListener = l;
    }

    //上传二维码信息
    public void requestCode() {
        if (DeviceHelper.isMainVod(mContext)) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_QR_CODE);
            if (r != null && !TextUtils.isEmpty(VodBoxForPhoneQrCode.getVodBoxUUID())
                    && !TextUtils.isEmpty(VodConfigData.getInstance().getRoomCode())
                    && !TextUtils.isEmpty(NodeRoomInfo.getInstance().getRoomSession())) {
                r.addParam("vod_token", VodBoxForPhoneQrCode.getVodBoxUUID());
                r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
                r.addParam("current_session", NodeRoomInfo.getInstance().getRoomSession());
                r.post();
            } else {
                if (mQrCodeRequestListener != null) {
                    if (TextUtils.isEmpty(NodeRoomInfo.getInstance().getRoomSession()))
                        mQrCodeRequestListener.onQrCodeFail("未开房！");
                }
            }
        } else {//副屏获取房间信息
            requestNodeRoom();
        }
    }

    //回调二维码信息
    private void callbackQrCode() {
        if (mQrCodeRequestListener != null && !TextUtils.isEmpty(NodeRoomInfo.getInstance().getPhoneSession())) {
            mQrCodeRequestListener.onQrCode(new RoomQrCodeSimple(NodeRoomInfo.getInstance().getPhoneSession(),
                    VodConfigData.getInstance().getRoomCode(), VodConfigData.getInstance().getKtvNetCode()));
        }
    }

    //查询房间信息
    private void requestNodeRoom() {
        if (!"B0".equals(VodConfigData.getInstance().getRoomCode())) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_INFO);
            r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
            r.setConvert2Class(RoomV4.class);
            r.get();
        }
    }

    public HttpRequestV4 initRequest(String method) {
        try {
            HttpRequestV4 request = new HttpRequestV4(mContext, method);
            request.setHttpRequestListener(this);
            return request;
        } catch (Exception e) {
            Log.w(getClass().getSimpleName(), "初始化http异常", e);
        }
        return null;
    }

    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_QR_CODE.equals(method)) {//上传成功
            requestNodeRoom();
        } else if (RequestMethod.NODE_ROOM_INFO.equals(method)) {//查询房间信息成功
            RoomV4 roomV4;
            if (object instanceof RoomV4 && (roomV4 = (RoomV4) object).room != null) {
                NodeRoomInfo.getInstance().setNodeRoom(roomV4.room);
                callbackQrCode();
            } else {
                if (mQrCodeRequestListener != null) {
                    mQrCodeRequestListener.onQrCodeFail("返回数据异常！");
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object obj) {
        if (mQrCodeRequestListener != null) {
            if (obj instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) obj;
                mQrCodeRequestListener.onQrCodeFail(baseModelV4.tips);
            } else if (obj instanceof String) {
                mQrCodeRequestListener.onQrCodeFail(obj.toString());
            }

        }
    }

    @Override
    public void onError(String method, String error) {

    }
}
