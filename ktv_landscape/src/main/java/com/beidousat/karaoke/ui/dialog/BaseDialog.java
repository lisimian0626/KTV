package com.beidousat.karaoke.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.beidousat.karaoke.ui.BaseActivity;

/**
 * Created by J Wong on 2017/5/22.
 */

public class BaseDialog extends Dialog {

    //    AnimatorSet mAnimatorSet;
//    int mDuration = 700;
    public BaseDialog(Context context) {
        super(context);
//        init();
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
//        init();
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
//        init();
    }

//    private void init() {
//        mAnimatorSet = new AnimatorSet();
//    }

    @Override
    public void show() {
        Log.d(BaseDialog.class.getSimpleName(), "show");
        BaseActivity.mCanShowAd = false;
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d(BaseDialog.class.getSimpleName(), "dismiss");
        BaseActivity.mCanShowAd = true;
    }

//    public void enterAnim() {
//        if (getWindow().getDecorView() != null)
//            mAnimatorSet.playTogether(
//                    ObjectAnimator.ofFloat(getWindow().getDecorView(), "rotation", 1080, 720, 360, 0).setDuration(mDuration),
//                    ObjectAnimator.ofFloat(getWindow().getDecorView(), "alpha", 0, 1).setDuration(mDuration * 3 / 2),
//                    ObjectAnimator.ofFloat(getWindow().getDecorView(), "scaleX", 0.1f, 0.5f, 1).setDuration(mDuration),
//                    ObjectAnimator.ofFloat(getWindow().getDecorView(), "scaleY", 0.1f, 0.5f, 1).setDuration(mDuration));
//        else {
//            Logger.d(getClass().getSimpleName(), "enterAnim getDecorView is null !!!!");
//        }
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(BaseDialog.class.getSimpleName(), "mLastTouchTime");
        BaseActivity.mLastTouchTime = System.currentTimeMillis();
        return super.dispatchTouchEvent(ev);
    }

}
