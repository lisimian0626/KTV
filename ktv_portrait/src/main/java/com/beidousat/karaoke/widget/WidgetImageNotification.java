package com.beidousat.karaoke.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bumptech.glide.Glide;

/**
 * Created by J Wong on 2017/9/26.
 */

public class WidgetImageNotification extends RelativeLayout {

    private View mRootView;
    private RecyclerImageView rivBackground, rivImage;

    public WidgetImageNotification(Context context) {
        super(context);
        initView();
    }

    public WidgetImageNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WidgetImageNotification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_image_notification, this);
        rivBackground = mRootView.findViewById(android.R.id.background);
        rivImage = mRootView.findViewById(android.R.id.icon);
    }

    public void play(String backgroundUrl, String imageUrl, boolean loadBackground) {
        if (TextUtils.isEmpty(backgroundUrl)) {
            if (loadBackground)
                Glide.with(getContext()).load(R.drawable.bg_notification_default).into(rivBackground);
        } else {
            Glide.with(getContext()).load(backgroundUrl).into(rivBackground);
        }
        Glide.with(getContext()).load(imageUrl).into(rivImage);
    }
}
