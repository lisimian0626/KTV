package com.bestarmedia.libwidget.floatview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bestarmedia.libwidget.R;

public class EnFloatingView extends FloatingMagnetView {

    private final ImageView mIcon;

    public EnFloatingView(@NonNull Context context) {
        super(context, null);
        inflate(context, R.layout.en_floating_view, this);
        mIcon = findViewById(R.id.icon);
    }

    public void setIconImage(@DrawableRes int resId) {
        mIcon.setImageResource(resId);
    }

    public ImageView getImageView() {
        return mIcon;
    }

}
