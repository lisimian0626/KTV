package com.bestarmedia.libwidget.text;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;


/**
 * Created by J Wong on 2018/11/30.
 * <p>
 * 就像上面的显示效果一样一束白光闪过，这种效果主要还是使用了LinearGradient类来进行的
 * LinearGradient也称作线性渲染，LinearGradient的作用是实现某一区域内颜色的线性渐变效果
 */

public class GradientBaseTextView extends android.support.v7.widget.AppCompatTextView {

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0, mViewHeight;
    private int mTranslate = 0;
    private int mStartColor = Color.WHITE, mEndColor = Color.WHITE;
    private boolean mAnimating = false;
    private boolean mIsLeft2Right = true;

    public GradientBaseTextView(Context context) {
        this(context, null);
    }

    public GradientBaseTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientBaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        paintGradient();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
    }

    @Override
    public void setHighlightColor(int color) {
        super.setHighlightColor(color);
        paintGradient();
    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        paintGradient();
    }

/**
 *  public LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1, Shader.TileMode tile)
 *其中，参数x0表示渐变的起始点x坐标；参数y0表示渐变的起始点y坐标；参数x1表示渐变的终点x坐标；参数y1表示渐变的终点y坐标　；color0表示渐变开始颜色；color1表示渐变结束颜色；参数tile表示平铺方式。
 Shader.TileMode有3种参数可供选择，分别为CLAMP、REPEAT和MIRROR：
 CLAMP的作用是如果渲染器超出原始边界范围，则会复制边缘颜色对超出范围的区域进行着色
 REPEAT的作用是在横向和纵向上以平铺的形式重复渲染位图
 MIRROR的作用是在横向和纵向上以镜像的方式重复渲染位图
 */

    /**
     * public LinearGradient (float x0, float y0, float x1, float y1, int[] colors, float[] positions, Shader.TileMode tile);
     * 其中，参数x0表示渐变的起始点x坐标；参数y0表示渐变的起始点y坐标；参数x1表示渐变的终点x坐标；参数y1表示渐变的终点y坐标；参数colors表示渐变的颜色数组；
     * 参数positions用来指定颜色数组的相对位置；参数tile表示平铺方式。通常，参数positions设为null，表示颜色数组以斜坡线的形式均匀分布。
     */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            paintGradient();
        }
    }

    private void paintGradient() {
        if (isSelected()) {
            mStartColor = getTextColors().getColorForState(new int[]{android.R.attr.state_selected}, getTextColors().getDefaultColor());
            mEndColor = getTextColors().getColorForState(new int[]{android.R.attr.state_checked}, getTextColors().getDefaultColor());
        } else if (isPressed()) {
            mStartColor = getTextColors().getColorForState(new int[]{android.R.attr.state_pressed}, getTextColors().getDefaultColor());
            mEndColor = getTextColors().getColorForState(new int[]{android.R.attr.state_focused}, getTextColors().getDefaultColor());
        } else {
            mStartColor = getTextColors().getDefaultColor();
            mEndColor = getHighlightColor();
        }
//        Log.d(getClass().getSimpleName(), "mStartColor:#" + Integer.toHexString(mStartColor) + " mEndColor:#" + Integer.toHexString(mEndColor));
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        if (mViewWidth > 0) {
            mPaint = getPaint();
            if (mAnimating) {
                mLinearGradient = new LinearGradient(-mViewWidth, 0, 0, mViewHeight, new int[]{mEndColor, mStartColor, mEndColor}, new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
            } else {
                if (mIsLeft2Right) {
                    mLinearGradient = new LinearGradient(0, 0, mViewWidth, mViewHeight, new int[]{mStartColor, mEndColor}, new float[]{0.0f, 1.0f}, Shader.TileMode.MIRROR);
                } else {
                    mLinearGradient = new LinearGradient(mViewWidth, mViewHeight - 5, 0, mViewHeight - 5, new int[]{mStartColor, mEndColor}, new float[]{0.0f, 1.0f}, Shader.TileMode.MIRROR);
                }
            }
            mPaint.setShader(mLinearGradient);
            mGradientMatrix = new Matrix();
        }
    }

    /**
     * RGB分解
     *
     * public static int getRGB(int rgbColor, int alpha) {
     * int red = (0xff0000 & rgbColor) >> 16;
     * int green = (0xff00 & rgbColor) >> 8;
     * int blue = (0xff & rgbColor);
     * return Color.argb(alpha, red, green, blue);
     * }
     **/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null) {
            /*
            mTranslate += mViewWidth / 10;很简单表示每一次运动的递增值
             */
            mTranslate += mViewWidth / 10;
            /*
             */
            if (mTranslate > 2 * mViewWidth) {
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(50);
        }
    }
}
