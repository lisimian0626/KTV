package com.bestarmedia.libcommon.util;

import android.content.Context;

import com.bestarmedia.libcommon.data.DeviceInfoData;
import com.bestarmedia.libcommon.model.v4.RoomDevice;

import java.util.List;

/**
 * Created by J Wong on 2016/2/1 16:49.
 */
public class DeviceHelper {

//    /**
//     * 获取主屏ip
//     *
//     * @return
//     */
//    public static String getMyDeviceId(Context context) {
//        List<RoomDevice> devices = DeviceInfoData.getInstance().getNodeRoom().roomDeviceList;
//        for (RoomDevice device : devices) {
//            if (DeviceUtil.getH8CupChipID(context).equalsIgnoreCase(device.serialNo)) {
//                return device.id;
//            }
//        }
//        return "";
//    }


    /**
     * @return 获取主屏ip
     */
    public static String getMainVodIp() {
        if (DeviceInfoData.getInstance().getNodeRoom() != null && DeviceInfoData.getInstance().getNodeRoom().roomDeviceList != null) {
            List<RoomDevice> devices = DeviceInfoData.getInstance().getNodeRoom().roomDeviceList;
            for (RoomDevice device : devices) {
                if (device.type == 1) {
                    return device.ip;
                }
            }
        }
        return "";
    }

    /**
     * @return 是否主屏
     */
    public static boolean isMainVod(Context context) {
        if (DeviceInfoData.getInstance().getNodeRoom() != null && DeviceInfoData.getInstance().getNodeRoom().roomDeviceList != null) {
            List<RoomDevice> devices = DeviceInfoData.getInstance().getNodeRoom().roomDeviceList;
            for (RoomDevice device : devices) {
                if (DeviceUtil.getCupSerial().equalsIgnoreCase(device.serialNo)) {
                    return device.type == 1;
                }
            }
        }
        return true;
    }

//    /**
//     * 1,8 结尾
//     *
//     * @return
//     */
//    public String[] getSecondaryVodIp() {
//        String mIp = NetWorkUtils.getLocalHostIp();
//        if (!TextUtils.isEmpty(mIp)) {
//            String[] secIps = new String[2];
//            String secIp1 = mIp.substring(0, mIp.length() - 1) + "1";
//            String secIp2 = mIp.substring(0, mIp.length() - 1) + "8";
//            secIps[0] = secIp1;
//            secIps[1] = secIp2;
//            return secIps;
//        }
//        return null;
//    }
//
//
//    /**
//     * 3 ,4, 5, 6,7 结尾
//     *
//     * @return
//     */
//    public String[] getWallIps() {
//        String mIp = NetWorkUtils.getLocalHostIp();
//        if (!TextUtils.isEmpty(mIp)) {
//            String mainIp = mIp.substring(0, mIp.length() - 1);
//            String[] wallIps = new String[5];
//            for (int i = 0; i < wallIps.length; i++) {
//                wallIps[i] = mainIp + (i + 3);
//            }
//            return wallIps;
//        }
//        return null;
//    }
//
//    public boolean isWallIp() {
//        String mIp = NetWorkUtils.getLocalHostIp();
//        return mIp != null && (mIp.endsWith("3") || mIp.endsWith("4") || mIp.endsWith("5") || mIp.endsWith("6") || mIp.endsWith("7"));
//    }
//
//    public String getProjectorIp() {
//        String mIp = NetWorkUtils.getLocalHostIp();
//        if (!TextUtils.isEmpty(mIp)) {
//            return mIp.substring(0, mIp.length() - 1) + "9";
//        }
//        return "";
//    }
}
