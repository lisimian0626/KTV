package com.bestarmedia.libwidget.anim;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.bestarmedia.libwidget.util.DensityUtil;

/**
 * author: Hanson
 * date:   2017/5/10
 * describe:
 */

public class AnimatorUtils {

    private static final String TAG = "AnimatorUtils";

    private static void animateParabola(final ViewGroup parent, final View src, View target) {
        final int[] locTag = new int[2];
        final int[] locSrc = new int[2];
        target.getLocationInWindow(locTag);
        src.getLocationInWindow(locSrc);

        int pointX = (locSrc[0] + locTag[0]) / 2;
        int pointY = locSrc[1] - locTag[1];
        final Point controllPoint = new Point(pointX, pointY);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(600);
        valueAnimator.setObjectValues(new PointF(locSrc[0], locSrc[1]), new PointF(locTag[0], locTag[1]));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                //贝塞尔曲线
                int x = (int) ((1 - fraction) * (1 - fraction) * startValue.x + 2 * fraction * (1 - fraction) * controllPoint.x + fraction * fraction * endValue.x);
                int y = (int) ((1 - fraction) * (1 - fraction) * startValue.y + 2 * fraction * (1 - fraction) * controllPoint.y + fraction * fraction * endValue.y);
                PointF p = new PointF(x, y);

                return p;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                src.setX(point.x);
                src.setY(point.y);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewGroup) parent.getParent()).removeView(src);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private static View addAnimatorView(ViewGroup parent, View src, String text, int resId) {
//        src.destroyDrawingCache();
//        src.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = src.getMeasuredWidth();
        int height = src.getMeasuredHeight();
//        src.layout(0, 0, width, height);
//        src.setDrawingCacheEnabled(true);
//        Bitmap bitmap = src.getDrawingCache(true);
//        Drawable drawable = new BitmapDrawable(bitmap);

        int[] location = new int[2];
        src.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        src.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标

        TextView view = new TextView(parent.getContext());
        view.setX(location[0] + width / 2);
        view.setY(location[1] + height / 2);
        view.setBackgroundResource(resId);
        view.setPadding(16, 16, 16, 16);
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, 28);
        view.setTextColor(Color.parseColor("#f4d573"));
        view.setText(text);
//        ((ViewGroup) parent.getParent()).addView(view);
        ((ViewGroup) parent.getParent()).addView(view, 200, 60);
        return view;
    }

    public static void playParabolaAnimator(ViewGroup parent, View src, View target, String text, int resId) {
        View view = addAnimatorView(parent, src, text, resId);
        animateParabola(parent, view, target);
    }


    public static void moveView2Target(View view, int[] locSrc, int[] locTag) {
        int pointX = (locSrc[0] + locTag[0]) / 2;
        int pointY = locSrc[1] - locTag[1];
        final Point controllerPoint = new Point(pointX, pointY);
        ValueAnimator valueAnimator = new ValueAnimator();
        double distance = Math.sqrt(Math.abs((locSrc[0] - locTag[0]) * (locSrc[0] - locTag[0]) + (locSrc[1] - locTag[1]) * (locSrc[1] - locTag[1])));
        valueAnimator.setDuration((int) (distance));
        valueAnimator.setObjectValues(new PointF(locSrc[0], locSrc[1]), new PointF(locTag[0], locTag[1]));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                int x = (int) ((1 - fraction) * (1 - fraction) * startValue.x + 2 * fraction * (1 - fraction) * controllerPoint.x + fraction * fraction * endValue.x);
                int y = (int) ((1 - fraction) * (1 - fraction) * startValue.y + 2 * fraction * (1 - fraction) * controllerPoint.y + fraction * fraction * endValue.y);
                PointF p = new PointF(x, y);
                return p;
            }
        });
        valueAnimator.start();
        valueAnimator.addUpdateListener(animation -> {
            PointF point = (PointF) animation.getAnimatedValue();
            view.setX(point.x);
            view.setY(point.y);
        });
    }


    private static View addAnimatorView(ViewGroup parent, int resId) {
        View view = new View(parent.getContext());
        view.setX(parent.getWidth() / 2);
        view.setY(parent.getHeight() / 3);
        view.setBackgroundResource(resId);
        ((ViewGroup) parent.getParent()).addView(view, DensityUtil.dip2px(parent.getContext(), 60), DensityUtil.dip2px(parent.getContext(), 60));
        return view;
    }

    public static void playParabolaAnimator(ViewGroup parent, View target, int resId) {
        View view = addAnimatorView(parent, resId);
        animateParabola(parent, view, target);
    }

}
