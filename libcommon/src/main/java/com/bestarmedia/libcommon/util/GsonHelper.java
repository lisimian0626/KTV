package com.bestarmedia.libcommon.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.MessageFormat;


public class GsonHelper {

    private static final String TAG = "GsonHelper";
    private static Gson sGson;

    public static final String TEST = "{\"status\":\"1001\",\"info\":\"\\u6210\\u529f\",\"data\":{\"list\":[{\"ID\":\"006f71cf-b3d8-421a-b88c-0bec3dceb028\",\"SimpName\":\"JustGiveMeaReason\",\"Img\":null,\"SongFileName\":\"006f71cf-b3d8-421a-b88c-0bec3dceb028.MP4\",\"SongFilePath\":\"\\/data\\/song\\/006f71cf-b3d8-421a-b88c-0bec3dceb028.mp4\",\"SingerName\":false},{\"ID\":\"00b961bb-1c9f-42a9-904b-9e1f6d04e4c2\",\"SimpName\":\"\\u5b64\\u72ec\\u5144\\u5f1f\",\"Img\":null,\"SongFileName\":\"00b961bb-1c9f-42a9-904b-9e1f6d04e4c2.MP4\",\"SongFilePath\":\"\\/data\\/song\\/00b961bb-1c9f-42a9-904b-9e1f6d04e4c2.mp4\",\"SingerName\":false},{\"ID\":\"00cae3e0-e4ca-4b08-a3fe-f63be8889f6b\",\"SimpName\":\"\\u563f!\\u59d1\\u5a18\",\"Img\":null,\"SongFileName\":\"00cae3e0-e4ca-4b08-a3fe-f63be8889f6b.MP4\",\"SongFilePath\":\"\\/data\\/song\\/00cae3e0-e4ca-4b08-a3fe-f63be8889f6b.mp4\",\"SingerName\":\"\\u53cd\\u5149\\u955c\\u4e50\\u961f\"},{\"ID\":\"00f587cc-c293-4402-939e-4b1c91cfe185\",\"SimpName\":\"\\u4e5f\\u8bb8\\u660e\\u5929\",\"Img\":null,\"SongFileName\":\"00f587cc-c293-4402-939e-4b1c91cfe185.MP4\",\"SongFilePath\":\"\\/data\\/song\\/00f587cc-c293-4402-939e-4b1c91cfe185.mp4\",\"SingerName\":false},{\"ID\":\"00f67afa-dcd6-4166-a0d8-d1fca809ed0a\",\"SimpName\":\"Baby\",\"Img\":null,\"SongFileName\":\"00f67afa-dcd6-4166-a0d8-d1fca809ed0a.MP4\",\"SongFilePath\":\"\\/data\\/song\\/00f67afa-dcd6-4166-a0d8-d1fca809ed0a.mp4\",\"SingerName\":false},{\"ID\":\"0110cc79-8140-4f48-99c2-def2488aa3cb\",\"SimpName\":\"Habits\",\"Img\":null,\"SongFileName\":\"0110cc79-8140-4f48-99c2-def2488aa3cb.MP4\",\"SongFilePath\":\"\\/data\\/song\\/0110cc79-8140-4f48-99c2-def2488aa3cb.mp4\",\"SingerName\":false},{\"ID\":\"01244652-6add-4cc0-9175-df6a2859488a\",\"SimpName\":\"\\u7231\\u662f\\u4fe1\\u4ef0\",\"Img\":null,\"SongFileName\":\"01244652-6add-4cc0-9175-df6a2859488a.MP4\",\"SongFilePath\":\"\\/data\\/song\\/01244652-6add-4cc0-9175-df6a2859488a.mp4\",\"SingerName\":\"\\u6797\\u6615\\u9633\"},{\"ID\":\"012b238f-cd6a-496d-8a3c-b55f7b91f899\",\"SimpName\":\"\\u767d\\u6866\\u6797\",\"Img\":null,\"SongFileName\":\"012b238f-cd6a-496d-8a3c-b55f7b91f899.MP4\",\"SongFilePath\":\"\\/data\\/song\\/012b238f-cd6a-496d-8a3c-b55f7b91f899.mp4\",\"SingerName\":false},{\"ID\":\"013414a3-0903-438d-8cc4-9a4999916608\",\"SimpName\":\"\\u51ac\\u5929\\u91cc\\u7684\\u4e00\\u628a\\u706b\",\"Img\":null,\"SongFileName\":\"013414a3-0903-438d-8cc4-9a4999916608.MP4\",\"SongFilePath\":\"\\/data\\/song\\/013414a3-0903-438d-8cc4-9a4999916608.mp4\",\"SingerName\":\"\\u5f20\\u9753\\u9896\"},{\"ID\":\"01903557-3603-4092-9d98-7d445bbad858\",\"SimpName\":\"\\u6642\\u7a7a\\u30c4\\u30a2\\u30fc\\u30ba\",\"Img\":null,\"SongFileName\":\"01903557-3603-4092-9d98-7d445bbad858.MP4\",\"SongFilePath\":\"\\/data\\/song\\/01903557-3603-4092-9d98-7d445bbad858.mp4\",\"SingerName\":false}],\"total\":\"2160\",\"nowPage\":1,\"totalPages\":216}}";
    private static Gson getGson() {
        if (sGson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.excludeFieldsWithoutExposeAnnotation();
            sGson = gsonBuilder.create();
        }
        return sGson;
    }

    /**
     * 将对象转换为Json
     *
     * @param srcObj 对象
     * @return Json字符串
     */
    public static String objectToJson(Object srcObj) {
        String strJson = "";
        try {
            strJson = getGson().toJson(srcObj);
        } catch (Exception ex) {
            Logger.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
                    "objectToJson", ex.getMessage()));
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return strJson;
    }

    /**
     * 将Json格式的字符串转换为对象
     *
     * @param json  Json格式字符串
     * @param clazz 实体对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        try {
            T t = getGson().fromJson(json, clazz);
            return t;
        } catch (Exception ex) {
            Logger.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG,
                    "jsonToObject", ex.getMessage()));
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * 将Json格式的字符串转换为泛型集合
     *
     * @param json Json格式字符串
     */
    public static <T> T jsonToObject(String json, TypeToken<T> token) {
        try {
            T t = getGson().fromJson(json, token.getType());
            return t;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }





}

