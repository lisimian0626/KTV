package com.bestarmedia.libcommon.helper;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.config.OkConfig;
import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.data.VodConfigData;
import com.bestarmedia.libcommon.http.HttpRequestListener;
import com.bestarmedia.libcommon.http.HttpRequestV4;
import com.bestarmedia.libcommon.http.RequestMethod;
import com.bestarmedia.libcommon.interf.NodeRoomListener;
import com.bestarmedia.libcommon.model.vod.NodeRoom;
import com.bestarmedia.libcommon.model.vod.NodeRoomV4;
import com.bestarmedia.libcommon.model.vod.RoomV4;
import com.bestarmedia.libcommon.util.DeviceUtil;
import com.bestarmedia.libcommon.util.NetWorkUtils;
import com.bestarmedia.libcommon.util.PackageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J Wong on 2015/11/9 09:09.
 */
public class NodeRoomTimer implements HttpRequestListener {

    private final static String TAG = "NodeRoomTimer";

    private final static int INTERVAL = 30 * 1000;

    private final static int INTERVAL_HIGH = 10 * 1000;

    private static NodeRoomTimer mRomDetailTimer;

    private Context mContext;

    private int mRequestTimes;

    private Handler mHandlerTimer;

    private HandlerThread thread;

    private List<NodeRoomListener> mNodeRoomListener = new ArrayList<>();


    public static NodeRoomTimer getInstance(Context context) {
        if (mRomDetailTimer == null) {
            synchronized (NodeRoomTimer.class) {
                if (mRomDetailTimer == null) {
                    mRomDetailTimer = new NodeRoomTimer(context);
                }
            }
        }
        return mRomDetailTimer;
    }

    private NodeRoomTimer(Context context) {
        this.mContext = context;
    }

    public void addNodeRoomListener(NodeRoomListener listener) {
        mNodeRoomListener.add(listener);
    }

    public void removeNodeRoomListener(NodeRoomListener listener) {
        mNodeRoomListener.remove(listener);
    }

    public void startTimer() {
        mRequestTimes = 0;
        stopTimer();
        getTimerHandler().post(runnable);
    }

    public void stopTimer() {
        getTimerHandler().removeCallbacks(runnable);
    }

