package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.widget.Toast;

import com.bestarmedia.libcommon.data.DeviceInfoData;
import com.bestarmedia.libcommon.data.PrefData;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.BaseHttpRequest;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.JWTUtil;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.CheckDeviceListener;
import com.bestarmedia.libcommon.model.BaseModelV4;
import com.bestarmedia.libcommon.model.node.NodeAuthor;
import com.bestarmedia.libcommon.model.v4.DeviceType;
import com.bestarmedia.libcommon.model.v4.DeviceTypeV4;
import com.bestarmedia.libcommon.model.v4.JWTDTO;
import com.bestarmedia.libcommon.model.v4.NodeRoomV4;
import com.bestarmedia.libcommon.model.vod.GetToken;
import com.bestarmedia.libcommon.model.vod.RegisterDevice;
import com.bestarmedia.libcommon.model.vod.login.DevcieV4;
import com.bestarmedia.libcommon.model.vod.login.RequestSuccess;
import com.bestarmedia.libcommon.util.Logger;

import java.util.List;


/**
 * Created by J Wong on 2017/5/15.
 */
public class LoginHelper implements HttpRequestListener {
    private String Tag = LoginHelper.class.getSimpleName();
    private static LoginHelper mLoginHelper;
    private Context mContext;
    private CheckDeviceListener checkDeviceListener;

    public static LoginHelper getInstance(Context context) {
        if (mLoginHelper == null) {
            mLoginHelper = new LoginHelper(context);
        }
        return mLoginHelper;
    }

    private LoginHelper(Context context) {
        this.mContext = context;
    }

    public void setCheckDeviceListener(CheckDeviceListener checkDeviceListener) {
        this.checkDeviceListener = checkDeviceListener;
    }

