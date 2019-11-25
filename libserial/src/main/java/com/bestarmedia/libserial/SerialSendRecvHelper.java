package com.bestarmedia.libserial;

import android.util.Log;

/**
 * Created by J Wong on 2016/10/26.
 */

public class SerialSendRecvHelper {

    private StringBuffer data = new StringBuffer();

    private SerialHelper mSerialHelper;

    private static SerialSendRecvHelper mSerialSendRecvHelper;

    public static SerialSendRecvHelper getInstance() {
        if (mSerialSendRecvHelper == null) {
            mSerialSendRecvHelper = new SerialSendRecvHelper();
        }
        return mSerialSendRecvHelper;
    }

    private void open(int baudRate) {
        open("/dev/ttyS3", baudRate);
    }

    public void open(String path, int baudRate) {
        mSerialHelper = new SerialHelper(path, baudRate) {
            @Override
            protected void onReceive(final byte[] btData) {
                data = new StringBuffer();
                Log.i("SerialSendRecvHelper", "receive byte[] :" + new String(btData));
                // int i=0;
                for (byte b : btData) {
                    data.append(DataTransition.byte2Hex(b) + " ");
                }
                Log.i("SerialSendRecvHelper", "receive data to string :" + data.toString());
                if (mOnSerialReceiveListener != null && data.length() > 0) {
                    String code = data.toString();
                    try {
                        code = code.trim();
                        Log.i("SerialSendRecvHelper", "receive data code :" + code);
                    } catch (Exception e) {
                        Log.i("SerialSendRecvHelper", "ex:" + data.toString());
                    }
                    mOnSerialReceiveListener.OnSerialReceive(code);
                }
            }
        };
        try {
            mSerialHelper.open();
        } catch (Exception ex) {
            Log.e("SerialSendRecvHelper", ex.toString());
        }
    }

    private OnSerialReceiveListener mOnSerialReceiveListener;

    public void setOnSerialReceiveListener(OnSerialReceiveListener listener) {
        mOnSerialReceiveListener = listener;
    }

    public void send(String code) {
        try {
            Log.d("SerialSendRecvHelper", "send :" + code.replace(" ", ""));
            mSerialHelper.sendHex(code.replace(" ", ""));
        } catch (Exception ex) {
            Log.e("SerialSendRecvHelper", "send ex:" + ex.toString());
        }
    }

    public interface OnSerialReceiveListener {
        void OnSerialReceive(String data);
    }

}
