package com.beidousat.karaoke.helper;

import android.content.Context;
import android.util.Log;

import com.beidousat.karaoke.service.UsbService;
import com.bestarmedia.libcommon.util.DeviceHelper;
import com.felhr.utils.HexData;

/**
 * Created by J Wong on 2017/9/22.
 */

public class BnsBeaconHelper {

    private final static String TAG = "BnsBeacon";
    private final static String BEACON_DATA_HEAD = "4F 45 6F 65";
    private final static String BEACON_DATA_SESSION = "11 22";
    private final static String BEACON_DATA_CMD_ID = "06";
    private UsbService usbService;

    private static boolean mHaveBeacon = false;

    public static void setHaveBeacon(Context context, boolean haveBeacon) {
        if (DeviceHelper.isMainVod(context)) {
            sendBeaconStatus2Sec(haveBeacon);
        }
        mHaveBeacon = haveBeacon;
    }

    public static boolean haveBeacon() {
        return mHaveBeacon;
    }

    private static void sendBeaconStatus2Sec(boolean enable) {
//        SocketOperationUtil.sendBeacon2Sec(enable ? 1 : 0);
    }

    public BnsBeaconHelper(UsbService usbService) {
        this.usbService = usbService;
    }

    private void sendText(String data) {
        Log.d(TAG, "sendText :" + data);
        if (usbService != null) { // if UsbService was correctly binded, Send data
            String[] arrayOfString = data.split(" ");
            try {
                byte[] arrayOfByte1 = new byte[arrayOfString.length];
                for (int i = 0; i < arrayOfString.length; i++) {
                    arrayOfByte1[i] = HexData.stringTobytes(arrayOfString[i])[0];
                }
                usbService.write(arrayOfByte1);
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
    }

    public void setBeaconInfo(String uuid, String major, String minor) {
        Log.d(TAG, "setBeaconInfo uuid:" + uuid + " major:" + major + " minor:" + minor);
        try {
            String hexMajor = Integer.toHexString(Integer.valueOf(major));
            String hexMinor = Integer.toHexString(Integer.valueOf(minor));
            String data = (BEACON_DATA_HEAD + BEACON_DATA_SESSION + BEACON_DATA_CMD_ID + "00800000"
                    + uuid + hexMajor + hexMinor + "C5").replace(" ", "").replace("-", "");
            sendText(convertHex(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String convertHex(String input) {
        String regex = "(.{2})";
        input = input.replaceAll(regex, "$1 ");
        return input;
    }
}
