package com.bestarmedia.libwidget.util;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {

    //加载本地一般图片
    public static void LoadImage(Context context, int ResourceId, boolean skipMemoryCache, ImageView imageView) {
        Glide.with(context).load(ResourceId).skipMemoryCache(skipMemoryCache)
                .into(imageView);
    }

    //网络加载一般图片
    public static void LoadImage(Context context, String url, boolean skipMemoryCache, ImageView imageView) {
        Glide.with(context).load(url).centerCrop().skipMemoryCache(skipMemoryCache)
                .into(imageView);
    }

    //网络加载一般图片设置占位图
    public static void LoadImage(Context context, String url, int placeholderResourceId, int errorResourceId, boolean skipMemoryCache, ImageView imageView) {
        Glide.with(context).load(url).skipMemoryCache(skipMemoryCache)
                .placeholder(placeholderResourceId)
                .error(errorResourceId)
                .into(imageView);
    }

    //圆角图片
    public static void LoadCornersImage(Context context, String url, boolean skipMemoryCache, ImageView imageView, int num) {
        RoundedCorners roundedCorners = new RoundedCorners(num);
        Glide.with(context).load(url).skipMemoryCache(skipMemoryCache)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(roundedCorners))
                .into(imageView);
    }

    //圆角图片
    public static void LoadCornersImage(Context context, Uri uri, boolean skipMemoryCache, ImageView imageView, int num) {
        RoundedCorners roundedCorners = new RoundedCorners(num);
        Glide.with(context).load(uri).skipMemoryCache(skipMemoryCache)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(roundedCorners))
                .into(imageView);
    }

    //圆角图片设置占位图
    public static void LoadCornersImage(Context context, String url, int placeholderResourceId, int errorResourceId, int num, boolean skipMemoryCache, ImageView imageView) {
        RoundedCorners roundedCorners = new RoundedCorners(num);
        Glide.with(context).load(url).placeholder(placeholderResourceId).error(errorResourceId).skipMemoryCache(skipMemoryCache)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(roundedCorners))
                .into(imageView);
    }

    //圆角图片设置占位图
    public static void loadCornersImage(Context context, Uri uri, int placeholderResourceId, int errorResourceId, int num, boolean skipMemoryCache, ImageView imageView) {
        RoundedCorners roundedCorners = new RoundedCorners(num);
        Glide.with(context).load(uri).placeholder(placeholderResourceId).error(errorResourceId).skipMemoryCache(skipMemoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(roundedCorners))
                .into(imageView);
    }

    //瀑布流图片
    public static void loadStaggeredImage(final Context context, String url, int width, int high, final ImageView imageView) {
        Glide.with(context).load(url).override(width, high).fitCenter().into(imageView);
    }

    //加载本地Gif图片
    public static void loadLocalGif(Context context, int resId, ImageView imageView) {
        Glide.with(context).asGif().load(resId).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
    }

    //加载网络Gif图片
    public static void loadGif(Context context, String url, ImageView imageView) {
        Glide.with(context).asGif().load(url).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
    }

    //圆角图片设置占位图
    public static void LoadCornersImage(Fragment fragment, String url, int placeholderResourceId, int errorResourceId, int num, boolean skipMemoryCache, ImageView imageView) {
        RoundedCorners roundedCorners = new RoundedCorners(num);
        Glide.with(fragment).load(url).placeholder(placeholderResourceId).error(errorResourceId).skipMemoryCache(skipMemoryCache)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(roundedCorners))
                .into(imageView);
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(() -> Glide.get(context).clearDiskCache()).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            Log.e("GlideUtils", "清除图片磁盘缓存出错了", e);
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            Log.e("GlideUtils", "清除图片内存缓存出错了", e);
        }
    }
}
