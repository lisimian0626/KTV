package com.beidousat.karaoke.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class MyPopWindow implements PopupWindow.OnDismissListener{
    private static final String TAG = "MyPopWindow";
    private static final float DEFAULT_ALPHA = 0.7F;
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private boolean mIsFocusable;
    private boolean mIsOutside;
    private int mResLayoutId;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private int mAnimationStyle;
    private boolean mClippEnable;
    private boolean mIgnoreCheekPress;
    private int mInputMode;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private int mSoftInputMode;
    private boolean mTouchable;
    private View.OnTouchListener mOnTouchListener;
    private Window mWindow;
    private boolean mIsBackgroundDark;
    private float mBackgroundDrakValue;
    private boolean enableOutsideTouchDisMiss;

    private MyPopWindow(Context context) {
        this.mIsFocusable = true;
        this.mIsOutside = true;
        this.mResLayoutId = -1;
        this.mAnimationStyle = -1;
        this.mClippEnable = true;
        this.mIgnoreCheekPress = false;
        this.mInputMode = -1;
        this.mSoftInputMode = -1;
        this.mTouchable = true;
        this.mBackgroundDrakValue = 0.0F;
        this.mIsBackgroundDark = false;
        this.enableOutsideTouchDisMiss = true;
        this.mContext = context;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public MyPopWindow showAsDropDown(View anchor, int xOff, int yOff) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff);
        }

        return this;
    }

    public MyPopWindow showAsDropDown(View anchor) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor);
        }

        return this;
    }

    @RequiresApi(
            api = 19
    )
    public MyPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAsDropDown(anchor, xOff, yOff, gravity);
        }

        return this;
    }

    public MyPopWindow showAtLocation(View parent, int gravity, int x, int y) {
        if (this.mPopupWindow != null) {
            this.mPopupWindow.showAtLocation(parent, gravity, x, y);
        }

        return this;
    }

    private void apply(PopupWindow popupWindow) {
        popupWindow.setClippingEnabled(this.mClippEnable);
        if (this.mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress();
        }

        if (this.mInputMode != -1) {
            popupWindow.setInputMethodMode(this.mInputMode);
        }

        if (this.mSoftInputMode != -1) {
            popupWindow.setSoftInputMode(this.mSoftInputMode);
        }

        if (this.mOnDismissListener != null) {
            popupWindow.setOnDismissListener(this.mOnDismissListener);
        }

        if (this.mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(this.mOnTouchListener);
        }

        popupWindow.setTouchable(this.mTouchable);
    }

    private PopupWindow build() {
        if (this.mContentView == null) {
            this.mContentView = LayoutInflater.from(this.mContext).inflate(this.mResLayoutId, (ViewGroup) null);
        }

        Activity activity = (Activity) this.mContentView.getContext();
        if (activity != null && this.mIsBackgroundDark) {
            float alpha = this.mBackgroundDrakValue > 0.0F && this.mBackgroundDrakValue < 1.0F ? this.mBackgroundDrakValue : 0.7F;
            this.mWindow = activity.getWindow();
            WindowManager.LayoutParams params = this.mWindow.getAttributes();
            params.alpha = alpha;
            this.mWindow.addFlags(2);
            this.mWindow.setAttributes(params);
        }

        if (this.mWidth != 0 && this.mHeight != 0) {
            this.mPopupWindow = new PopupWindow(this.mContentView, this.mWidth, this.mHeight);
        } else {
            this.mPopupWindow = new PopupWindow(this.mContentView, -2, -2);
        }

        if (this.mAnimationStyle != -1) {
            this.mPopupWindow.setAnimationStyle(this.mAnimationStyle);
        }

        this.apply(this.mPopupWindow);
        if (this.mWidth == 0 || this.mHeight == 0) {
            this.mPopupWindow.getContentView().measure(0, 0);
            this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
            this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
        }

        this.mPopupWindow.setOnDismissListener(this);
        if (!this.enableOutsideTouchDisMiss) {
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setOutsideTouchable(false);
            this.mPopupWindow.setBackgroundDrawable((Drawable) null);
            this.mPopupWindow.getContentView().setFocusable(true);
            this.mPopupWindow.getContentView().setFocusableInTouchMode(true);
            this.mPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 4) {
                        MyPopWindow.this.mPopupWindow.dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            this.mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (event.getAction() != 0 || x >= 0 && x < MyPopWindow.this.mWidth && y >= 0 && y < MyPopWindow.this.mHeight) {
                        if (event.getAction() == 4) {
                            Log.e("MyPopWindow", "out side ...");
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        Log.e("MyPopWindow", "out side ");
                        Log.e("MyPopWindow", "width:" + MyPopWindow.this.mPopupWindow.getWidth() + "height:" + MyPopWindow.this.mPopupWindow.getHeight() + " x:" + x + " y  :" + y);
                        return true;
                    }
                }
            });
        } else {
            this.mPopupWindow.setFocusable(this.mIsFocusable);
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
        }

        this.mPopupWindow.update();
        return this.mPopupWindow;
    }

    public void onDismiss() {
        this.dissmiss();
    }

    public void dissmiss() {
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }

        if (this.mWindow != null) {
            WindowManager.LayoutParams params = this.mWindow.getAttributes();
            params.alpha = 1.0F;
            this.mWindow.setAttributes(params);
        }

        if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }

    }

    public PopupWindow getPopupWindow() {
        return this.mPopupWindow;
    }

    public static class PopupWindowBuilder {
        private MyPopWindow mMyPopWindow;

        public PopupWindowBuilder(Context context) {
            this.mMyPopWindow = new MyPopWindow(context);
        }

        public MyPopWindow.PopupWindowBuilder size(int width, int height) {
            this.mMyPopWindow.mWidth = width;
            this.mMyPopWindow.mHeight = height;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setFocusable(boolean focusable) {
            this.mMyPopWindow.mIsFocusable = focusable;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setView(int resLayoutId) {
            this.mMyPopWindow.mResLayoutId = resLayoutId;
            this.mMyPopWindow.mContentView = null;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setView(View view) {
            this.mMyPopWindow.mContentView = view;
            this.mMyPopWindow.mResLayoutId = -1;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable) {
            this.mMyPopWindow.mIsOutside = outsideTouchable;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setAnimationStyle(int animationStyle) {
            this.mMyPopWindow.mAnimationStyle = animationStyle;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setClippingEnable(boolean enable) {
            this.mMyPopWindow.mClippEnable = enable;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress) {
            this.mMyPopWindow.mIgnoreCheekPress = ignoreCheekPress;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setInputMethodMode(int mode) {
            this.mMyPopWindow.mInputMode = mode;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener) {
            this.mMyPopWindow.mOnDismissListener = onDissmissListener;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setSoftInputMode(int softInputMode) {
            this.mMyPopWindow.mSoftInputMode = softInputMode;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setTouchable(boolean touchable) {
            this.mMyPopWindow.mTouchable = touchable;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter) {
            this.mMyPopWindow.mOnTouchListener = touchIntercepter;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder enableBackgroundDark(boolean isDark) {
            this.mMyPopWindow.mIsBackgroundDark = isDark;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder setBgDarkAlpha(float darkValue) {
            this.mMyPopWindow.mBackgroundDrakValue = darkValue;
            return this;
        }

        public MyPopWindow.PopupWindowBuilder enableOutsideTouchableDissmiss(boolean disMiss) {
            this.mMyPopWindow.enableOutsideTouchDisMiss = disMiss;
            return this;
        }

        public MyPopWindow create() {
            this.mMyPopWindow.build();
            return this.mMyPopWindow;
        }
    }


    public interface OnSelectedListener {
        void onItemClick(Object object);
    }
}
