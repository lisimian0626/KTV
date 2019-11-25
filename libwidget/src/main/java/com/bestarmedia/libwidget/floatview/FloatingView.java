package com.bestarmedia.libwidget.floatview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class FloatingView implements IFloatingView {

    private SparseArray<EnFloatingView> mEnFloatingViews = new SparseArray<>();
    private static volatile FloatingView mInstance;
    private FrameLayout mContainer;

    private FloatingView() {
    }

    public static FloatingView get() {
        if (mInstance == null) {
            synchronized (FloatingView.class) {
                if (mInstance == null) {
                    mInstance = new FloatingView();
                }
            }
        }
        return mInstance;
    }

    @Override
    public FloatingView removeAll() {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mEnFloatingViews == null || mEnFloatingViews.size() < 0) {
                return;
            }
            for (int i = 0; i < mEnFloatingViews.size(); i++) {
                int key = mEnFloatingViews.keyAt(i);
                remove(key);
            }
        });
        return this;
    }


    @Override
    public FloatingView remove(int key) {
        new Handler(Looper.getMainLooper()).post(() -> {
            EnFloatingView enFloatingView;
            if (mEnFloatingViews == null || mEnFloatingViews.size() < 0 || (enFloatingView = mEnFloatingViews.get(key)) == null) {
                return;
            }
            if (ViewCompat.isAttachedToWindow(enFloatingView) && mContainer != null) {
                mContainer.removeView(enFloatingView);
                mEnFloatingViews.remove(key);
            }
        });
        return this;
    }


    private void ensureMiniPlayer(Context context, int key, int width, int height) {
        synchronized (this) {
            EnFloatingView enFloatingView = new EnFloatingView(context.getApplicationContext());
            enFloatingView.setLayoutParams(getParams(width, height));
            addViewToWindow(enFloatingView);
            Log.d(getClass().getSimpleName(), "add float view key>>>>>>>>>> " + key);
            mEnFloatingViews.put(key, enFloatingView);
        }
    }

    @Override
    public FloatingView add(int key, int width, int height) {
        Log.d(getClass().getSimpleName(), "add key >>>> " + key);
        synchronized (this) {
            if (mEnFloatingViews.get(key) == null) {
                ensureMiniPlayer(EnContext.get(), key, width, height);
            }
        }
        return this;
    }

    @Override
    public FloatingView attach(Activity activity) {
        attach(getActivityRoot(activity));
        return this;
    }

    @Override
    public FloatingView attach(FrameLayout container) {
        if (container == null || mEnFloatingViews == null || mEnFloatingViews.size() <= 0) {
            mContainer = container;
            return this;
        }
        if (mEnFloatingViews.get(mEnFloatingViews.keyAt(0)).getParent() == container) {
            return this;
        }
        for (int i = 0; i < mEnFloatingViews.size(); i++) {
            int key = mEnFloatingViews.keyAt(i);
            EnFloatingView enFloatingView = mEnFloatingViews.get(key);
            if (mContainer != null && enFloatingView.getParent() == mContainer) {
                mContainer.removeView(enFloatingView);
            }
            mContainer = container;
            container.addView(enFloatingView);
        }
        return this;
    }

    @Override
    public FloatingView detach(Activity activity) {
        detach(getActivityRoot(activity));
        return this;
    }

    @Override
    public FloatingView detach(FrameLayout container) {
        if (mEnFloatingViews != null && container != null) {
            for (int i = 0; i < mEnFloatingViews.size(); i++) {
                int key = mEnFloatingViews.keyAt(i);
                EnFloatingView floatingView;
                if ((floatingView = mEnFloatingViews.get(key)) != null) {
                    if (ViewCompat.isAttachedToWindow(floatingView)) {
                        container.removeView(floatingView);
                    }
                }
            }
            if (mContainer == container) {
                mContainer = null;
            }
        }
        return this;
    }

    @Override
    public EnFloatingView getView(int key) {
        return mEnFloatingViews.get(key);
    }

    @Override
    public FloatingView icon(int key, @DrawableRes int resId) {
        if (mEnFloatingViews.get(key) != null) {
            mEnFloatingViews.get(key).setIconImage(resId);
        }
        return this;
    }

    @Override
    public FloatingView layoutParams(int key, ViewGroup.LayoutParams params) {
        if (mEnFloatingViews.get(key) != null) {
            mEnFloatingViews.get(key).setLayoutParams(params);
        }
        return this;
    }

    @Override
    public FloatingView listener(int key, MagnetViewListener magnetViewListener) {
        if (mEnFloatingViews.get(key) != null) {
            mEnFloatingViews.get(key).setMagnetViewListener(key, magnetViewListener);
        }
        return this;
    }

    private void addViewToWindow(final EnFloatingView view) {
        if (mContainer == null) {
            return;
        }
        Log.d(getClass().getSimpleName(), "add float view >>>>>>>>>> ");
        mContainer.addView(view);
    }

    private FrameLayout.LayoutParams getParams(int width, int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.gravity = Gravity.TOP | Gravity.START;
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin);
        return params;
    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void movie(int key, int x, int y) {
        if (mEnFloatingViews.get(key) != null) {
            mEnFloatingViews.get(key).moveTo(x, y);
        }
    }

    public void hideShowAll(boolean isHide) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (mEnFloatingViews == null || mEnFloatingViews.size() < 0) {
                return;
            }
            for (int i = 0; i < mEnFloatingViews.size(); i++) {
                int key = mEnFloatingViews.keyAt(i);
                EnFloatingView enFloatingView = mEnFloatingViews.get(key);
                if (ViewCompat.isAttachedToWindow(enFloatingView) && mContainer != null) {
                    enFloatingView.setVisibility(isHide ? View.GONE : View.VISIBLE);
                }
            }
        });
    }

}