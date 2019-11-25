package com.beidousat.karaoke.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class FloatView extends LinearLayout {

    private WindowManager.LayoutParams wmParams;
    private WindowManager wm;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    private IFloatViewClick listener;
    private boolean isAllowTouch = true;

    public FloatView(Context context, int x, int y, int layoutres) {
        super(context);
        View view = LayoutInflater.from(getContext()).inflate(layoutres, null);
        init(view, x, y);
    }

    public FloatView(Context context, int x, int y, View childView) {
        super(context);
        init(childView, x, y);
    }

    private void init(View childView, int x, int y) {
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        //设置你要添加控件的类型，TYPE_ALERT需要申明权限，TOast不需要，在某些定制系统中会禁止悬浮框显示，所以最后用TYPE_TOAST
        wmParams.type = WindowManager.LayoutParams.FIRST_SUB_WINDOW;
        //设置控件在坐标计算规则，相当于屏幕左上角
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = (int) x;
        wmParams.y = (int) y;
        if (childView != null) {
            addView(childView);
        }
        //   wm.addView(this, wmParams);
    }

    /**
     * 更新位置
     *
     * @param x
     * @param y
     */
    public void updateFloatViewPosition(int x, int y, int windowWidth, int windowHeight) {
        wmParams.x = x;
        wmParams.y = y;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = windowHeight;
        wm.updateViewLayout(this, wmParams);
    }

    public void setFloatViewClickListener(IFloatViewClick listener) {
        this.listener = listener;
    }

    /**
     * 添加至窗口
     *
     * @return
     */
    public boolean addToWindow() {
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    wm.addView(this, wmParams);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() == null) {
                        wm.addView(this, wmParams);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }


        } else {
            return false;
        }
    }

    public void setIsAllowTouch(boolean flag) {
        isAllowTouch = flag;
    }

    /**
     * 从窗口移除
     *
     * @return
     */
    public boolean removeFromWindow() {
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    wm.removeViewImmediate(this);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() != null) {
                        wm.removeViewImmediate(this);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }


        } else {
            return false;
        }

    }


    // 此wmParams为获取的全局变量，用以保存悬浮窗口的属性

    // 重写，返回true 拦截触摸事件
//	 @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return isAllowTouch;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = (int) event.getRawX() - this.getMeasuredWidth() / 2;
                mTouchStartY = (int) event.getRawY() - this.getMeasuredHeight() / 2;

                return true;
            case MotionEvent.ACTION_MOVE:
                wmParams.x = (int) event.getRawX() - this.getMeasuredWidth() / 2;
                // 减25为状态栏的高度
                wmParams.y = (int) event.getRawY() - this.getMeasuredHeight() / 2;
                // 刷新
                if (Math.abs(wmParams.y - mTouchStartY) > 10 || Math.abs(wmParams.x - mTouchStartX) > 10) {
                    wm.updateViewLayout(this, wmParams);
                }
                return true;
            case MotionEvent.ACTION_UP:
                y = (int) event.getRawY() - this.getMeasuredHeight() / 2;
                x = (int) event.getRawX() - this.getMeasuredWidth() / 2;
                if (Math.abs(y - mTouchStartY) > 10 || Math.abs(x - mTouchStartX) > 10) {
                    wm.updateViewLayout(this, wmParams);
                } else {
                    if (listener != null) {
                        listener.onFloatViewClick();
                    }

                }
                return true;
            default:
                break;
        }
        return false;

    }


    public interface IFloatViewClick {
        void onFloatViewClick();
    }

}
