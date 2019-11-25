package com.bestarmedia.libcommon.helper;


import com.bestarmedia.libcommon.data.NodeRoomInfo;
import com.bestarmedia.libcommon.interf.ControlBoxListener;

/**
 * Created by J Wong on 2016/4/12.
 */
public class ControlBoxGetter {

    private ControlBoxListener mControlBoxListener;

    public ControlBoxGetter(ControlBoxListener listener) {
        mControlBoxListener = listener;
    }

    public void getControlBoxInfo() {
        if (NodeRoomInfo.getInstance().getControlBox() != null) {
            if (mControlBoxListener != null) {
                mControlBoxListener.onControlBox(NodeRoomInfo.getInstance().getControlBox());
            }
        } else {
            if (mControlBoxListener != null) {
                mControlBoxListener.onControlBox(null);
            }
        }
    }
}
