package com.bestarmedia.libnetty.netty;

/**
 * Created by J Wong on 2019/5/28.
 */

public class CommonUtil {
    /**
     * 字符串首字母小写
     *
     * @param s string
     * @return string
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