    private void requestNodeRoom() {
        if (!"B0".equals(VodConfigData.getInstance().getRoomCode())) {
            HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM_INFO);
            r.addParam("room_code", VodConfigData.getInstance().getRoomCode());
            r.setConvert2Class(RoomV4.class);
            r.get();
            mRequestTimes++;
        } else {
            requestRoom();
        }
    }

    private void requestRoom() {
        HttpRequestV4 r = initRequest(RequestMethod.NODE_ROOM);
        String myIp = NetWorkUtils.getLocalHostIp();
        r.addParam("ip", myIp);
        r.addParam("serial_no", DeviceUtil.getCupSerial());
        r.addParam("mac_serial", DeviceUtil.getMacAddress());
        r.addParam("anufacturer", OkConfig.boxManufacturerName());
        r.addParam("os_version", String.valueOf(PackageUtil.getSystemVersionCode()));
        r.addParam("apk_version_code", String.valueOf(PackageUtil.getVersionCode(mContext)));
        r.addParam("apk_version_name", PackageUtil.getVersionName(mContext));
        r.addParam("apk_describe", PackageUtil.getApplicationName(mContext));
        r.setConvert2Class(NodeRoom.class);
        r.get();
    }


    private HttpRequestV4 initRequest(String method) {
        HttpRequestV4 request = new HttpRequestV4(mContext, method);
        request.setHttpRequestListener(this);
        return request;
    }


    @Override
    public void onFailed(String method, Object obj) {
//        if (RequestMethod.NODE_ROOM.equals(method)) {
//
//        } else if (RequestMethod.NODE_ROOM_INFO.equals(method)) {
//
//        }
    }

    @Override
    public void onError(String method, String error) {

    }

    @Override
    public void onSuccess(String method, Object object) {
        if (RequestMethod.NODE_ROOM_INFO.equals(method)) {
            RoomV4 roomV4;
            if (object instanceof RoomV4 && (roomV4 = (RoomV4) object).room != null) {
                if (roomV4.room.room == null) {
                    Log.d(TAG, "onSuccess roomV4.room.room is null !!!!!!!");
                }
                boolean isFirstLoad = NodeRoomInfo.getInstance().getNodeRoom() == null;
                if (isFirstLoad) {//第一次加载
                    NodeRoomInfo.getInstance().setNodeRoom(roomV4.room);
                    callbackOnLoaded(NodeRoomInfo.getInstance().getNodeRoom());
                    callbackRoomSessionChanged(null, NodeRoomInfo.getInstance().getNodeRoom().room.currentSession);
                } else {//
                    NodeRoomV4 preNodeRoomV4 = NodeRoomInfo.getInstance().getNodeRoom();
                    NodeRoomInfo.getInstance().setNodeRoom(roomV4.room);
                    if (preNodeRoomV4.room.currentSession == null) {
                        preNodeRoomV4.room.currentSession = "";
                    }
                    if (roomV4.room.room.currentSession == null) {
                        roomV4.room.room.currentSession = "";
                    }
                    if (!preNodeRoomV4.room.currentSession.equalsIgnoreCase(roomV4.room.room.currentSession)) {
                        //回调开关房变化
                        callbackRoomSessionChanged(preNodeRoomV4.room.currentSession, NodeRoomInfo.getInstance().getNodeRoom().room.currentSession);
                    }
                }
                //回调房间信息
                callbackNodeRoomReturn(NodeRoomInfo.getInstance().getNodeRoom());
            }
        } else if (RequestMethod.NODE_ROOM.equals(method)) {
            NodeRoom nodeRoom;
            if (object instanceof NodeRoom && !TextUtils.isEmpty((nodeRoom = (NodeRoom) object).roomCode)) {
                NodeRoomInfo.getInstance().setRoomCode(nodeRoom.roomCode);
                requestNodeRoom();
            }
        }
    }

    private void callbackOnLoaded(NodeRoomV4 nodeRoomV4) {
        Log.d(TAG, "callbackOnLoaded  !!!!!!!");
        if (mNodeRoomListener != null && mNodeRoomListener.size() > 0) {
            for (NodeRoomListener listener : mNodeRoomListener) {
                listener.onNodeRoomLoaded(nodeRoomV4);
            }
        } else {
            Log.d(TAG, "NodeRoomListener Listener is empty !!!!!!!");
        }
    }

    private void callbackRoomSessionChanged(final String preSession, final String curSession) {
        Log.d(TAG, "callbackRoomStatusChanged  !!!!!!!");
        if (mNodeRoomListener != null && mNodeRoomListener.size() > 0) {
            for (NodeRoomListener listener : mNodeRoomListener) {
                listener.onRoomSessionChanged(preSession, curSession);
            }
        } else {
            Log.d(TAG, "callbackRoomStatusChanged Listener is empty !!!!!!!");
        }
    }

    private void callbackNodeRoomReturn(NodeRoomV4 nodeRoomV4) {
        if (mNodeRoomListener != null && mNodeRoomListener.size() > 0) {
            for (NodeRoomListener listener : mNodeRoomListener) {
                listener.onNodeRoomReturn(nodeRoomV4);
            }
        } else {
            Log.d(TAG, "callbackNodeRoomReturn Listener is empty !!!!!!!");
        }
    }

    @Override
    public void onStart(String method) {
    }

    private Handler getTimerHandler() {
        if (mHandlerTimer == null) {
            thread = new HandlerThread("NodeRoomThread");
            thread.start();
            mHandlerTimer = new MyHandler(thread.getLooper());
        }
        return mHandlerTimer;
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getTimerHandler().sendEmptyMessage(0);
            getTimerHandler().postDelayed(this, mRequestTimes < 20 ? INTERVAL_HIGH : INTERVAL);
        }
    };

    class MyHandler extends Handler {

        private MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            requestNodeRoom();
        }
    }
}
