package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.beidousat.karaoke.R;
import com.bestarmedia.libcommon.model.view.RollText;
import com.bestarmedia.libwidget.danmaku.RollTextView;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.util.GlideUtils;
import com.bestarmedia.proto.node.Text;

/**
 * Created by J Wong on 2019/7/11 16:56.
 */
public class NotificationWidget extends LinearLayout implements RollTextView.OnRollListener {

    private RecyclerImageView ivBackground;
    private RollTextView rollTextView;
    private RollTextView.OnRollListener onRollListener;

    public NotificationWidget(Context context) {
        super(context);
        initView();
    }

    public NotificationWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NotificationWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_notification, this);
        ivBackground = rootView.findViewById(android.R.id.background);
        rollTextView = rootView.findViewById(android.R.id.text1);
        rollTextView.setOnRollListener(this);
    }


    public void setOnRollListener(RollTextView.OnRollListener listener) {
        onRollListener = listener;
    }

    @Override
    public void onRollStart(RollText text) {
        if (onRollListener != null) {
            onRollListener.onRollStart(text);
        }
    }

    @Override
    public void onRollQueueEmpty(RollText last) {
        if (onRollListener != null) {
            onRollListener.onRollQueueEmpty(last);
        }
    }

    public void loadBackground(String url) {
        GlideUtils.LoadImage(getContext(), url, false, ivBackground);
//        GlideUtils.LoadImage(getContext(), R.drawable.bg_keyboard_hand_writing, true, ivBackground);
    }

    public void playText(Text text) {
        int color = Color.WHITE;
        try {
            if (!TextUtils.isEmpty(text.getColor())) {
                color = Color.parseColor(text.getColor());
            }
        } catch (Exception e) {
        }
        if (!TextUtils.isEmpty(text.getBackground())) {
            loadBackground(text.getBackground());
        }
        rollTextView.setPlayInterval(text.getPlayInterval());
        int count = text.getPlayCount() > 0 ? text.getPlayCount() : 1;
        for (int i = 0; i < count; i++) {
            rollTextView.addRollText(true, String.valueOf(0), text.getPlayDirection(), text.getContent(), color,
                    24.0f, Color.TRANSPARENT, text.getPlayTime() > 0 ? text.getPlayTime() : RollTextView.DEFAULT_DURATION);
        }
    }
}
