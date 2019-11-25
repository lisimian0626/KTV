package com.bestarmedia.libcommon.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by J Wong on 2016/6/22.
 */
public class GsonUtil {

    public static String map2Json(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        if (map != null && map.size() > 0) {
            builder.append("{");
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                builder.append("\"").append(key).append("\"").append(":").append("\"").append(val).append("\"").append(",");
            }
            int index = builder.lastIndexOf(",");
            builder.delete(index, index + 1);

            builder.append("}");
        }
        Logger.d("GsonUtil", builder.toString());
        return builder.toString();
    }

    public static Object convert2Object(Object object, Class<?> cls) {
        Object obj = null;
        if (object != null) {
            try {
                Gson gson = new Gson();
                obj = gson.fromJson(object.toString(), cls);
            } catch (Exception e) {
                Logger.e("GsonUtil", "convert2Object Exception :" + e.toString() + " str:" + object.toString());
            }
        }
        return obj;
    }


    public static Object convert2Token(Object object, TypeToken<?> typeToken) {
        Object obj = null;
        try {
            String json = object.toString();
            Gson gson = new Gson();
            obj = gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            Logger.e("GsonUtil", "convert2Token Exception :" + e.toString());
        }
        return obj;
    }


    public static JsonElement convert3JsonElement(Object object) {
        try {
            Gson gson = new Gson();
            return gson.toJsonTree(object);
        } catch (Exception e) {
            Logger.d("GsonUtil", e.toString());
        }
        return null;
    }
}
