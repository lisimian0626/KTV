package com.bestarmedia.libcommon.config;

import android.Manifest;
import android.provider.Settings;

public class KtvPermissionConfig {

    public static String[] ktvPermissions = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.READ_PHONE_STATE,
            "com.android.example.USB_PERMISSION",
            "android.hardware.usb.action.USB_DEVICE_ATTACHED",
            "android.hardware.usb.action.USB_DEVICE_DETACHED"
    };
}
