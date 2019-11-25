package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.text.MarqueeTextView;
import com.bestarmedia.libwidget.util.GlideUtils;

/**
 * Created by J Wong on 2017/9/26.
 */

public class WidgetTextNotification extends RelativeLayout {

    private View mRootView;
    private RecyclerImageView rivBackground;
    private MarqueeTextView marqueeTextView;

    public WidgetTextNotification(Context context) {
        super(context);
        initView();
    }

    public WidgetTextNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WidgetTextNotification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_text_notification, this);
        rivBackground = mRootView.findViewById(android.R.id.background);
        marqueeTextView = mRootView.findViewById(android.R.id.text1);
    }

    public void play(String backgroundUrl, String text, String color, boolean loadBackground) {
        if (TextUtils.isEmpty(backgroundUrl)) {
            if (loadBackground)
                rivBackground.setImageResource(R.drawable.bg_notification_default);
        } else {
            GlideUtils.LoadImage(getContext(), backgroundUrl, false, rivBackground);
        }
        marqueeTextView.setTextDistance(50);//设置间距
        marqueeTextView.setTextSpeed(0.6F);//设置速度
        try {
            if (!TextUtils.isEmpty(color)) {
                marqueeTextView.setTextColor(Color.parseColor(color));//设置颜色
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "颜色解析出错了", e);
        }
        marqueeTextView.setTextSize(36);//设置文字大小
        marqueeTextView.setContent(text);
    }

}
