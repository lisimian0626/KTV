//package com.bestarmedia.libwidget.util;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Registry;
//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
//import com.bumptech.glide.load.model.GlideUrl;
//import com.bumptech.glide.module.AppGlideModule;
//
//import java.io.InputStream;
//
//@GlideModule
//public class BaseGlide extends AppGlideModule {
//
//    @Override
//    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
//        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(UnsafeOkHttpClient.getUnsafeOkHttpClient());
//        registry.replace(GlideUrl.class, InputStream.class, factory);
////        super.registerComponents(context, glide, registry);
//    }
//
//}
