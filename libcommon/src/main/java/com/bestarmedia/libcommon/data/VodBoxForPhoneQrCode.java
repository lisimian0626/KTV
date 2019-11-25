package com.bestarmedia.libcommon.data;

import android.text.TextUtils;
import android.util.Log;

import com.bestarmedia.libcommon.util.UUIDUtil;

/**
 * Created by J Wong on 2018/2/5.
 */

public class VodBoxForPhoneQrCode {

    private static String VOD_BOX_UUID = null;

    public static String getVodBoxUUID() {
        if (TextUtils.isEmpty(VOD_BOX_UUID)) {
            VOD_BOX_UUID = UUIDUtil.getRandomUUID();
            Log.d("QrCodeRequest", "create box uuid :" + VOD_BOX_UUID);
        }
        return VOD_BOX_UUID;
    }

    public static void resetVodBoxUUID() {
        VOD_BOX_UUID = null;
    }
}
