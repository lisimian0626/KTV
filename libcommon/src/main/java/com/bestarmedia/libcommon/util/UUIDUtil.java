package com.bestarmedia.libcommon.util;

import java.util.UUID;

/**
 * Created by J Wong on 2016/8/1.
 */
public class UUIDUtil {


    public static String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

}
