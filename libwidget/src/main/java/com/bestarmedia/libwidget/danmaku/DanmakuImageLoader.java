package com.bestarmedia.libwidget.danmaku;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by J Wong on 2016/7/11.
 */
public class DanmakuImageLoader {

    public interface DanmakuImageLoaderListener {
        void onDamakuCallback(String text, Drawable drawable);
    }

    private Context mContext;
    private String mText;
    private Uri mImgUrl;

    public DanmakuImageLoader(Context context, String tag) {
        mContext = context;
        mText = tag;
    }

    private DanmakuImageLoaderListener mDanmakuImageLoaderListener;

    public void setDanmakuImageLoaderListener(DanmakuImageLoaderListener loaderListener) {
        mDanmakuImageLoaderListener = loaderListener;
    }

    public void load(Uri imgUrl) {
        mImgUrl = imgUrl;
        Log.w("DanmakuImageLoader", "load url:" + mImgUrl);
        Glide.with(mContext).load(mImgUrl).transform(new CropCircleTransformation()).into(new SimpleTarget<Drawable>(120, 120) {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (mDanmakuImageLoaderListener != null) {
                    mDanmakuImageLoaderListener.onDamakuCallback(mText, resource);
                }
            }
        });
    }


}
