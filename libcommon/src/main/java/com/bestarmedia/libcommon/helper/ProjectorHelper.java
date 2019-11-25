//package com.bestarmedia.libcommon.helper;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.bestarmedia.libcommon.http.HttpRequestListener;
//import com.bestarmedia.libcommon.http.HttpRequestV4;
//import com.bestarmedia.libcommon.http.RequestMethod;
//import com.bestarmedia.libcommon.model.vod.SceneV4;
//import com.bestarmedia.libcommon.util.Logger;
//
///**
// * Created by J Wong on 2016/10/10.
// */
//
//public class ProjectorHelper {
//
//    private final static String SCENES_DEFAULT_1 = "data/File/SceneDefault_1.mp4";
//    private final static String SCENES_DEFAULT_2 = "data/File/SceneDefault_2.mp4";
//    private final static String SCENES_DEFAULT_3 = "data/File/SceneDefault_3.mp4";
//
//    public static void sendStarScene2Projector(Context context, String singerId) {
//        if (!TextUtils.isEmpty(singerId)) {
//            HttpRequestV4 request = new HttpRequestV4(context.getApplicationContext(), RequestMethod.VOD_SCENE);
//            request.setHttpRequestListener(new HttpRequestListener() {
//                @Override
//                public void onStart(String method) {
//                }
//
//                @Override
//                public void onSuccess(String method, Object object) {
//                    try {
//                        if (RequestMethod.VOD_SCENE.equalsIgnoreCase(method)) {
//                            SceneV4 sceneV4;
//                            if (object != null && object instanceof SceneV4 && (sceneV4 = (SceneV4) object) != null
//                                    && sceneV4.scene != null && !TextUtils.isEmpty(sceneV4.scene.SceneFile)) {
//                                SocketOperationUtil.sendVideoUrl2Projector(0, sceneV4.scene.SceneFile);
//                            }
//                        }
//                    } catch (Exception e) {
//                        Logger.w("ProjectorHelper", "sendStarScene2Projector ex:" + e.toString());
//                    }
//                }
//
//                @Override
//                public void onFailed(String method, String error) {
//                    SocketOperationUtil.sendVideoUrl2Projector(0, AdModelDefault.getPatchDefaultAd().get(0).getAdContent());
//                }
//            });
//            request.addParam("singer_id", singerId);
//            request.setConvert2Class(SceneV4.class);
//            request.get();
//        } else {
//            SocketOperationUtil.sendVideoUrl2Projector(0, AdModelDefault.getPatchDefaultAd().get(0).getAdContent());
//        }
//    }
//
//    public static void sendMv2Scene2Projector(String url, int position) {
//        SocketOperationUtil.sendVideoUrl2Projector(position, url);
//    }
//
//
//    public static void sendBirthday2Projector() {
//        SocketOperationUtil.sendVideoUrl2Projector(0, SCENES_DEFAULT_1);
//    }
//
//    public static void sendRomantic2Projector() {
//        SocketOperationUtil.sendVideoUrl2Projector(0, SCENES_DEFAULT_2);
//    }
//
//
//    public static void sendHot2Projector() {
//        SocketOperationUtil.sendVideoUrl2Projector(0, SCENES_DEFAULT_3);
//    }
//}
