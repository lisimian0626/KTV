package com.bestarmedia.libcommon.ad;

import android.content.Context;

import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.model.ad.ADModel;
import com.bestarmedia.libcommon.model.vod.AdRecordRequestBody;
import com.bestarmedia.libcommon.util.DeviceUtil;

/**
 * Created by J Wong on 2015/12/27 09:24.
 */
public class AdPlayRecorder implements HttpRequestListener {

    private Context mContext;

    public AdPlayRecorder(Context context) {
        this.mContext = context;
    }

    public void recordAdPlay(ADModel ad) {
        if (ad.getId() > 0) {
            recordAdPlay(ad, ad.getAdPosition());
        }
    }

    private void recordAdPlay(ADModel ad, String adPos) {
        if (ad.getId() > 0) {
            recordAdPlay(ad.getId(), DeviceUtil.getDeviceTypeByAdPos(adPos), ad.getName(), ad.getAdTypeId());
        }
    }

    /**
     * @param adId       ID
     * @param deviceType 设备类型，1为电视屏，2为点歌屏，3为墙面板，4为门口屏，5为大堂电视
     * @param adName     广告名称
     * @param adTypeId   类型id
     */
    public void recordAdPlay(int adId, int deviceType, String adName, int adTypeId) {
        if (NodeRoomInfo.getInstance().getNodeRoom() != null && adId > 0) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_AD_RECORD);
            AdRecordRequestBody body = new AdRecordRequestBody();
            body.id = String.valueOf(adId);
            body.name = adName;
            body.adTypeId = adTypeId;
            body.deviceType = deviceType;
            body.playDeviceId = DeviceUtil.getCupSerial();
            body.playDeviceMac = DeviceUtil.getMacAddress();
            body.playRoomName = NodeRoomInfo.getInstance().getRoomName();
            body.roomCode = VodConfigData.getInstance().getRoomCode();
            r.postJson(body.toString());
        }
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

    }

    @Override
    public void onFailed(String method, Object obj) {

    }

    @Override
    public void onError(String method, String error) {

    }
}
