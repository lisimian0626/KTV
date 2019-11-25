package com.bestarmedia.libcommon.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by J Wong on 2017/12/21.
 */

public class MatcherUtil {
    /**
     * 正则匹配获取两个字符之间的内容
     *
     * @param src
     * @param first
     * @param second
     * @return
     */
    public static String findString(String src, String first, String second) {
        String target = "";
        if (!TextUtils.isEmpty(src)) {
            String regex = first + "(.*)" + second;
            Matcher matcher = Pattern.compile(regex).matcher(src);
            while (matcher.find()) {
                String s = matcher.group();
                target = s.substring(first.length(), s.length() - second.length());
            }
        }
        return target;
    }

}
