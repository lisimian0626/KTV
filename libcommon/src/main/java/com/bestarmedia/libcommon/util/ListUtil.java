package com.bestarmedia.libcommon.util;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by J Wong on 2016/1/4 19:06.
 */
public class ListUtil {

    public static List<String> array2List(String[] texts) {
        if (texts != null && texts.length > 0) {
            List<String> list = Arrays.asList(texts);
            return list;
        }
        return new ArrayList<>();
    }

    public static boolean isEmptyArray(String[] strings) {
        for (String s : strings) {
            if (!TextUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    public static <E> List<E> deepCopy(List<E> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<E> dest = (List<E>) in.readObject();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
