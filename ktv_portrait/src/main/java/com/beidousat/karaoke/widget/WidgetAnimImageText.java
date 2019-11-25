package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.beidousat.karaoke.R;
import com.bestarmedia.libwidget.anim.Anim;
import com.bestarmedia.libwidget.anim.EnterAnimUtil;
import com.bestarmedia.libwidget.image.RecyclerImageView;
import com.bestarmedia.libwidget.layout.EnterAnimLayout;
import com.bestarmedia.libwidget.text.GradientTextView;
import com.bestarmedia.libwidget.util.GlideUtils;

public class WidgetAnimImageText extends EnterAnimLayout {

    private View mRootView;
    private EnterAnimLayout enterAnimLayout;
    private RecyclerImageView ivQrCode;
    private GradientTextView tvText;

    public WidgetAnimImageText(Context context) {
        super(context);
        initView();
    }

    public WidgetAnimImageText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WidgetAnimImageText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_anim_image_text, this);
        enterAnimLayout = mRootView.findViewById(R.id.root);
        ivQrCode = mRootView.findViewById(R.id.iv_qr_code);
        tvText = mRootView.findViewById(R.id.tv_text);
    }

    public void setImageText(Bitmap bitmap, String text) {
        ivQrCode.setImageBitmap(bitmap);
        tvText.setText(text);
        tvText.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        Anim anim = EnterAnimUtil.getRandomEnterAnim(enterAnimLayout);
        anim.startAnimation(800);
    }

    public void setImageText(String imageUrl, String text) {
        GlideUtils.LoadImage(getContext(), imageUrl, false, ivQrCode);
        tvText.setText(text);
        tvText.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
        Anim anim = EnterAnimUtil.getRandomEnterAnim(enterAnimLayout);
        anim.startAnimation(800);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        float scale = (float) w / getContext().getResources().getDimensionPixelSize(R.dimen.television_qr_code_width);
        tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.television_qr_code_text_size) * scale);
    }
}
