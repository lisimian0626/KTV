package com.bestarmedia.libcommon.util;

import com.bestarmedia.libcommon.config.OkConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by J Wong on 2017/5/15.
 */

public class UsbFileUtil {

    //硬盘根目录
    private final static String USB_PATH = "/mnt/usb_storage/USB_DISK0/udisk0/";

    private final static String USB_PATH_901 = "/mnt/usb_storage/USB_DISK0/C/";

    private final static String USB_PATH_H6 = "/mnt/usbhost/Storage01/";

    private final static String STORE_FILE_NAME = "BNS_KBox";

    public static boolean isUsbExitBoxCode() {
        File file = new File(OkConfig.boxManufacturer() == 1 ? USB_PATH_901 :
                OkConfig.boxManufacturer() == 2 ? USB_PATH_H6 : USB_PATH, STORE_FILE_NAME);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public static String readKBoxCode() {
        BufferedReader br = null;
        String ret = "";
        try {
            StringBuilder sb = new StringBuilder();
            String s = "";
            File file = new File(OkConfig.boxManufacturer() == 1 ? USB_PATH_901 :
                    OkConfig.boxManufacturer() == 2 ? USB_PATH_H6 : USB_PATH, STORE_FILE_NAME);
            br = new BufferedReader(new FileReader(file));
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            ret = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
        return ret;
    }

}