    public void getAuthorInfo() {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.NODE_AUTHORIZATION);
        request.setConvert2Class(NodeAuthor.class);
        request.setHttpRequestListener(this);
        request.get();
    }

    public void getRoomInfo(String roomCode) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.V4_NODE_ROOM);
        request.addParam("room_code", roomCode);
        request.setConvert2Class(NodeRoomV4.class);
        request.setHttpRequestListener(this);
        request.get();
    }

    public void getDeviceType() {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.V4_NODE_ROOM_DEVICE_TYPE);
        request.setHttpRequestListener(this);
        request.setConvert2Class(DeviceTypeV4.class);
        request.get();
    }

    public void getDeviceInfo(String serialNum) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.V4_NODE_ROOM_DEVICE);
        request.addParam("serial_no", serialNum);
        request.setHttpRequestListener(this);
        request.setConvert2Class(DevcieV4.class);
        request.get();
    }

    //设备注册
    public void register(RegisterDevice registerDevice) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.V4_NODE_ROOM_DEVICE);
        request.setHttpRequestListener(this);
        request.setConvert2Class(RequestSuccess.class);
        request.postJson(registerDevice.toString());
    }

    public void getToken(String ktvNetCode, String deviceSn) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.V4_NODE_AUTH_TOKEN);
        GetToken getToken = DeviceInfoData.getInstance().getDeviceInfo().toGetToken(ktvNetCode, deviceSn);
        request.setHttpRequestListener(this);
        request.setConvert2Class(JWTDTO.class);
        request.postJson(getToken.toString());
    }

    //设备注销
    public void logout(String devcieId) {
        HttpRequestV4 request = new HttpRequestV4(mContext, RequestMethod.V4.NODE_ROOM_DEVICE + "/" + devcieId);
        request.setHttpRequestListener(this);
        request.delete();
    }


    @Override
    public void onStart(String method) {
    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.V4.V4_NODE_ROOM_DEVICE.equalsIgnoreCase(method)) {
            Logger.d(Tag, "onSuccess" + "    获取设备信息");
            if (object instanceof DevcieV4) {
                DevcieV4 devcieV4 = (DevcieV4) object;
                DeviceInfoData.getInstance().setDeviceInfo(devcieV4.room_device);
                if (devcieV4.room_device != null) {
                    if (checkDeviceListener != null) {
                        checkDeviceListener.onDeviceInfoSucced(devcieV4.room_device);
                    }
                }
            } else if (object instanceof RequestSuccess) {
                Logger.d(Tag, "onSuccess" + "    (登录)注册成功");
                if (checkDeviceListener != null) {
                    checkDeviceListener.onRegister(true, "");
                }
            }
        } else if (RequestMethod.V4.V4_NODE_AUTH_TOKEN.equalsIgnoreCase(method)) {
            Logger.d(Tag, "onSuccess" + "    获取 token");
            JWTDTO jwtdto = (JWTDTO) object;
            if (jwtdto != null && jwtdto.jwt != null) {
                PrefData.setJWT(mContext, jwtdto.jwt);
                VodConfigData.getInstance().setJwtMessage(JWTUtil.jwt(jwtdto.jwt));
                BaseHttpRequest.setToken(jwtdto.jwt);
                if (checkDeviceListener != null) {
                    checkDeviceListener.onGetJwt(jwtdto.jwt);
                }
            }
        } else if (RequestMethod.V4.V4_NODE_ROOM_DEVICE_TYPE.equalsIgnoreCase(method)) {
            DeviceTypeV4 deviceTypeV4 = (DeviceTypeV4) object;
            if (deviceTypeV4 != null && deviceTypeV4.deviceTypeList != null && deviceTypeV4.deviceTypeList.size() > 0) {
                List<DeviceType> deviceTypeList = deviceTypeV4.deviceTypeList;
                Logger.d(Tag, "onSuccess" + "    获取设备类型:" + deviceTypeList.toString());
                if (checkDeviceListener != null) {
                    checkDeviceListener.onDeviceType(deviceTypeList);
                }
            }
        } else if (RequestMethod.V4.V4_NODE_ROOM.equalsIgnoreCase(method)) {
            Logger.d(Tag, "onSuccess" + "    获取房间设备:");
            NodeRoomV4 nodeRoomV4 = (NodeRoomV4) object;
            if (nodeRoomV4 != null && nodeRoomV4.node_room != null) {
                if (checkDeviceListener != null) {
                    checkDeviceListener.onNodeRoom(nodeRoomV4.node_room);
                }
            }
        } else if ((RequestMethod.V4.NODE_ROOM_DEVICE + "/" + DeviceInfoData.getInstance().getLocalId()).equalsIgnoreCase(method)) {
            Logger.d(Tag, "onSuccess" + "    注销设备:");
            if (checkDeviceListener != null) {
                checkDeviceListener.onLogout(true, "");
            }
        } else if (RequestMethod.NODE_AUTHORIZATION.equalsIgnoreCase(method)) {
            Logger.d(Tag, "onSuccess" + "    获取店家授权信息:");
            if (object instanceof NodeAuthor) {
                NodeAuthor nodeAuthor = (NodeAuthor) object;
                if (nodeAuthor.author != null) {
                    if (checkDeviceListener != null) {
                        checkDeviceListener.onAuthor(nodeAuthor.author);
                    }
                }
            }
        }
    }

    @Override
    public void onFailed(String method, Object object) {
        //设备注销 (注销失败)
        if ((RequestMethod.V4.NODE_ROOM_DEVICE + "/" + DeviceInfoData.getInstance().getLocalId()).equalsIgnoreCase(method)) {
            if (object instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) object;
                Logger.d(Tag, "onFailed error:" + "code |" + baseModelV4.code + "   tips  |" + baseModelV4.tips);
                Toast.makeText(mContext, baseModelV4.tips, Toast.LENGTH_SHORT).show();
            } else if (object instanceof String) {
                Logger.d(Tag, "onFailed error:" + object.toString());
                Toast.makeText(mContext, object.toString(), Toast.LENGTH_SHORT).show();
            }//  获取设备信息
        } else if (RequestMethod.V4.V4_NODE_ROOM_DEVICE.equalsIgnoreCase(method)) {
            if (object instanceof BaseModelV4) {
                BaseModelV4 baseModelV4 = (BaseModelV4) object;
                Logger.d(Tag, "onFailed error:" + "code |" + baseModelV4.code + "   tips  |" + baseModelV4.tips);
                if (baseModelV4.code == 20211011) {
                    if (checkDeviceListener != null) {
                        checkDeviceListener.onDeviceInfoFail(baseModelV4.tips);
                    }
                }
                Toast.makeText(mContext, baseModelV4.tips, Toast.LENGTH_SHORT).show();
            } else if (object instanceof String) {
                Logger.d(Tag, "onFailed error:" + object.toString());
                if (checkDeviceListener != null) {
                    checkDeviceListener.onDeviceInfoFail(object.toString());
                }
            }
        }
    }

    @Override
    public void onError(String method, String error) {

    }

}
