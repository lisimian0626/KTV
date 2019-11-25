package com.bestarmedia.libwidget.material;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.interf.DefaultClickEffectScaleAnimate;
import com.bestarmedia.libwidget.interf.OnClickEffectTouchListener;

public class ImageMaterial extends RecyclerImageView {

    public ImageMaterial(Context context) {
        super(context);
        init();
    }

    public ImageMaterial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageMaterial(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        OnClickEffectTouchListener onClickEffectTouchListener = new OnClickEffectTouchListener();
        onClickEffectTouchListener.setViewClickEffect(new DefaultClickEffectScaleAnimate());
        this.setOnTouchListener(onClickEffectTouchListener);
    }
}
